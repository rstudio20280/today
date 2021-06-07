package com.study.today.feature.main.Fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.study.today.R
import com.study.today.databinding.FragmentSecondBinding
import com.study.today.feature.main.MainActivity
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


//위치권한
val PERMISSIONS_REQUEST_CODE = 100
var REQUIRED_PERMISSIONS = arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION)

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private var keyword =""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)

        val BASE_URL = getString(R.string.BASE_URL)
        val API_KEY = getString(R.string.API_KEY)

        val mapView = MapView(activity)
        binding.mapContainer.addView(mapView)
        mapView.setZoomLevel(7,true)

        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading) //현재위치에서 트래킹
        mapView.setShowCurrentLocationMarker(true)//현재위치 마커 생성

        //줌인
        binding.zoomInBtn.setOnClickListener{
            zoomIn(mapView)
        }

        //줌아웃
        binding.zoomOutBtn.setOnClickListener{
            zoomOut(mapView)
        }

        //현재 위치 확인 함수
        binding.mapPageLocationBtn.setOnClickListener {
            touchLocation(mapView)
        }

        return binding.root
    }

    //줌인
    private fun zoomIn(mapView : MapView){
        mapView.zoomIn(true)
    }

    //줌아웃
    private fun zoomOut(mapView: MapView){
        mapView.zoomOut(true)
    }

    //현재 위치
    private fun touchLocation(mapView: MapView){
        val permissionCheck =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
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
}