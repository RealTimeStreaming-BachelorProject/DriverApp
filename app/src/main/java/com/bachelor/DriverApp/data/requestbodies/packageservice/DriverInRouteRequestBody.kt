package com.bachelor.DriverApp.data.requestbodies.packageservice

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

data class DriverInRouteRequestBody(
    @SerializedName("packageIDs") val packageIds: ArrayList<UUID>,
    @SerializedName("driverID") val driverId: UUID,
    @SerializedName("fakeScenario") val fakeScenario: Boolean
) {
}