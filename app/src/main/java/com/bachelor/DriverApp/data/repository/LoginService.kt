package com.bachelor.DriverApp.data.repository

import com.bachelor.DriverApp.data.models.APILoginResponse
import com.bachelor.DriverApp.data.requestbodies.LoginRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequestBody) : Response<APILoginResponse>
}