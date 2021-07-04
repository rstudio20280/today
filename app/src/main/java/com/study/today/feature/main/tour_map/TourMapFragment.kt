package com.study.today.feature.main.tour_map

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.study.today.R
import com.study.today.databinding.FragmentTourMapBinding
import com.study.today.databinding.RangeAreaListBinding
import com.study.today.feature.main.MainActivity
import com.study.today.feature.main.search.SearchViewModel
import com.study.today.utils.RunWithPermission
import net.daum.mf.map.api.MapCircle
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import timber.log.Timber
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

//주석추가!
//위치권한
val PERMISSIONS_REQUEST_CODE = 100
var REQUIRED_PERMISSIONS = arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION)
const val EXTRA_NOTICE_SAVE = "EXTRA_NOTICE_SAVE"
const val TAG_DIALOG_EVENT = "TAG_DIALOG_EVENT"

class TourMapFragment : Fragment(), MapView.CurrentLocationEventListener{

    private var _binding: FragmentTourMapBinding? = null
    private val binding get() = _binding!!
    private var keyword = ""
    private var mCurrentLat : Double = 0.0
    private var mCurrentLng : Double = 0.0
    private var range : Int = 1000 //1km
    var isTrackingMode = false

    private lateinit var runWithPermission: RunWithPermission
    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTourMapBinding.inflate(inflater, container, false)
        //getAppKeyHash()

        val BASE_URL = getString(R.string.BASE_URL)
        val API_KEY = getString(R.string.API_KEY)


        val mapView = MapView(activity)
        binding.mapContainer.addView(mapView)
        mapView.setZoomLevel(7, true)
        mapView.setCurrentLocationEventListener(this)

        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        viewModel.searchResult.observe(viewLifecycleOwner, {
            // TODO: 결과로 전달받은 목록 처리
            Timber.i(it.toString())
            it.forEach {
                var x=it.mapx //lat
                var y=it.mapy //lng

                val marker = MapPOIItem()
                val mapPoint = MapPoint.mapPointWithGeoCoord(y, x)
                mapView.removePOIItem(marker)
                marker.itemName = it.title
                marker.mapPoint = mapPoint
                marker.setMarkerType(MapPOIItem.MarkerType.RedPin)
                mapView.addPOIItem(marker)
                var aaa = ""
                aaa = it.title
                Toast.makeText(context, aaa, Toast.LENGTH_SHORT).show()
            }
            val mk = MapPOIItem()

            val testpoint = MapPoint.mapPointWithGeoCoord(mCurrentLat, mCurrentLng)
            mk.mapPoint = testpoint
            mk.setMarkerType(MapPOIItem.MarkerType.YellowPin)
            mapView.addPOIItem(mk)

        })
        viewModel.isLoading.observe(viewLifecycleOwner, {
            // TODO: 처리중일때 처리
//            binding.progress.isVisible = it
        })
        viewModel.toastMsgResId.observe(viewLifecycleOwner, {
            Toast.makeText(
                requireContext(),
                getString(it),
                Toast.LENGTH_SHORT
            ).show()
        })

        runWithPermission = RunWithPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        runWithPermission.setActionWhenGranted {
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading) //현재위치에서 트래킹
            mapView.setShowCurrentLocationMarker(true)//현재위치 마커 생성

        }.setActionWhenDenied {
            it.requestPermission()
        }.setActionInsteadPopup {
            it.startPermissionIntent()
        }.run()

        //줌인
        binding.zoomInBtn.setOnClickListener {
            zoomIn(mapView)
        }

        //줌아웃
        binding.zoomOutBtn.setOnClickListener {
            zoomOut(mapView)
        }

        //현재 위치 확인 함수
        binding.mapPageLocationBtn.setOnClickListener {
            touchLocation(mapView)
        }

        //주변 관광지 조회
        binding.searchBtn.setOnClickListener{
            InScopeSearch(mapView)
        }
        
        //관광지 조회 범위 지정
        binding.range.setOnClickListener{
            ShowRangeDialog(requireContext())
        }
        
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    //MapView.CurrentLocationEventListener 인터페이스
    //현위치 좌표 얻기
    override fun onCurrentLocationUpdate(mapView: MapView, mapPoint: MapPoint, accuracyInMeters: Float) {
        val mapPointGeo = mapPoint.mapPointGeoCoord
        val currentMapPoint = MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude)
        //이 좌표로 지도 중심 이동
        mapView.setMapCenterPoint(currentMapPoint, true)

        //전역변수로 현재 좌표 저장
        mCurrentLat = mapPointGeo.latitude
        mCurrentLng = mapPointGeo.longitude
        //트래킹 모드가 아닌 단순 현재위치 업데이트일 경우, 한번만 위치 업데이트하고 트래킹을 중단시키기 위한 로직
        if (!isTrackingMode) {
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff)

        }
    }
    //단말의 방향 각도값 얻기
    override fun onCurrentLocationDeviceHeadingUpdate(mapView : MapView, headingAngle : Float ){}
    //현위치 갱신 실패시 호출
    override fun onCurrentLocationUpdateFailed(mapView : MapView){}
    //트래킹 기능 취소될 경우 호출
    override fun onCurrentLocationUpdateCancelled(mapView : MapView){}



    //줌인
    private fun zoomIn(mapView: MapView) {
        mapView.zoomIn(true)
    }

    //줌아웃
    private fun zoomOut(mapView: MapView) {
        mapView.zoomOut(true)
    }

    //주변 관광지 조회
    private fun InScopeSearch(mapView: MapView){
        DrawCircle(mapView,3000)
        viewModel.search(mCurrentLat,mCurrentLng, 3000)
    }

    //관광지 조회 범위 지정
    private fun ShowRangeDialog(context: Context){
        val bundle = Bundle()
        //bundle.putParcelable(EXTRA_NOTICE_SAVE, range)
        val dialog = RangeDialogFragment()
        dialog.arguments = bundle
        activity?.supportFragmentManager?.let{ fragmentManager ->
            dialog.show(fragmentManager, TAG_DIALOG_EVENT) }
    }

    //범위 그리기
    private fun DrawCircle(mapView: MapView,range:Int = 3000){
        mapView.removeAllCircles()
        mapView.addCircle(MapCircle(MapPoint.mapPointWithGeoCoord(mCurrentLat,mCurrentLng),range,
            Color.argb(128,255,0,0),Color.argb(0,0,0,0)))
    }

    //현재 위치
    private fun touchLocation(mapView: MapView) {
        val permissionCheck =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            val lm = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            try {
                val userNowLocation: Location? =
                    lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                val uLatitude = userNowLocation?.latitude
                val uLongitude = userNowLocation?.longitude
                val uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude!!, uLongitude!!)
                mapView.setMapCenterPoint(uNowPosition, true)
                mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading) //현재위치에서 트래킹
                mapView.setShowCurrentLocationMarker(true)//현재위치 마커 생성
                //val mapPoint = MapPoint
                //val mapPointGeo: GeoCoordinate = mapPoint.getMapPointGeoCoord()
            } catch (e: NullPointerException) {
                Log.e("LOCATION_ERROR", e.toString())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ActivityCompat.finishAffinity(requireActivity())
                } else {
                    ActivityCompat.finishAffinity(requireActivity())
                }

                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
                System.exit(0)
            }
        } else {
            Toast.makeText(requireContext(), "위치 권한이 없습니다.", Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                PERMISSIONS_REQUEST_CODE
            )
        }
    }


    private fun getAppKeyHash() {
        try {
            val info: PackageInfo =
                requireContext().getPackageManager().getPackageInfo(
                    requireContext().getPackageName(),
                    PackageManager.GET_SIGNATURES
                )
            for (signature in info.signatures) {
                var md: MessageDigest
                md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val something = String(Base64.encode(md.digest(), 0))
                Log.e("Hash key", something)
            }
        } catch (e: NameNotFoundException) {
            Log.e("name not found", e.toString())
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
    }
}