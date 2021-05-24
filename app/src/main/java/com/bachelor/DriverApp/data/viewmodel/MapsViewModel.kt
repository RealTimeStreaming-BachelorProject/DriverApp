package com.bachelor.DriverApp.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng


class MapsViewModel(application: Application): AndroidViewModel(application) {

    private val _latLng = MutableLiveData<LatLng>(LatLng(0.0, 0.0));
    val latLng: LiveData<LatLng> get() = _latLng
}

data class AuthEvent (val token: String)

data class CoordEvent (val coordinate: Array<Double>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CoordEvent

        if (!coordinate.contentEquals(other.coordinate)) return false

        return true
    }

    override fun hashCode(): Int {
        return coordinate.contentHashCode()
    }
}