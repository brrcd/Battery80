package com.example.battery80

import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.battery80.App.Companion.appContext
import com.example.battery80.BatteryService.Companion.ACTION_STOP_SERVICE
import com.example.battery80.BatteryService.Companion.NOTIFICATION_RINGTONE
import com.example.battery80.BatteryService.Companion.NOTIFICATION_VIBRATION
import com.example.battery80.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var receiver: BatteryBroadcastReceiver? = null
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initReceiver()

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVibrationSwitch()
        initRingtonePicker()
    }

    // TODO notification sound picked instead of ringtones
    private fun initRingtonePicker() {
        val preferences = getSharedPreferences(NOTIFICATION_RINGTONE, MODE_PRIVATE)
        val ringtonePicker = registerForActivityResult(RingtoneContract()) { uri ->
            preferences.edit()
                .putString(NOTIFICATION_RINGTONE, uri.toString())
                .apply()
        }

        // TODO selected ringtone when ringtone picker open
        binding.buttonPickRingtone.setOnClickListener {
            ringtonePicker.launch(1)
        }
    }

    private fun initVibrationSwitch() {
        val preferences = getSharedPreferences(NOTIFICATION_VIBRATION, MODE_PRIVATE)
        binding.switchVibration.setOnCheckedChangeListener { _, isChecked ->
            preferences.edit()
                .putBoolean(NOTIFICATION_VIBRATION, isChecked)
                .apply()
        }
        binding.switchVibration.isChecked = preferences.getBoolean(NOTIFICATION_VIBRATION, false)
    }

    private fun initReceiver() {
        receiver = BatteryBroadcastReceiver()
        registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null

        //TODO FIXME app does not closing
        if (receiver != null) {
            unregisterReceiver(receiver)
        }

        val stopIntent = Intent(appContext, BatteryService::class.java).also {
            it.action = ACTION_STOP_SERVICE
        }
        startForegroundService(stopIntent)
    }
}