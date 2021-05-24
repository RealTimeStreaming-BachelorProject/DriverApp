package com.bachelor.DriverApp.ui.maps

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bachelor.DriverApp.R
import com.bachelor.DriverApp.data.viewmodel.MapsViewModel
import com.bachelor.DriverApp.services.LocationService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.Projection
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar


class MapsFragment : Fragment() {

    private var mapFragment: SupportMapFragment? = null
    private lateinit var mapMarker: Marker
    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var parentView: View
    private var errorSnackBar: Snackbar? = null
    private lateinit var btnStartService: Button
    private var isServiceStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapsViewModel = ViewModelProvider(this).get(MapsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentView = view
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        // Initial Google Maps
        mapFragment?.getMapAsync { googleMap ->
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
            mapMarker = googleMap.addMarker(
                MarkerOptions()
                    .position(
                        LatLng(
                            mapsViewModel.latLng.value!!.latitude,
                            mapsViewModel.latLng.value!!.longitude
                        )
                    )
            )
        }

        btnStartService = requireView().findViewById<Button>(R.id.btn_start_service)
        btnStartService.setOnClickListener{
            if (!isServiceStarted) {
                startLocationService()
                btnStartService.setText(R.string.btn_stop_service)
                isServiceStarted = true
            } else {
                stopLocationService()
                btnStartService.setText(R.string.btn_start_service)
                isServiceStarted = false
            }
        }

    }

    override fun onResume() {
        super.onResume()

    }

    private fun startLocationService() {
        val intent = Intent(requireContext(), LocationService::class.java)
        requireContext().startForegroundService(intent)
        listenForLocationUpdates()
        listenForConnectionErrors()
    }

    private fun stopLocationService() {
        val intent = Intent(requireContext(), LocationService::class.java)
        requireContext().stopService(intent)
    }

    private fun listenForLocationUpdates() {
        val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(arg0: Context?, arg1: Intent) {
                val newLatLng = arg1.extras!!.get("latlng")
                updateMapLocation(newLatLng as LatLng)
                if (errorSnackBar != null && errorSnackBar?.isShown() == true) {
                    // Hide snackbar if connection comes back
                    errorSnackBar?.dismiss()
                }
            }
        }
        requireContext().registerReceiver(broadcastReceiver, IntentFilter("new_latlng"))
    }

    // Show error if GPS service is not running locally. Doesn't break the app, but won't send coordinates to the server.
    private fun listenForConnectionErrors() {
        val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(arg0: Context?, arg1: Intent) {
                val errorMessage = arg1.extras!!.getString("error_message") ?: return
                if (errorSnackBar != null) return
                val navBar = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav) ?: return
                errorSnackBar = Snackbar.make(navBar, errorMessage, Snackbar.LENGTH_INDEFINITE).apply {
                    anchorView = navBar
                }
                errorSnackBar?.setTextColor(Color.YELLOW)
                errorSnackBar?.show()
            }
        }
        requireContext().registerReceiver(broadcastReceiver, IntentFilter("socket_error"))
    }

    private fun updateMapLocation(latLng: LatLng) {
        mapFragment?.getMapAsync { googleMap ->
            animateMarker(mapMarker, latLng, false, googleMap)
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        }
    }

    private fun animateMarker(
        marker: Marker, toPosition: LatLng,
        hideMarker: Boolean,
        googleMap: GoogleMap
    ) {
        val handler = Handler()
        val start = SystemClock.uptimeMillis()
        val proj: Projection = googleMap.projection
        val startPoint: Point = proj.toScreenLocation(marker.position)
        val startLatLng = proj.fromScreenLocation(startPoint)
        val duration: Long = 500
        val interpolator: Interpolator = LinearInterpolator()
        handler.post(object : Runnable {
            override fun run() {
                val elapsed = SystemClock.uptimeMillis() - start
                val t: Float = interpolator.getInterpolation(
                    elapsed.toFloat()
                            / duration
                )
                val lng = t * toPosition.longitude + (1 - t) * startLatLng.longitude
                val lat = t * toPosition.latitude + (1 - t) * startLatLng.latitude
                marker.position = LatLng(lat, lng)
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16)
                } else {
                    marker.isVisible = !hideMarker
                }
            }
        })
    }
}