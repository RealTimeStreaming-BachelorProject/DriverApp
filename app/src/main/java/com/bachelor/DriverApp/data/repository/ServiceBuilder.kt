package com.bachelor.DriverApp.data.repository

import com.bachelor.DriverApp.config.Urls
import com.bachelor.DriverApp.data.repository.loginservice.LoginService
import com.bachelor.DriverApp.data.repository.packageservice.PackageService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceBuilder {

    private var loginServiceRetrofit: Retrofit = Retrofit.Builder().baseUrl(Urls.LOGIN_SERVICE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var packageServiceRetrofit: Retrofit = Retrofit.Builder().baseUrl(Urls.PACKAGE_SERVICE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getLoginService(): LoginService {
        return loginServiceRetrofit.create(LoginService::class.java)
    }

    fun getPackageService(): PackageService {
        return packageServiceRetrofit.create(PackageService::class.java)
    }
}