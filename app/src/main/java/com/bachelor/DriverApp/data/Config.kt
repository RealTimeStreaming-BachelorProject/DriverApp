package com.bachelor.DriverApp.data

object Config {
    private const val LOGIN_SERVICE_HOST = "http://192.168.50.65:5005"
    const val LOGIN_SERVICE_URL = "$LOGIN_SERVICE_HOST/authentication/"
    private const val PACKAGE_SERVICE_HOST = "http://192.168.50.65:9001"
    const val PACKAGE_SERVICE_URL = "$PACKAGE_SERVICE_HOST/packages/"


    const val FAKE_SCENARIO = true;
}