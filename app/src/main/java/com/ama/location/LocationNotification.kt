package com.ama.location

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.ama.MainActivity
import com.ama.R

class LocationNotification(private val applicationContext: Context) {

    private val notificationManager by lazy {
        NotificationManagerCompat.from(applicationContext)
    }

    fun create() = applicationContext.run {
        notificationManager.createNotificationChannel()
        val pendingIntent = getOnlineComponentsActivityPendingIntent()
        createNotification(pendingIntent)
    }

    private fun getOnlineComponentsActivityPendingIntent(): PendingIntent {
        return NavDeepLinkBuilder(applicationContext)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.fr_location)
            .createPendingIntent()
    }

    private fun NotificationManagerCompat.createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            createNotificationChannel(notificationChannel)
        }
    }

    private fun createNotification(pendingIntent: PendingIntent): Notification {
        return NotificationCompat
            .Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(applicationContext.getString(R.string.location_service_title))
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_location_on_black_24dp)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setShowWhen(false)
            .setAutoCancel(false)
            .build()
    }

    private companion object {
        const val NOTIFICATION_CHANNEL_ID =
            "LOCATION_NOTIFICATION_CHANNEL_ID"
        const val NOTIFICATION_CHANNEL_NAME =
            "LOCATION_NOTIFICATION_CHANNEL_NAME"
    }
}