package com.bachelor.DriverApp.config

import java.util.*

object DriverData {
    var JWT = ""
        get() {
            return field
        }
        set(value) {
            field = value
        }
    var driverID: UUID? = null
        get() {
            return field
        }
        set(value) {
            field = value
        }

}