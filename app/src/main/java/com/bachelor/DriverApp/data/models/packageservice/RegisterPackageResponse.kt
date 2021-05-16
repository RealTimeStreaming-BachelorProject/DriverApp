package com.bachelor.DriverApp.data.models.packageservice

import com.google.gson.annotations.SerializedName
import java.util.*

class RegisterPackageResponse {
    @SerializedName("status")
    var status: Int = 0
    @SerializedName("message")
    var message: String = ""
    @SerializedName("packageID")
    lateinit var packageID: UUID
}