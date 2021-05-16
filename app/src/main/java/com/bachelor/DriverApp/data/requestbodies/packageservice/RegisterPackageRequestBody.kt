package com.bachelor.DriverApp.data.requestbodies.packageservice

import com.google.gson.annotations.SerializedName

data class RegisterPackageRequestBody (
    @SerializedName("receiverAddress") val receiverAddress: String = "Hans Jensens Stræde 45, 5000 Odense",
    @SerializedName("receiverName") val receiverName: String = "H.C. Andersen",
    @SerializedName("receiverEmail") val receiverEmail: String = "hcswag69@gmail.com",
    @SerializedName("senderAddress") val senderAddress: String = "Bergvägen 1, SE-341 42 LJUNGBY, SWEDEN",
    @SerializedName("senderName") val senderName: String = "Komplett.dk",
    @SerializedName("weightKg") val weightKg: Int = 6,
    @SerializedName("fakeScenario") val fakeScenario: Boolean = true
    )