package com.bachelor.DriverApp.data.viewmodel

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bachelor.DriverApp.data.Config
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import org.json.JSONObject
import java.net.URISyntaxException


class MapsViewModel(application: Application): AndroidViewModel(application) {

    private val _longitude = MutableLiveData<Double>(0.0);
    val longitude: LiveData<Double> get() = _longitude;

    private val _latitude = MutableLiveData<Double>(0.0);
    val latitude: LiveData<Double> get() = _latitude;

    private val context = application.applicationContext
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var mSocket: Socket

    fun startLocationUpdates(locationCallback: LocationCallback) {
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
        locationRequest.interval = 100
        locationRequest.fastestInterval = 100
        locationRequest.smallestDisplacement = 1f


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
            mSocket = IO.socket(Config.DRIVERS_URL, opts)
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