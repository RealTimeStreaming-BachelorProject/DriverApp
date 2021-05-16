package com.bachelor.DriverApp.data.requestbodies.packageservice

import com.google.gson.annotations.SerializedName

data class DriverDeliveryRequestBody(
    @SerializedName("packageIDs") val packageIds: ArrayList<String>,
    @SerializedName("driverID") val driverId: String,
    @SerializedName("fakeScenario") val fakeScenario: Boolean
)
