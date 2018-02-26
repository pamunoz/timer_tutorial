package com.pfariasmunoz.timertutorial.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pfariasmunoz.timertutorial.util.AlarmUtil
import com.pfariasmunoz.timertutorial.util.NotificationUtil
import com.pfariasmunoz.timertutorial.util.PrefUtil

class TimerExpiredReceiver : BroadcastReceiver() {



    override fun onReceive(context: Context, intent: Intent) {
        val prefs: PrefUtil = PrefUtil(context)
        val notifications: NotificationUtil = NotificationUtil(context)

        // show notification
        notifications.showTimerExpired()
        prefs.setTimerState(TimerState.STOPPED)
        prefs.setAlarmSetTime(0)
    }
}
