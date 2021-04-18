package com.bachelor.DriverApp.data

object Config {
    private const val LOGIN_SERVICE_HOST = "http://192.168.50.65:5005"
    const val LOGIN_SERVICE_URL = "$LOGIN_SERVICE_HOST/authentication/"
    private const val GPS_SERVICE_HOST = "http://10.186.19.135:5022"
    const val DRIVERS_URL = "$GPS_SERVICE_HOST/drivers/"
}