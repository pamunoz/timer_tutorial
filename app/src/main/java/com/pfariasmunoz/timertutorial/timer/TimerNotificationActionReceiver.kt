package com.pfariasmunoz.timertutorial.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pfariasmunoz.timertutorial.di.Injector
import com.pfariasmunoz.timertutorial.util.AlarmUtil
import com.pfariasmunoz.timertutorial.util.NotificationUtil
import com.pfariasmunoz.timertutorial.util.PrefUtil

class TimerNotificationActionReceiver : BroadcastReceiver() {

    val preferences: PrefUtil
    val notifications: NotificationUtil
    val alarm: AlarmUtil

    init {
        this.preferences = Injector.get().prefUtil()
        this.notifications = Injector.get().notificationUtil()
        this.alarm = Injector.get().alarmUtil()
    }

    override fun onReceive(context: Context, intent: Intent) {

//        val prefs: PrefUtil = PrefUtil(context)
//        val alarm: AlarmUtil = AlarmUtil(context, prefs)
//        val notifications: NotificationUtil = NotificationUtil(context)


        when (intent.action) {
            Action.STOP -> {
                alarm.removeAlarm()
                preferences.setTimerState(TimerState.STOPPED)
                notifications.hideTimerNotification()
            }
            Action.PAUSE -> {
                var secondsRemaining = preferences.getSecondsRemaining()
                val alarmSetTime = preferences.getAlarmSetTime()
                val nowSeconds = alarm.nowSeconds

                // This is time in which the timer was running in the background
                secondsRemaining -= nowSeconds - alarmSetTime
                preferences.setSecondsRemaining(secondsRemaining)

                alarm.removeAlarm()
                preferences.setTimerState(TimerState.PAUSED)
                notifications.showTimerPaused()
            }
            Action.RESUME -> {
                val secondsRemaining = preferences.getSecondsRemaining()
                val wakeUpTime = alarm.setAlarm(alarm.nowSeconds, secondsRemaining)
                preferences.setTimerState(TimerState.RUNNING)
                notifications.showTimerRunning(wakeUpTime)
            }
            Action.START -> {
                val minutesRemaining = preferences.getTimerLength()
                val secondsRemaining = minutesRemaining * 60L
                val wakeUpTime = alarm.setAlarm(alarm.nowSeconds, secondsRemaining)
                preferences.setTimerState(TimerState.RUNNING)
                // We need this because the remaining seconds are the full length of the timer
                preferences.setSecondsRemaining(secondsRemaining)
                notifications.showTimerRunning(wakeUpTime)
            }
        }
    }
}
