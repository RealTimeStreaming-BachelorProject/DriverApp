package com.bachelor.DriverApp.ui.maps

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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
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
import com.google.android.material.snackbar.Snackbar


class MapsFragment : Fragment() {

    private var mapFragment: SupportMapFragment? = null
    private lateinit var mapMarker: Marker
    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var parentView: View
    private var errorSnackBar: Snackbar? = null

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
                    .position(LatLng(mapsViewModel.latLng.value!!.latitude, mapsViewModel.latLng.value!!.longitude))
            )
        }

        val intent = Intent(requireContext(), LocationService::class.java)
        requireContext().startForegroundService(intent)
        listenForLocationUpdates()
        listenForConnectionErrors()
    }

    private fun listenForLocationUpdates() {
        val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(arg0: Context?, arg1: Intent) {
                val newLatLng = arg1.extras!!.get("latlng")
                updateMapLocation(newLatLng as LatLng)
                // TODO: Hide snackbar if connection comes back?
            }
        }
        requireContext().registerReceiver(broadcastReceiver, IntentFilter("new_latlng"))
    }

    private fun listenForConnectionErrors() {
        val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(arg0: Context?, arg1: Intent) {
                val errorMessage = arg1.extras!!.getString("error_message")
                println(errorMessage)
                if (errorMessage == null) return
                if (errorSnackBar != null) return
                errorSnackBar = Snackbar.make(parentView, errorMessage, Snackbar.LENGTH_INDEFINITE);
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