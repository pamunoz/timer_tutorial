package com.pfariasmunoz.timertutorial.util

import android.content.Context
import android.content.res.Resources
import com.pfariasmunoz.timertutorial.R
import com.pfariasmunoz.timertutorial.timer.TimerState
import com.pfariasmunoz.timertutorial.extensions.defaultSharedPreferences
import com.pfariasmunoz.timertutorial.extensions.put
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefUtil @Inject constructor(val context: Context) {

    val res: Resources = context.resources
    private val TIMER_LENGHT_ID = res.getString(R.string.timer_length_id)
    private val LONG_BREAK_ID = res.getString(R.string.long_break_id)
    private val SHORT_BREAK_ID = res.getString(R.string.short_break_id)
    // This is the ID we use in the preferences to identify the values
    private val PREVIOUS_TIMER_LENGTH_SECONDS_ID = res.getString(R.string.previous_timer_length_seconds_id)
    // Kepp track of timer state
    private val TIMER_STATE_ID = res.getString(R.string.timer_state_id)
    // Keep track of seconds remaining
    // This is the ID we use in the preferences to identify the values
    private val SECONDS_REMAINING_ID= res.getString(R.string.seconds_remaining_id)
    private val ALARM_SET_TIME_ID = res.getString(R.string.alarm_set_time_id)


    // Get the timer in Minutes
    fun getTimerLength(): Int {
        return context.defaultSharedPreferences.getInt(TIMER_LENGHT_ID, 25)
    }

    // Get the timer in Minutes
    fun getLongBreak(): Int {
        return context.defaultSharedPreferences.getInt(LONG_BREAK_ID, 20)
    }

    // Get the timer in Minutes
    fun getShortBreak(): Int {
        return context.defaultSharedPreferences.getInt(SHORT_BREAK_ID, 5)
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