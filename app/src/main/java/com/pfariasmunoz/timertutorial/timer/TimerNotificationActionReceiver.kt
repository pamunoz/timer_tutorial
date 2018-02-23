package com.pfariasmunoz.timertutorial.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pfariasmunoz.timertutorial.util.AlarmUtil
import com.pfariasmunoz.timertutorial.util.NotificationUtil
import com.pfariasmunoz.timertutorial.util.PrefUtil

class TimerNotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Action.STOP -> {
                AlarmUtil.removeAlarm(context)
                PrefUtil.setTimerState(TimerState.STOPPED, context)
                NotificationUtil.hideTimerNotification(context)
            }
            Action.PAUSE -> {
                var secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val alarmSetTime = PrefUtil.getAlarmSetTime(context)
                val nowSeconds = AlarmUtil.nowSeconds

                // This is time in which the timer was running in the background
                secondsRemaining -= nowSeconds - alarmSetTime
                PrefUtil.setSecondsRemaining(secondsRemaining, context)

                AlarmUtil.removeAlarm(context)
                PrefUtil.setTimerState(TimerState.PAUSED, context)
                NotificationUtil.showTimerPaused(context)
            }
            Action.RESUME -> {
                val secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val wakeUpTime = AlarmUtil.setAlarm(context, AlarmUtil.nowSeconds, secondsRemaining)
                PrefUtil.setTimerState(TimerState.RUNNING, context)
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
            Action.START -> {
                val minutesRemaining = PrefUtil.getTimerLength(context)
                val secondsRemaining = minutesRemaining * 60L
                val wakeUpTime = AlarmUtil.setAlarm(context, AlarmUtil.nowSeconds, secondsRemaining)
                PrefUtil.setTimerState(TimerState.RUNNING, context)
                // We need this because the remaining seconds are the full length of the timer
                PrefUtil.setSecondsRemaining(secondsRemaining, context)
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
        }
    }
}
