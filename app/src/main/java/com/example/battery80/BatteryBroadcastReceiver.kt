package com.example.battery80

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.util.Log
import android.widget.Toast
import com.example.battery80.App.Companion.appContext
import com.example.battery80.BatteryService.Companion.NOTIFICATION_TEXT
import com.example.battery80.BatteryService.Companion.NOTIFICATION_TITLE
import com.example.battery80.BatteryService.Companion.SOUND_FLAG

class BatteryBroadcastReceiver : BroadcastReceiver() {
    private var isFirstRun = true
    private var isRestarting = false
    private var isCharging: Boolean? = null
    private var prevChargingState: Boolean? = null

    override fun onReceive(context: Context, intent: Intent?) {

        val status: Int = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1

        isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING
                || status == BatteryManager.BATTERY_STATUS_FULL

        if (isCharging != prevChargingState) {
            isRestarting = true
        }
        prevChargingState = isCharging

        val batteryPct: Int? = intent?.let { it ->
            val level: Int = it.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale: Int = it.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            level * 100 / scale
        }

        val serviceIntent = Intent(appContext, BatteryService::class.java)
        serviceIntent.putExtra(NOTIFICATION_TITLE, "$isCharging")
        serviceIntent.putExtra(NOTIFICATION_TEXT, "$batteryPct")

        //TODO filter intent to send only when percentage changes/charging on/off

        if (isCharging!! && batteryPct == 80) {
            serviceIntent.putExtra(SOUND_FLAG, true)
            isRestarting = true
        }

        if (isFirstRun || isRestarting) {
            context.startForegroundService(serviceIntent)
            isFirstRun = false
            isRestarting = false
        }
    }
}