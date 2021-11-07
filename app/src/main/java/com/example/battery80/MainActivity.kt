package com.example.battery80

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.battery80.App.Companion.appContext
import com.example.battery80.BatteryService.Companion.ACTION_STOP_SERVICE

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private var receiver: BatteryBroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        receiver = BatteryBroadcastReceiver()

        registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    override fun onDestroy() {
        super.onDestroy()

        //TODO FIXME app does not closing
        if (receiver != null) {
            unregisterReceiver(receiver)
        }

        val stopIntent = Intent(appContext, BatteryService::class.java)
        stopIntent.action = ACTION_STOP_SERVICE
        startForegroundService(stopIntent)
    }
}