package com.pfariasmunoz.timertutorial.util

import android.content.Context
import android.preference.PreferenceManager
import com.pfariasmunoz.timertutorial.TimerActivity


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
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
    }

    // Set the previous timer
    fun setPreviousTimerLengthSeconds(seconds: Long, context: Context) {
        val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
        editor.apply()
    }

    // Kepp track of timer state
    private const val TIMER_STATE_ID = "com.pfariasmunoz.timer.timer_state"

    fun getTimerState(context: Context): TimerActivity.TimerState {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val ordinal = preferences.getInt(TIMER_STATE_ID, 0)
        return TimerActivity.TimerState.values()[ordinal]
    }

    fun setTimerState(state: TimerActivity.TimerState, context: Context) {
        val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        val ordinal = state.ordinal
        editor.putInt(TIMER_STATE_ID, ordinal)
        editor.apply()
    }


    // Keep track of seconds remaining
    // This is the ID we use in the preferences to identify the values
    private const val SECONDS_REMAINING_ID= "com.pfariasmunoz.timer.seconds_remaining"
    // Get the timer in Seconds
    // it remember the timer of the previous timer length, because we want to change a
    // new timer, not the currently running
    fun getSecondsRemaining(context: Context): Long {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getLong(SECONDS_REMAINING_ID, 0)
    }

    // Set the previous timer
    fun setSecondsRemaining(seconds: Long, context: Context) {
        val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        editor.putLong(SECONDS_REMAINING_ID, seconds)
        editor.apply()
    }

    private const val ALARM_SET_TIME_ID = "com.pfariasmunoz.timer.backgrounded_time"

    fun getAlarmSetTime(context: Context): Long {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getLong(ALARM_SET_TIME_ID, 0)
    }

    fun setAlarmSetTime(time: Long, context: Context) {
        val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        editor.putLong(ALARM_SET_TIME_ID, time)
        editor.apply()
    }
}