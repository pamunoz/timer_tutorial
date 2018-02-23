package com.pfariasmunoz.timertutorial.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.pfariasmunoz.timertutorial.timer.TimerExpiredReceiver
import com.pfariasmunoz.timertutorial.extensions.alarmManager
import java.util.*

object AlarmUtil {
    fun setAlarm(context: Context, nowSeconds: Long, secondsRemaining: Long): Long {
        val wakeUpTime = (nowSeconds + secondsRemaining) * 1000
        val alarmManager = context.alarmManager
        val intent = Intent(context, TimerExpiredReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
        PrefUtil.setAlarmSetTime(nowSeconds, context)
        return wakeUpTime
    }

    fun removeAlarm(context: Context) {
        val intent = Intent(context, TimerExpiredReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager = context.alarmManager
        alarmManager.cancel(pendingIntent)
        PrefUtil.setAlarmSetTime(0, context)
    }
    val nowSeconds: Long
        get() = Calendar.getInstance().timeInMillis / 1000
}