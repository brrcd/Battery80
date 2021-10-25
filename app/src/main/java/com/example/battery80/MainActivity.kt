package com.example.battery80

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.battery80.App.Companion.appContext
import com.example.battery80.BatteryService.Companion.ACTION_STOP_SERVICE

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerReceiver(BatteryBroadcastReceiver(), IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    override fun onDestroy() {
        super.onDestroy()
        val stopIntent = Intent(appContext, BatteryService::class.java)
        stopIntent.action = ACTION_STOP_SERVICE
        startForegroundService(stopIntent)
        unregisterReceiver(BatteryBroadcastReceiver())
    }
}