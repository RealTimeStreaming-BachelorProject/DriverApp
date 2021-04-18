package com.bachelor.DriverApp.data.repository.loginservice

import com.bachelor.DriverApp.data.models.loginservice.LoginResponse
import com.bachelor.DriverApp.data.requestbodies.loginservice.LoginRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequestBody) : Response<LoginResponse>
}