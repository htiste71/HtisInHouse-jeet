package com.htistelecom.htisinhouse.utilities

import android.R.id
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.activity.MainActivityNavigation


class NotificationChannelClass {
    lateinit var context: Context

    constructor(ctx: Context) {
        context = ctx
    }




    fun showNotification(msg: String) {
        Log.e("Notification called","Notification")
        val CHANNEL_ID = "my_channel_01"
        val name: CharSequence = "my_channel"
        val Description = "This is my channel"

        val NOTIFICATION_ID = 234

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = Description
            mChannel.enableLights(true)
            mChannel.lightColor = Color.RED
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            mChannel.setShowBadge(true)
            notificationManager?.createNotificationChannel(mChannel)
        }


        val resultIntent = Intent(context, MainActivityNavigation::class.java)
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(MainActivityNavigation::class.java)
        stackBuilder.addNextIntent(resultIntent)
        val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)


        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("WFMS")
               // .setContentText("SUB-TITLE")
                .setStyle(NotificationCompat.BigTextStyle().bigText(msg))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setColor(context.getResources().getColor(android.R.color.holo_red_dark))



        notificationManager?.notify(NOTIFICATION_ID, builder.build())
      //notificationManager.activeNotifications
    }






}