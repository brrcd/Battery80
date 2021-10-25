package com.example.battery80

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.battery80.BatteryService.Companion.ACTION_STOP_SERVICE

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, BatteryService::class.java)
        startForegroundService(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        val stopIntent = Intent(this, BatteryService::class.java)
        stopIntent.action = ACTION_STOP_SERVICE
        startForegroundService(stopIntent)
    }
}