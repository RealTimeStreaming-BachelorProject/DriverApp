package com.bachelor.DriverApp.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bachelor.DriverApp.data.Config.FAKE_SCENARIO
import com.bachelor.DriverApp.data.models.packageservice.PackageData
import com.bachelor.DriverApp.data.repository.ServiceBuilder
import com.bachelor.DriverApp.data.requestbodies.packageservice.DriverInRouteRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PackageServiceViewModel :ViewModel() {
    private val packageService = ServiceBuilder().getPackageService()

    private val _packages = MutableLiveData<Array<PackageData>>()
    val packages: LiveData<Array<PackageData>>
        get() = _packages

    fun postInRoute(packageIds: Array<String>, driverId: String) {
        viewModelScope.launch {
            val jsonBody = DriverInRouteRequestBody(packageIds, driverId, FAKE_SCENARIO)
            withContext(Dispatchers.IO) {
                val loginResponse = packageService.inRoute(jsonBody)
                if (loginResponse.isSuccessful) {
                    // TODO:
                } else {

                }
            }

        }
    }
}