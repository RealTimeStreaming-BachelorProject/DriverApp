package com.bachelor.DriverApp.data.repository

import com.bachelor.DriverApp.data.Config
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceBuilder {

    private var retrofit: Retrofit = Retrofit.Builder().baseUrl(Config.LOGIN_SERVICE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getLoginService(): LoginService {
        return retrofit.create(LoginService::class.java);
    }
}