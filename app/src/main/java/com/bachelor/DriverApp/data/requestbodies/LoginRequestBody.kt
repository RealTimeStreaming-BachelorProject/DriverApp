package com.bachelor.DriverApp.data.requestbodies

import com.google.gson.annotations.SerializedName

data class LoginRequestBody(
        @SerializedName("username") val username: String,
        @SerializedName("password") val password: String
)
