package com.bachelor.DriverApp.broadcastreceivers

interface BatteryCallback {
    fun onLowBattery()
    fun onOkayBattery()
}