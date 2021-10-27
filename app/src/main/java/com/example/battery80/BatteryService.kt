package com.example.battery80

import android.app.*
import android.content.Intent
import android.os.IBinder
import android.util.Log

class BatteryService : Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val bool = intent.getBooleanExtra(SOUND_FLAG, false)
        createNotificationChannel(intent.getBooleanExtra(SOUND_FLAG, false))
        //TODO remove
        Log.v("_TEST", "sound - $bool")

        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addNextIntentWithParentStack(launchIntent)


        val pendingIntent: PendingIntent = stackBuilder.getPendingIntent(
            0,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val title: String? = intent.getStringExtra(NOTIFICATION_TITLE)
        val text: String? = intent.getStringExtra(NOTIFICATION_TEXT)

        val notification: Notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("$title - $text")
            .setSmallIcon(R.drawable.ic_battery_full)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(NOTIFICATION_ID, notification)
        // on receiving stop service action intent
        // from MainActivity onDestroy method
        if (intent.action == ACTION_STOP_SERVICE) {
            stopForeground(true)
            stopSelf()
            //TODO remove
            Log.v("_TEST", "stop service")
        }
        return START_NOT_STICKY
    }

    private fun createNotificationChannel(isSoundOn: Boolean) {
        val notificationChannel = if (isSoundOn) {
            NotificationChannel(
                CHANNEL_ID, "name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
        } else {
            NotificationChannel(
                CHANNEL_ID, "name",
                NotificationManager.IMPORTANCE_LOW
            )
        }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    companion object {
        const val CHANNEL_ID = "channel id"
        const val ACTION_STOP_SERVICE = "stop service"
        const val SOUND_FLAG = "sound"
        const val NOTIFICATION_TITLE = "title"
        const val NOTIFICATION_TEXT = "text"
        const val NOTIFICATION_ID = 4321
    }
}