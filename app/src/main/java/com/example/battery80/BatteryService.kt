package com.example.battery80

import android.app.*
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat.stopForeground

class BatteryService: Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()

        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addNextIntentWithParentStack(launchIntent)


        val pendingIntent: PendingIntent = stackBuilder.getPendingIntent(
            0,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val title: String? = intent?.getStringExtra("title")
        val text: String? = intent?.getStringExtra("text")

        val notification: Notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("$title - $text")
            .setSmallIcon(R.drawable.ic_battery_full)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(122, notification)
        // on receiving stop service action intent
        // from MainActivity onDestroy method
        if (intent?.action == ACTION_STOP_SERVICE)
        {
            stopForeground(true)
            stopSelf()
            Log.v("_TEST", "stop service")
        }
        return START_NOT_STICKY
    }

    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(CHANNEL_ID, "name",
        NotificationManager.IMPORTANCE_LOW)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    companion object {
        const val CHANNEL_ID = "channel id"
        const val ACTION_STOP_SERVICE = "stop service"
    }
}