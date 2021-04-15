package com.bachelor.DriverApp.data.models

import com.google.gson.annotations.SerializedName

class APILoginResponse {

    @SerializedName("statusCode")
    var statusCode: Int = 0
    @SerializedName("message")
    var message: String = ""
    @SerializedName("token")
    var token: String = ""
}