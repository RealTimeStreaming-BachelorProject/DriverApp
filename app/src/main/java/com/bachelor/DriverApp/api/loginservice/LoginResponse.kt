package com.bachelor.DriverApp.api.loginservice

import com.google.gson.annotations.SerializedName

class LoginResponse {

    @SerializedName("statusCode")
    var statusCode: Int = 0
    @SerializedName("message")
    var message: String = ""
    @SerializedName("token")
    var token: String = ""
}