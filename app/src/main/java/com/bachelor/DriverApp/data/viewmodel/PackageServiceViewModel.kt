package com.bachelor.DriverApp.data.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

object PackageServiceViewModel :ViewModel() {

    private val packageService = ServiceBuilder().getPackageService()

    private val validPackageIDs = ArrayList<UUID>()

    val packages: MutableLiveData<ArrayList<PackageData>> = MutableLiveData()
        get() {
            if (field.value == null)
                field.value = ArrayList()
            return field
        }

    init {
        this.packages.value = ArrayList()
    }

    fun driverPickUp(packageID: UUID, driverId: UUID) {
        viewModelScope.launch {
            val jsonBody = DriverInRouteRequestBody(arrayListOf(packageID), driverId, FAKE_SCENARIO)
            withContext(Dispatchers.IO) {
                val inRouteResponse = packageService.inRoute(jsonBody)
                if (inRouteResponse.isSuccessful) {
                    addPackageDetails(packageID)
                } else {

                }
            }

        }
    }

    private fun addPackageDetails(packageID: UUID) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val packageDetailsResponse = packageService.getPackageDetails(packageID.toString())
                if (packageDetailsResponse.isSuccessful) {
                    var responseBody = packageDetailsResponse.body()!!
                    var packageData = PackageData(
                        responseBody.receiverAddress,
                        responseBody.weightKg,
                        responseBody.packageID,
                        responseBody.expectedDeliveryTime
                    )

                    packages.value?.add(packageData)
                } else {
                    // TODO: Handle errors
                    print("Error")
                }
            }

        }
    }

    fun driverDelivery(packageIds: ArrayList<String>, driverId: String) {
        viewModelScope.launch {
            val jsonBody = DriverDeliveryRequestBody(packageIds, driverId, FAKE_SCENARIO)
            withContext(Dispatchers.IO) {
                val deliveryResponse = packageService.deliver(jsonBody)
                if (deliveryResponse.isSuccessful) {
                    for (packageID in packageIds) {
                        packages.value?.find { packageData -> packageData.packageId.equals(packageID) }
                    }
                } else {
                    // TODO: handle errors
                    print("Error2")
                }
            }
        }
    }

    // NOTE: This app should not be registering new packages,
    // this logic happens when someone orders an item from a webshop
    // this is only for demo and testing purposes
    fun registerNewPackage() {
        viewModelScope.launch {
            val jsonBody = RegisterPackageRequestBody()
            withContext(Dispatchers.IO) {
                val registerPackage = packageService.register(jsonBody)
                if (registerPackage.isSuccessful) {
                    validPackageIDs.add(registerPackage.body()?.packageID!!);
                } else {
                    // TODO: handle errors
                    print("Error2")
                }
            }
        }
    }

    fun getRandomValidPackageID() : UUID {
        return validPackageIDs.random()
    }

}