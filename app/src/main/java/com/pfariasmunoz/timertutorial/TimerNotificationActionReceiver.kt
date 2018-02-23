package com.pfariasmunoz.timertutorial

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pfariasmunoz.timertutorial.util.AlarmUtil
import com.pfariasmunoz.timertutorial.util.NotificationUtil
import com.pfariasmunoz.timertutorial.util.PrefUtil

class TimerNotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            AppConstants.ACTION_STOP -> {
                AlarmUtil.removeAlarm(context)
                PrefUtil.setTimerState(TimerActivity.TimerState.STOPPED, context)
                NotificationUtil.hideTimerNotification(context)
            }
            AppConstants.ACTION_PAUSE -> {
                var secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val alarmSetTime = PrefUtil.getAlarmSetTime(context)
                val nowSeconds = AlarmUtil.nowSeconds

                // This is time in which the timer was running in the background
                secondsRemaining -= nowSeconds - alarmSetTime
                PrefUtil.setSecondsRemaining(secondsRemaining, context)

                AlarmUtil.removeAlarm(context)
                PrefUtil.setTimerState(TimerActivity.TimerState.PAUSED, context)
                NotificationUtil.showTimerPaused(context)
            }
            AppConstants.ACTION_RESUME -> {
                val secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val wakeUpTime = AlarmUtil.setAlarm(context, AlarmUtil.nowSeconds, secondsRemaining)
                PrefUtil.setTimerState(TimerActivity.TimerState.RUNNING, context)
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
            AppConstants.ACTION_START -> {
                val minutesRemaining = PrefUtil.getTimerLength(context)
                val secondsRemaining = minutesRemaining * 60L
                val wakeUpTime = AlarmUtil.setAlarm(context, AlarmUtil.nowSeconds, secondsRemaining)
                PrefUtil.setTimerState(TimerActivity.TimerState.RUNNING, context)
                // We need this because the remaining seconds are the full length of the timer
                PrefUtil.setSecondsRemaining(secondsRemaining, context)
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
        }
    }
}
