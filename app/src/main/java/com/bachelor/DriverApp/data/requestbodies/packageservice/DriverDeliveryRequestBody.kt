package com.bachelor.DriverApp.data.requestbodies.packageservice

import com.google.gson.annotations.SerializedName
import java.util.*

data class DriverDeliveryRequestBody(
    @SerializedName("packageIDs") val packageIds: Array<UUID?>,
    @SerializedName("driverID") val driverId: String,
    @SerializedName("fakeScenario") val fakeScenario: Boolean
)
