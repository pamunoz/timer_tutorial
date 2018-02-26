package com.pfariasmunoz.timertutorial.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pfariasmunoz.timertutorial.util.AlarmUtil
import com.pfariasmunoz.timertutorial.util.NotificationUtil
import com.pfariasmunoz.timertutorial.util.PrefUtil

class TimerNotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val prefs: PrefUtil = PrefUtil(context)
        val alarm: AlarmUtil = AlarmUtil(context, prefs)
        val notifications: NotificationUtil = NotificationUtil(context)


        when (intent.action) {
            Action.STOP -> {
                alarm.removeAlarm()
                prefs.setTimerState(TimerState.STOPPED)
                notifications.hideTimerNotification()
            }
            Action.PAUSE -> {
                var secondsRemaining = prefs.getSecondsRemaining()
                val alarmSetTime = prefs.getAlarmSetTime()
                val nowSeconds = alarm.nowSeconds

                // This is time in which the timer was running in the background
                secondsRemaining -= nowSeconds - alarmSetTime
                prefs.setSecondsRemaining(secondsRemaining)

                alarm.removeAlarm()
                prefs.setTimerState(TimerState.PAUSED)
                notifications.showTimerPaused()
            }
            Action.RESUME -> {
                val secondsRemaining = prefs.getSecondsRemaining()
                val wakeUpTime = alarm.setAlarm(alarm.nowSeconds, secondsRemaining)
                prefs.setTimerState(TimerState.RUNNING)
                notifications.showTimerRunning(wakeUpTime)
            }
            Action.START -> {
                val minutesRemaining = prefs.getTimerLength()
                val secondsRemaining = minutesRemaining * 60L
                val wakeUpTime = alarm.setAlarm(alarm.nowSeconds, secondsRemaining)
                prefs.setTimerState(TimerState.RUNNING)
                // We need this because the remaining seconds are the full length of the timer
                prefs.setSecondsRemaining(secondsRemaining)
                notifications.showTimerRunning(wakeUpTime)
            }
        }
    }
}
