package com.todokanai.composepractice.tools

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.todokanai.composepractice.R
import com.todokanai.composepractice.application.MyApplication
import com.todokanai.composepractice.myobjects.Constants.CHANNEL_ID

/**
 *  이 클래스의 method는 독립적으로 사용 가능함
 *
 */

class MyNotification() {
    val channelId = CHANNEL_ID
    val context = MyApplication.appContext
    val icon = R.drawable.ic_launcher_background
    fun createNotification(title:String,message:String) {

        // Notification 채널 생성
        val channel = NotificationChannel(channelId, "My Channel", NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = "This is my notification channel"
        }
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val isNotiEnabled = notificationManager.areNotificationsEnabled()
        if(isNotiEnabled) {
            // Notification 채널 등록
            notificationManager.createNotificationChannel(channel)

            // NotificationCompat.Builder를 사용하여 Notification 생성
            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            // Notification 표시
            with(NotificationManagerCompat.from(context)) {

                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                notify(0, builder.build())
            }
        }
    }

    fun createOngoingNotification(title:String,message:String) {

        // Notification 채널 생성
        val channel = NotificationChannel(channelId, "My Channel", NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = "This is my notification channel"
        }
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val isNotiEnabled = notificationManager.areNotificationsEnabled()
        if(isNotiEnabled) {
            // Notification 채널 등록
            notificationManager.createNotificationChannel(channel)

            // NotificationCompat.Builder를 사용하여 Notification 생성
            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setOngoing(true)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            // Notification 표시
            with(NotificationManagerCompat.from(context)) {

                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                notify(0, builder.build())
            }
        }
    }

 fun deleteProgressNoti(progress:Int,total:Int) {
        val channel = NotificationChannel(channelId, "My Channel", NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = "This is my notification channel"
        }
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(icon)
            .setContentTitle("Deleting")
            .setContentText("$progress / $total")
            .setOngoing(true)
            .setProgress(100,100*progress/total,false)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notify(0, builder.build())
                return
            }
            notify(0, builder.build())
        }
    }

    fun copyProgressNoti(progress:Int) = progressNoti("Copying","$progress %",progress)
    fun moveProgressNoti(progress:Int) = progressNoti("Moving","$progress %",progress)
    fun unzipProgressNoti(progress: Int) = progressNoti("Unzipping","$progress %",progress)
    fun zipProgressNoti(progress: Int) = progressNoti("Zipping","$progress %",progress)
    fun progressNoti(title: String,message: String,progress:Int){
        fun ongoing():Boolean{
            return progress<100
        }
        val channel = NotificationChannel(
            channelId,
            "My Channel",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "This is my notification channel"
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setOngoing(ongoing())
            .setContentText(message)
            .setProgress(100, progress, false)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notify(0, builder.build())
            }
            notify(0, builder.build())
        }
    }

    fun completedNotification(title: String,message: String){
        val channel = NotificationChannel(
            channelId,
            "My Channel",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "This is my notification channel"
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setOngoing(false)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notify(0, builder.build())
            }
            notify(0, builder.build())
        }
    }

}