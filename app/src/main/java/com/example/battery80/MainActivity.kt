package com.example.battery80

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.battery80.App.Companion.appContext
import com.example.battery80.BatteryService.Companion.ACTION_STOP_SERVICE

class MainActivity : AppCompatActivity() {
    private var receiver: BatteryBroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        //TODO remove
        Log.v("_TEST", "onDestroy activity")
    }
}