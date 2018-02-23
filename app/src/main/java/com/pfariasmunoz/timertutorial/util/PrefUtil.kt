package com.pfariasmunoz.timertutorial.util

import android.content.Context
import com.pfariasmunoz.timertutorial.TimerState
import com.pfariasmunoz.timertutorial.extensions.defaultSharedPreferences
import com.pfariasmunoz.timertutorial.extensions.put


object PrefUtil {
    // Get the timer in Minutes
    fun getTimerLength(context: Context): Int {
        // placeholder function
        return 1
    }

    // This is the ID we use in the preferences to identify the values
    private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "com.pfariasmunoz.timer.previous_timer_length"
    // Get the timer in Seconds
    // it remember the timer of the previous timer length, because we want to change a
    // new timer, not the currently running
    fun getPreviousTimerLengthSeconds(context: Context): Long {
        return context.defaultSharedPreferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
    }

    // Set the previous timer
    fun setPreviousTimerLengthSeconds(seconds: Long, context: Context) {
        context.defaultSharedPreferences.put(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
    }

    // Kepp track of timer state
    private const val TIMER_STATE_ID = "com.pfariasmunoz.timer.timer_state"

    fun getTimerState(context: Context): TimerState {
        val ordinal = context.defaultSharedPreferences.getInt(TIMER_STATE_ID, 0)
        return TimerState.values()[ordinal]
    }

    fun setTimerState(state: TimerState, context: Context) {
        context.defaultSharedPreferences.put(TIMER_STATE_ID, state.ordinal)
    }


    // Keep track of seconds remaining
    // This is the ID we use in the preferences to identify the values
    private const val SECONDS_REMAINING_ID= "com.pfariasmunoz.timer.seconds_remaining"
    // Get the timer in Seconds
    // it remember the timer of the previous timer length, because we want to change a
    // new timer, not the currently running
    fun getSecondsRemaining(context: Context): Long {
        return context.defaultSharedPreferences.getLong(SECONDS_REMAINING_ID, 0)
    }

    // Set the previous timer
    fun setSecondsRemaining(seconds: Long, context: Context) {
        context.defaultSharedPreferences.put(SECONDS_REMAINING_ID, seconds)
    }

    private const val ALARM_SET_TIME_ID = "com.pfariasmunoz.timer.backgrounded_time"

    fun getAlarmSetTime(context: Context): Long {
        return context.defaultSharedPreferences.getLong(ALARM_SET_TIME_ID, 0)
    }

    fun setAlarmSetTime(time: Long, context: Context) {
        context.defaultSharedPreferences.put(ALARM_SET_TIME_ID, time)
    }
}