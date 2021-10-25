package com.example.battery80

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.util.Log
import com.example.battery80.App.Companion.appContext
import com.example.battery80.BatteryService.Companion.ACTION_STOP_SERVICE

class BatteryBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Log.v("_TEST", "broadcast received")

        val serviceIntent = Intent(appContext, BatteryService::class.java)
        context.startForegroundService(serviceIntent)

        val status: Int = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING
                || status == BatteryManager.BATTERY_STATUS_FULL

        val batteryPct: Int? = intent?.let { it ->
            val level: Int = it.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale: Int = it.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            level * 100 / scale
        }

        if (isCharging && batteryPct == 80) {

        }
    }
}