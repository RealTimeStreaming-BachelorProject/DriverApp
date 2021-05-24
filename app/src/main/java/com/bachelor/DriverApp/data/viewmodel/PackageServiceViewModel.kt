package com.bachelor.DriverApp.data.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bachelor.DriverApp.R
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
import kotlin.random.Random

object PackageServiceViewModel : ViewModel() {

    private val packageService = ServiceBuilder().getPackageService()

    private val validPackageIDs = ArrayList<UUID>()

    val packages: MutableLiveData<ArrayList<PackageData>> = MutableLiveData()
        get() {
            if (field.value == null)
                field.value = ArrayList()
            return field
        }

    var deliveredPackageCounter = 0;

    private val errorMessage = SingleLiveEvent<Int>()
    fun getErrorMessage(): SingleLiveEvent<Int> {
        return errorMessage
    }

    init {
        this.packages.value = ArrayList()
    }

    fun driverPickUp(packageID: UUID) {
        if (DriverData.testUser) {
            addPackageDetails(packageID)
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
                    errorMessage.postValue(R.string.package_server_error)
                }
            } catch (e: Exception) {
                errorMessage.postValue(R.string.package_server_connection_error)
            }
        }
    }

    private fun addPackageDetails(packageID: UUID) {
        if (DriverData.testUser) {
            val packageData = PackageData(
                "Test Adresse nr. 42 5000 Odense C",
                12,
                packageID,
                Date(2021, 6, 1, Random.nextInt(8, 17),Random.nextInt(0, 60))
            )
            packages.value?.add(packageData)
            return
        }
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
                    errorMessage.postValue(R.string.package_server_error)
                }
            } catch (e: Exception) {
                errorMessage.postValue(R.string.package_server_connection_error)
            }
        }
    }

    fun driverDelivery(packageId: UUID?) {
        if (DriverData.testUser) {
            val deliveredPackage = packages.value?.find { packageData -> packageData.packageId == packageId }
            if (deliveredPackage != null) {
                deliveredPackage.delivered = true
            }
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
                    errorMessage.postValue(R.string.package_server_error)
                }
            } catch (e: Exception) {
                errorMessage.postValue(R.string.package_server_connection_error)
            }
        }
    }

    // NOTE: This app should not be registering new packages,
    // this logic happens when someone orders an item from a webshop
    // this is only for demo and testing purposes
    fun registerNewPackage() {
        if (DriverData.testUser) {
            val packageId = UUID.randomUUID()
            validPackageIDs.add(packageId)
            driverPickUp(packageId)
            return
        }
        viewModelScope.launch {
            val jsonBody = RegisterPackageRequestBody()
            try {
                val registerPackageResponse = withContext(Dispatchers.IO) {
                    packageService.register(jsonBody)
                }
                if (registerPackageResponse.isSuccessful) {
                    val packageId = registerPackageResponse.body()?.packageID!!
                    validPackageIDs.add(packageId)

                    driverPickUp(packageId)
                } else {
                    errorMessage.postValue(R.string.package_server_error)
                }
            } catch (e: Exception) {
                errorMessage.postValue(R.string.package_server_connection_error)
            }
        }
    }
}