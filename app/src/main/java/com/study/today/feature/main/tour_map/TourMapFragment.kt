package com.study.today.feature.main.tour_map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
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
import androidx.fragment.app.Fragment
import com.study.today.R
import com.study.today.databinding.FragmentTourMapBinding
import com.study.today.feature.main.MainActivity
import com.study.today.utils.RunWithPermission
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

//주석추가!
//위치권한
val PERMISSIONS_REQUEST_CODE = 100
var REQUIRED_PERMISSIONS = arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION)

class TourMapFragment : Fragment() {

    private var _binding: FragmentTourMapBinding? = null
    private val binding get() = _binding!!
    private var keyword = ""

    private lateinit var runWithPermission: RunWithPermission

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTourMapBinding.inflate(inflater, container, false)
        getAppKeyHash()

        val BASE_URL = getString(R.string.BASE_URL)
        val API_KEY = getString(R.string.API_KEY)

        val mapView = MapView(activity)
        binding.mapContainer.addView(mapView)
        mapView.setZoomLevel(7, true)

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

        return binding.root
    }

    //줌인
    private fun zoomIn(mapView: MapView) {
        mapView.zoomIn(true)
    }

    //줌아웃
    private fun zoomOut(mapView: MapView) {
        mapView.zoomOut(true)
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