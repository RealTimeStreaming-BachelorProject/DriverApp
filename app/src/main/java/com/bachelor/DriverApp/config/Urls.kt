package com.bachelor.DriverApp.config

object Urls {
    private const val LOGIN_SERVICE_HOST = "http://192.168.50.65:5005"
    const val LOGIN_SERVICE_URL = "$LOGIN_SERVICE_HOST/authentication/"
    private const val PACKAGE_SERVICE_HOST = "http://192.168.50.65:9001"
    const val PACKAGE_SERVICE_URL = "$PACKAGE_SERVICE_HOST/packages/"

    private const val GPS_SERVICE_HOST = "http://10.186.19.135:5002"
    const val DRIVERS_URL = "$GPS_SERVICE_HOST/drivers"

    const val FAKE_SCENARIO = true;
}