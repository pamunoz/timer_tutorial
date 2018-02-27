package com.pfariasmunoz.timertutorial.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pfariasmunoz.timertutorial.di.Injector
import com.pfariasmunoz.timertutorial.util.AlarmUtil
import com.pfariasmunoz.timertutorial.util.NotificationUtil
import com.pfariasmunoz.timertutorial.util.PrefUtil

class TimerExpiredReceiver : BroadcastReceiver() {

    private val preferences: PrefUtil
    private val notifications: NotificationUtil

    init {
        this.preferences = Injector.get().prefUtil()
        this.notifications = Injector.get().notificationUtil()
    }

    override fun onReceive(context: Context, intent: Intent) {
//        val prefs: PrefUtil = PrefUtil(context)
//        val notifications: NotificationUtil = NotificationUtil(context)

        // show notification
        notifications.showTimerExpired()
        preferences.setTimerState(TimerState.STOPPED)
        preferences.setAlarmSetTime(0)
    }
}
