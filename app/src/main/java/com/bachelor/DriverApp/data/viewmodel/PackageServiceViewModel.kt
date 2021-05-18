package com.bachelor.DriverApp.data.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bachelor.DriverApp.config.DriverData
import com.bachelor.DriverApp.config.Urls.FAKE_SCENARIO
import com.bachelor.DriverApp.data.models.packageservice.PackageData
import com.bachelor.DriverApp.data.repository.ServiceBuilder
import com.bachelor.DriverApp.data.requestbodies.packageservice.DriverDeliveryRequestBody
import com.bachelor.DriverApp.data.requestbodies.packageservice.DriverInRouteRequestBody
import com.bachelor.DriverApp.data.requestbodies.packageservice.RegisterPackageRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

object PackageServiceViewModel : ViewModel() {

    private val packageService = ServiceBuilder().getPackageService()

    private val validPackageIDs = ArrayList<UUID>()

    val packages: MutableLiveData<ArrayList<PackageData>> = MutableLiveData()
        get() {
            if (field.value == null)
                field.value = ArrayList()
            return field
        }

    var packageInteractionCounter = 0;

    private val errorMessage = SingleLiveEvent<String>()
    fun getErrorMessage(): SingleLiveEvent<String> {
        return errorMessage
    }

    init {
        this.packages.value = ArrayList()
    }

    fun driverPickUp(packageID: UUID) {
        if (DriverData.driverID == null) {
            errorMessage.postValue("Invalid driverID, make sure you are logged in properly")
            return
        }
        viewModelScope.launch {
            val jsonBody = DriverInRouteRequestBody(arrayListOf(packageID),
                DriverData.driverID!!, FAKE_SCENARIO)
            try {
                val inRouteResponse = withContext(Dispatchers.IO) {
                    packageService.inRoute(jsonBody)
                }
                if (inRouteResponse.isSuccessful) {
                    addPackageDetails(packageID)
                } else {
                    errorMessage.postValue(inRouteResponse.message())
                }
            } catch (e: Exception) {
                errorMessage.postValue("Could not contact package server")
            }
        }
    }

    private fun addPackageDetails(packageID: UUID) {
        viewModelScope.launch {
            try {
                val packageDetailsResponse = withContext(Dispatchers.IO) {
                    packageService.getPackageDetails(packageID.toString())
                }
                if (packageDetailsResponse.isSuccessful) {
                    val responseBody = packageDetailsResponse.body()!!
                    val packageData = PackageData(
                        responseBody.receiverAddress,
                        responseBody.weightKg,
                        responseBody.packageID,
                        responseBody.expectedDeliveryTime
                    )

                    packages.value?.add(packageData)
                } else {
                    errorMessage.postValue(packageDetailsResponse.message())
                }
            } catch (e: Exception) {
                errorMessage.postValue("Could not contact package server")
            }
        }
    }

    fun driverDelivery(packageId: UUID?) {
        if (DriverData.driverID == null) {
            errorMessage.postValue("Invalid driverID, make sure you are logged in properly")
            return
        }
        viewModelScope.launch {
            val packageIds = Array(1) { packageId }

            val jsonBody = DriverDeliveryRequestBody(packageIds, DriverData.driverID.toString(), FAKE_SCENARIO)
            try {
                val deliveryResponse = withContext(Dispatchers.IO) {
                    packageService.deliver(jsonBody)
                }
                if (deliveryResponse.isSuccessful) {
                    for (packageID in packageIds) {
                        val deliveredPackage = packages.value?.find { packageData -> packageData.packageId == packageID }
                        if (deliveredPackage != null) {
                            deliveredPackage.delivered = true
                        }
                    }
                } else {
                    errorMessage.postValue(deliveryResponse.message())
                }
            } catch (e: Exception) {
                errorMessage.postValue("Could not contact package server")
            }
        }
    }

    // NOTE: This app should not be registering new packages,
    // this logic happens when someone orders an item from a webshop
    // this is only for demo and testing purposes
    fun registerNewPackage() {
        if (DriverData.driverID == null) {
            errorMessage.postValue("Invalid driverID, make sure you are logged in properly")
            return
        }
        viewModelScope.launch { // runs on the UI thread by default
            val jsonBody = RegisterPackageRequestBody()
            try {
                val registerPackageResponse = withContext(Dispatchers.IO) {
                    packageService.register(jsonBody)
                }
                if (registerPackageResponse.isSuccessful) {
                    validPackageIDs.add(registerPackageResponse.body()?.packageID!!)

                    driverPickUp(
                        getRandomValidPackageID()
                    )
                } else {
                    errorMessage.postValue(registerPackageResponse.message())
                }
            } catch (e: Exception) {
                errorMessage.postValue("Could not contact package server")
            }
        }
    }

    fun getRandomValidPackageID(): UUID {
        return validPackageIDs.random()
    }

}