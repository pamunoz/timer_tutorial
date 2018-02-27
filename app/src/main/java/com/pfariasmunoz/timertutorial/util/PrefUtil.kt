package com.pfariasmunoz.timertutorial.util

import android.content.Context
import com.pfariasmunoz.timertutorial.timer.TimerState
import com.pfariasmunoz.timertutorial.extensions.defaultSharedPreferences
import com.pfariasmunoz.timertutorial.extensions.put
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefUtil @Inject constructor(val context: Context) {

    companion object {
        private const val TIMER_LENGHT_ID = "com.pfariasmunoz.timertutorial.timer.timer_length"
        // This is the ID we use in the preferences to identify the values
        private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "com.pfariasmunoz.timer.previous_timer_length"
        // Kepp track of timer state
        private const val TIMER_STATE_ID = "com.pfariasmunoz.timer.timer_state"
        // Keep track of seconds remaining
        // This is the ID we use in the preferences to identify the values
        private const val SECONDS_REMAINING_ID= "com.pfariasmunoz.timer.seconds_remaining"
        private const val ALARM_SET_TIME_ID = "com.pfariasmunoz.timer.backgrounded_time"
    }


    // Get the timer in Minutes
    fun getTimerLength(): Int {
        return context.defaultSharedPreferences.getInt(TIMER_LENGHT_ID, 10)
    }


    // Get the timer in Seconds
    // it remember the timer of the previous timer length, because we want to change a
    // new timer, not the currently running
    fun getPreviousTimerLengthSeconds(): Long {
        return context.defaultSharedPreferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
    }

    // Set the previous timer
    fun setPreviousTimerLengthSeconds(seconds: Long) {
        context.defaultSharedPreferences.put(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
    }

    fun getTimerState(): TimerState {
        val ordinal = context.defaultSharedPreferences.getInt(TIMER_STATE_ID, 0)
        return TimerState.values()[ordinal]
    }

    fun setTimerState(state: TimerState) {
        context.defaultSharedPreferences.put(TIMER_STATE_ID, state.ordinal)
    }
    // Get the timer in Seconds
    // it remember the timer of the previous timer length, because we want to change a
    // new timer, not the currently running
    fun getSecondsRemaining(): Long {
        return context.defaultSharedPreferences.getLong(SECONDS_REMAINING_ID, 0)
    }

    // Set the previous timer
    fun setSecondsRemaining(seconds: Long) {
        context.defaultSharedPreferences.put(SECONDS_REMAINING_ID, seconds)
    }

    fun getAlarmSetTime(): Long {
        return context.defaultSharedPreferences.getLong(ALARM_SET_TIME_ID, 0)
    }

    fun setAlarmSetTime(time: Long) {
        context.defaultSharedPreferences.put(ALARM_SET_TIME_ID, time)
    }
}