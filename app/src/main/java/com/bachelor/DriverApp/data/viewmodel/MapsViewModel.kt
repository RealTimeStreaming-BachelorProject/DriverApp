package com.bachelor.DriverApp.data.viewmodel

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Looper
import androidx.annotation.MainThread
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bachelor.DriverApp.config.Urls
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import org.json.JSONObject
import java.net.URISyntaxException


class MapsViewModel(application: Application): AndroidViewModel(application) {

    private val _latLng = MutableLiveData<LatLng>(LatLng(0.0, 0.0));
    val latLng: LiveData<LatLng> get() = _latLng
}

data class AuthEvent (val token: String)
data class CoordEvent (val coordinate: Array<Double>)