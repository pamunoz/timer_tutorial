package com.pfariasmunoz.timertutorial

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pfariasmunoz.timertutorial.util.NotificationUtil
import com.pfariasmunoz.timertutorial.util.PrefUtil

class TimerExpiredReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // show notification
        NotificationUtil.showTimerExpired(context)
        PrefUtil.setTimerState(TimerState.STOPPED, context)
        PrefUtil.setAlarmSetTime(0, context)
    }
}
