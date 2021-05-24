package com.bachelor.DriverApp.data.requestbodies.packageservice

import com.google.gson.annotations.SerializedName
import java.util.*

data class DriverDeliveryRequestBody(
    @SerializedName("packageIDs") val packageIds: Array<UUID?>,
    @SerializedName("driverID") val driverId: String,
    @SerializedName("fakeScenario") val fakeScenario: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DriverDeliveryRequestBody

        if (!packageIds.contentEquals(other.packageIds)) return false
        if (driverId != other.driverId) return false
        if (fakeScenario != other.fakeScenario) return false

        return true
    }

    override fun hashCode(): Int {
        var result = packageIds.contentHashCode()
        result = 31 * result + driverId.hashCode()
        result = 31 * result + fakeScenario.hashCode()
        return result
    }
}
