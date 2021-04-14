package com.bachelor.DriverApp.api.loginservice

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginService {
    @GET("login")
    fun Login(@Query("username") username: String, @Query("password") password: String) : Call<LoginResponse>
}