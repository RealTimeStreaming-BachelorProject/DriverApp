package com.bachelor.DriverApp.data.models.packageservice

import com.google.gson.annotations.SerializedName

class DeliveryResponse {
    @SerializedName("status")
    var status: Int = 0
    @SerializedName("message")
    var message: String = ""
}