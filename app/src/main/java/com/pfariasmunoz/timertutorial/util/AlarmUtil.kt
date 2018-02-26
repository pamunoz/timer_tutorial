package com.pfariasmunoz.timertutorial.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.pfariasmunoz.timertutorial.timer.TimerExpiredReceiver
import com.pfariasmunoz.timertutorial.extensions.alarmManager
import java.util.*

class AlarmUtil(val context: Context, val pref: PrefUtil) {
    fun setAlarm(nowSeconds: Long, secondsRemaining: Long): Long {
        val wakeUpTime = (nowSeconds + secondsRemaining) * 1000
        val alarmManager = context.alarmManager
        val intent = Intent(context, TimerExpiredReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
        pref.setAlarmSetTime(nowSeconds)
        return wakeUpTime
    }

    fun removeAlarm() {
        val intent = Intent(context, TimerExpiredReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager = context.alarmManager
        alarmManager.cancel(pendingIntent)
        pref.setAlarmSetTime(0)
    }
    val nowSeconds: Long
        get() = Calendar.getInstance().timeInMillis / 1000
}