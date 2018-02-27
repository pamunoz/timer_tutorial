package com.pfariasmunoz.timertutorial.util

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.pfariasmunoz.timertutorial.*
import com.pfariasmunoz.timertutorial.extensions.notificationManager
import com.pfariasmunoz.timertutorial.timer.Action
import com.pfariasmunoz.timertutorial.timer.TimerActivity
import com.pfariasmunoz.timertutorial.timer.TimerNotificationActionReceiver
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationUtil @Inject constructor(val context: Context) {

    companion object {
        private const val CHANNEL_ID_TIMER = "menu_timer"
        private const val CHANNEL_NAME_TIMER = "Timer App Timer"
        private const val TIMER_ID = 0
    }


    fun showTimerExpired() {
        // We want to be able to control the timer from notifications
        val startIntent = Intent(context, TimerNotificationActionReceiver::class.java)
        // specify actions for the intent
        startIntent.action = Action.START
        val startPendingIntent = PendingIntent.getBroadcast(context, 0,
                startIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        // Create the actual notifications
        val nBuilder = getBasicNotificationBuilder(CHANNEL_ID_TIMER, true)
        nBuilder.setContentTitle("Timer Expired")
                .setContentText("Start again?")
                .setContentIntent(getPendingIntentWithStack(TimerActivity::class.java))
                .addAction(R.drawable.ic_play, "Start", startPendingIntent)
        val nManager = context.notificationManager
        nManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER, true)
        // Now create the notification
        nManager.notify(TIMER_ID, nBuilder.build())
    }

    fun showTimerRunning(wakeUpTime: Long) {
        // stop intent
        val stopIntent = Intent(context, TimerNotificationActionReceiver::class.java)
        stopIntent.action = Action.STOP
        val stopPendingIntent = PendingIntent.getBroadcast(context, 0,
                stopIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        // stop intent
        val pauseIntent = Intent(context, TimerNotificationActionReceiver::class.java)
        pauseIntent.action = Action.STOP
        val pausePendingIntent = PendingIntent.getBroadcast(context, 0,
                pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val dateFormat = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)


        // Create the actual notifications
        val nBuilder = getBasicNotificationBuilder(CHANNEL_ID_TIMER, true)
        nBuilder.setContentTitle("Timer is Running.")
                .setContentText("End: ${dateFormat.format(Date(wakeUpTime))}")
                .setContentIntent(getPendingIntentWithStack(TimerActivity::class.java))
                .setOngoing(true) // the user cannot dismiss the notification manually
                .addAction(R.drawable.ic_stop, "Stop", stopPendingIntent)
                .addAction(R.drawable.ic_pause, "Pause", pausePendingIntent)
        val nManager = context.notificationManager
        nManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER, true)
        // Now create the notification
        nManager.notify(TIMER_ID, nBuilder.build())
    }

    fun showTimerPaused() {
        // We want to be able to control the timer from notifications
        val resumeIntent = Intent(context, TimerNotificationActionReceiver::class.java)
        // specify actions for the intent
        resumeIntent.action = Action.RESUME
        val resumePendingIntent = PendingIntent.getBroadcast(context, 0,
                resumeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        // Create the actual notifications
        val nBuilder = getBasicNotificationBuilder(CHANNEL_ID_TIMER, true)
        nBuilder.setContentTitle("Timer is paused.")
                .setContentText("Resume?")
                .setContentIntent(getPendingIntentWithStack(TimerActivity::class.java))
                .setOngoing(true)
                .addAction(R.drawable.ic_play, "Resume", resumePendingIntent)
        val nManager = context.notificationManager
        nManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER, true)
        // Now create the notification
        nManager.notify(TIMER_ID, nBuilder.build())
    }

    fun hideTimerNotification() {
        val nManager = context.notificationManager
        nManager.cancel(TIMER_ID)
    }

    private fun getBasicNotificationBuilder(channelId: String, playSound: Boolean)
            : NotificationCompat.Builder {
        val notificationSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val nBuilder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_timer)
                .setAutoCancel(true)
                .setDefaults(0)
        if (playSound) nBuilder.setSound(notificationSound)
        return nBuilder
    }

    private fun <T> getPendingIntentWithStack(javaClass: Class<T>): PendingIntent {
        val resultIntent = Intent(context, javaClass)
        // This will make that if the activity is already open, is not going to be created again
        resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        return TaskStackBuilder
                .create(context)
                .addParentStack(javaClass)
                .addNextIntent(resultIntent)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    @TargetApi(26)
    private fun NotificationManager.createNotificationChannel(channelId: String,
                                                              channelName: String,
                                                              playSound: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Here we control if we can play sound in oreo or later
            val channelImportance = if (playSound) {
                NotificationManager.IMPORTANCE_DEFAULT
            } else {
                NotificationManager.IMPORTANCE_LOW
            }
            val nChannel = NotificationChannel(channelId, channelName, channelImportance)
            nChannel.enableLights(true)
            nChannel.lightColor = Color.BLUE
            this.createNotificationChannel(nChannel)
        }
    }

}