package com.bachelor.DriverApp.ui.maps

import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.bachelor.DriverApp.R
import com.bachelor.DriverApp.data.viewmodel.MapsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.Projection
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapsFragment : Fragment() {

    private var mapFragment: SupportMapFragment? = null
    private lateinit var mapMarker: Marker
    private lateinit var mapsViewModel: MapsViewModel

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
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        // Initial Google Maps
        mapFragment?.getMapAsync { googleMap ->
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
            mapMarker = googleMap.addMarker(
                MarkerOptions()
                    .position(LatLng(mapsViewModel.latLng.value!!.latitude, mapsViewModel.latLng.value!!.longitude))
            )
        }

        mapsViewModel.latLng.observe(requireActivity()) { langLng ->
            run {
                mapFragment?.getMapAsync { googleMap ->
                    animateMarker(mapMarker, langLng, false, googleMap)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(langLng))
                }

            }
        }

        mapsViewModel.startLocationUpdates()
    }

    fun animateMarker(
        marker: Marker, toPosition: LatLng,
        hideMarker: Boolean,
        googleMap: GoogleMap
    ) {
        val handler = Handler()
        val start = SystemClock.uptimeMillis()
        val proj: Projection = googleMap.getProjection()
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
                marker.setPosition(LatLng(lat, lng))
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16)
                } else {
                    if (hideMarker) {
                        marker.isVisible = false
                    } else {
                        marker.isVisible = true
                    }
                }
            }
        })
    }
}