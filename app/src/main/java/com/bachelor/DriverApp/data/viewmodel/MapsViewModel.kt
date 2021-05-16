package com.bachelor.DriverApp.data.viewmodel

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bachelor.DriverApp.config.Urls
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import io.socket.client.Socket
import kotlinx.coroutines.Dispatchers.IO
import okhttp3.WebSocket
import org.json.JSONObject
import java.net.URISyntaxException


class MapsViewModel(application: Application): AndroidViewModel(application) {

    private val _latLng = MutableLiveData<LatLng>(LatLng(0.0, 0.0));
    val latLng: LiveData<LatLng> get() = _latLng

    private val context = application.applicationContext
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var mSocket: Socket

    fun startLocationUpdates() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        createSocket();

        var locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 3000
        locationRequest.fastestInterval = 100
        locationRequest.smallestDisplacement = 1f

        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    val newLatLng = LatLng(location.latitude, location.longitude)
                    _latLng.postValue(newLatLng)

                    val gson = Gson()
                    val obj = JSONObject(gson.toJson(CoordEvent(arrayOf(location.latitude, location.longitude))))
                    mSocket.emit("new_coordinates", obj)
                }
            }
        }


        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun createSocket() {
        try {
            val opts = IO.Options()
            opts.transports = arrayOf(WebSocket.NAME)
            mSocket = IO.socket(Urls.DRIVERS_URL, opts)
            mSocket.connect()
            mSocket.on(Socket.EVENT_CONNECT_ERROR, { error -> println("SOCKETIO:  error"); println(error.forEach { err -> println(err.toString()) }) })
            mSocket.on(Socket.EVENT_CONNECT, { println("SOCKETIO: connected") })
            mSocket.on(Socket.EVENT_DISCONNECT, { println("SOCKETIO: disconnected") })
            var token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Im5pY29sYWlncmFtIiwiZHJpdmVySUQiOiI2MjgzODhiMy1kYjM4LTQyMzgtOWRiYS00MjgyYmY2Y2E0ZmQifQ.UYPoCta13O-qpPa_oybbDU6S8FhClciM58efY0FZiwc"
            val gson = Gson()
            val obj = JSONObject(gson.toJson(AuthEvent(token)))
            mSocket.emit("authenticate", obj)
        } catch (e: URISyntaxException) {
            println("Error connecting to socket")
        }
    }

}

data class AuthEvent (val token: String)
data class CoordEvent (val coordinate: Array<Double>)