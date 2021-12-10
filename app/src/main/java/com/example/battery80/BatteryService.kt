package com.example.battery80

import android.app.*
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.IBinder
import android.util.Log
import com.example.battery80.App.Companion.appContext

class BatteryService : Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addNextIntentWithParentStack(launchIntent)

        val pendingIntent: PendingIntent = stackBuilder.getPendingIntent(
            0,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val title: String? = intent.getStringExtra(NOTIFICATION_TITLE)
        val text: String? = intent.getStringExtra(NOTIFICATION_TEXT)

        val notification: Notification
        val notificationChannel: NotificationChannel
        val isSoundOn = intent.getBooleanExtra(SOUND_FLAG, false)

        if (isSoundOn) {
            notification = Notification.Builder(this, CHANNEL_ID_SOUND)
                .setContentTitle("$title - $text")
                .setSmallIcon(R.drawable.ic_battery_full)
                .setContentIntent(pendingIntent)
                .build()

            val defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString()
            val ringtonePref = appContext.getSharedPreferences(NOTIFICATION_RINGTONE, MODE_PRIVATE)
            val vibrationPref = appContext.getSharedPreferences(NOTIFICATION_VIBRATION, MODE_PRIVATE)

            val uri = Uri.parse(ringtonePref.getString(NOTIFICATION_RINGTONE, defaultRingtoneUri))
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            notificationChannel = NotificationChannel(
                CHANNEL_ID_SOUND, "channel with sound",
                NotificationManager.IMPORTANCE_HIGH
            )
                .also {
                    it.setSound(uri, audioAttributes)
                    it.enableVibration(vibrationPref.getBoolean(NOTIFICATION_VIBRATION, false))
                }
        } else {
            notification = Notification.Builder(this, CHANNEL_ID_SILENT)
                .setContentTitle("$title - $text")
                .setSmallIcon(R.drawable.ic_battery_full)
                .setContentIntent(pendingIntent)
                .build()

            notificationChannel = NotificationChannel(
                CHANNEL_ID_SILENT, "silent channel",
                NotificationManager.IMPORTANCE_LOW
            )
        }

        notificationManager.createNotificationChannel(notificationChannel)
        startForeground(NOTIFICATION_ID, notification)

        if (intent.action == ACTION_STOP_SERVICE) {
            stopForeground(true)
            stopSelf()
        }

        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    companion object {
        const val CHANNEL_ID_SOUND = "sound_channel"
        const val CHANNEL_ID_SILENT = "silent_channel"
        const val ACTION_STOP_SERVICE = "stop service"
        const val SOUND_FLAG = "sound"
        const val NOTIFICATION_TITLE = "title"
        const val NOTIFICATION_RINGTONE = "ringtone"
        const val NOTIFICATION_VIBRATION = "vibration"
        const val NOTIFICATION_TEXT = "text"
        const val NOTIFICATION_ID = 4321
        const val MINIMIZE_AT_START = "minimize_at_start"
    }
}