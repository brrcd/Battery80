package com.example.battery80

import android.app.*
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.IBinder

class BatteryService : Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (intent.action == ACTION_STOP_SERVICE) {
            stopForeground(true)
            stopSelf()
        }

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

            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            notificationChannel = NotificationChannel(
                CHANNEL_ID_SOUND, "channel with sound",
                NotificationManager.IMPORTANCE_HIGH
            )
                .also { it.setSound(uri, audioAttributes) }
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
        const val NOTIFICATION_TEXT = "text"
        const val NOTIFICATION_ID = 4321
    }
}