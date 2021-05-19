package com.bachelor.DriverApp.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class LowBatteryReceiver : BroadcastReceiver() {
    private var listeners: ArrayList<BatteryCallback> = ArrayList()
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null)
            return

        if (intent.action == "android.intent.action.BATTERY_OKAY") {
            for (listener in listeners)
                listener.onOkayBattery()
        } else if (intent.action == "android.intent.action.BATTERY_LOW") {
            for (listener in listeners)
                listener.onLowBattery()
        }
    }

    fun registerBatteryCallback(listener :BatteryCallback) {
        listeners.add(listener)
    }
}