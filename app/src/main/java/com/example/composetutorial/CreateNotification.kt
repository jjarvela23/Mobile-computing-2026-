package com.example.composetutorial

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.health.connect.datatypes.units.Temperature
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class CreateNotification(private val context: Context) {

    fun ShowNotification(temp: Int) {
        val aintent = Intent(context, CreateNotification::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 1, aintent, PendingIntent.FLAG_IMMUTABLE)
        val bintent = Intent(context, MyBroadcastReceiver::class.java).apply {
            putExtra("MESSAGE", "thanks for clicking")
        }
        val otherIntent: PendingIntent = PendingIntent.getBroadcast(context, 2, bintent, PendingIntent.FLAG_IMMUTABLE)

        var builder = NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID)
            .setSmallIcon(R.drawable.istockphoto_1467588755_612x612)
            .setContentTitle("It is very hot")
            .setContentText("The temperature is " + temp + "°C")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(R.drawable.istockphoto_1467588755_612x612, "click me", otherIntent)
        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            )
            {
                return@with
            }
        }
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, builder.build())
    }
}