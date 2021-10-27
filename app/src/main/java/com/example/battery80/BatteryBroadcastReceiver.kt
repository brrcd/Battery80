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

class BatteryBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        //TODO remove
        Log.v("_TEST", "broadcast received")
        Toast.makeText(context, "broadcast", Toast.LENGTH_SHORT).show()

        val status: Int = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING
                || status == BatteryManager.BATTERY_STATUS_FULL

        val batteryPct: Int? = intent?.let { it ->
            val level: Int = it.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale: Int = it.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            level * 100 / scale
        }

        val serviceIntent = Intent(appContext, BatteryService::class.java)
        serviceIntent.putExtra(NOTIFICATION_TITLE, "$isCharging")
        serviceIntent.putExtra(NOTIFICATION_TEXT, "$batteryPct")

        if (isCharging) {
            serviceIntent.putExtra(SOUND_FLAG, true)
        }

        context.startForegroundService(serviceIntent)
    }
}