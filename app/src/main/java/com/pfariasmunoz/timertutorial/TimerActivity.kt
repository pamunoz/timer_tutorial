package com.pfariasmunoz.timertutorial

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.pfariasmunoz.timertutorial.util.AlarmUtil
import com.pfariasmunoz.timertutorial.util.NotificationUtil
import com.pfariasmunoz.timertutorial.util.PrefUtil
import kotlinx.android.synthetic.main.activity_timer.*
import kotlinx.android.synthetic.main.content_timer.*
import java.util.*

class TimerActivity : AppCompatActivity() {

    enum class TimerState{
        STOPPED, PAUSED, RUNNING
    }

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds = 0L
    private var timerState = TimerState.STOPPED

    private var secondsRemaining = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        setSupportActionBar(toolbar)
        supportActionBar?.setIcon(R.drawable.ic_timer)
        supportActionBar?.title = "      Timer"

        // add funtionality to the fabButtons
        fab_start.setOnClickListener {
            startTimer()
            timerState = TimerState.RUNNING
            updateButtons()
        }

        fab_pause.setOnClickListener {
            timer.cancel()
            timerState = TimerState.PAUSED
            updateButtons()
        }

        fab_stop.setOnClickListener {
            timer.cancel()
            onTimerFinished()
        }

    }

    override fun onResume() {
        super.onResume()

        initTimer()
        AlarmUtil.removeAlarm(this)
        NotificationUtil.hideTimerNotification(this)
    }

    override fun onPause() {
        super.onPause()

        if (timerState == TimerState.RUNNING) {
            timer.cancel()
            val wakeUpTime = AlarmUtil.setAlarm(this, AlarmUtil.nowSeconds, secondsRemaining)
            // show notification
            NotificationUtil.showTimerRunning(this, wakeUpTime)
        }
        else if (timerState == TimerState.PAUSED) {
            // show notification
            NotificationUtil.showTimerPaused(this)
        }

        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, this)
        PrefUtil.setSecondsRemaining(secondsRemaining, this)
        PrefUtil.setTimerState(timerState, this)
    }

    private fun initTimer() {
        timerState = PrefUtil.getTimerState(this)
        if (timerState == TimerState.STOPPED) {
            setNewTimerLength()
        } else {
            setPreviousTimerLength()
        }
        secondsRemaining = if (timerState == TimerState.RUNNING || timerState == TimerState.PAUSED) {
            PrefUtil.getSecondsRemaining(this)
        } else {
            timerLengthSeconds
        }

        val alarmSetTime = PrefUtil.getAlarmSetTime(this)
        if (alarmSetTime > 0) {
            secondsRemaining -= AlarmUtil.nowSeconds - alarmSetTime
        } else if (timerState == TimerState.RUNNING) {
            startTimer()
        }

        if (timerState == TimerState.RUNNING) startTimer()
        updateButtons()
        updateCountdownUI()

    }

    private fun onTimerFinished() {
        timerState = TimerState.STOPPED
        // We set the length of the timer to be the one set on the settings activity
        // if the length was changed when the timer was running
        setNewTimerLength()

        progress_countdown.progress = 0
        PrefUtil.setSecondsRemaining(timerLengthSeconds, this)
        secondsRemaining = timerLengthSeconds
        updateButtons()
        updateCountdownUI()
    }

    private fun startTimer() {
        timerState = TimerState.RUNNING
        timer= object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()
            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun setNewTimerLength() {
        val lengthInMinutes = PrefUtil.getTimerLength(this)
        timerLengthSeconds = (lengthInMinutes * 60L)
        progress_countdown.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength() {
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(this)
        progress_countdown.max = timerLengthSeconds.toInt()
    }

    private fun updateCountdownUI() {
        // this values are for the textview that we need to update
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinutesUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val sedondsStr = secondsInMinutesUntilFinished.toString()
        textView_countdown.text = "$minutesUntilFinished:${
            if(sedondsStr.length == 2) sedondsStr else "0" + sedondsStr
        }"
        progress_countdown.progress = (timerLengthSeconds - secondsRemaining).toInt()
    }

    private fun updateButtons() {
        when(timerState) {
            TimerState.RUNNING -> {
                fab_start.isEnabled = false
                fab_pause.isEnabled = true
                fab_stop.isEnabled = true
            }
            TimerState.STOPPED -> {
                fab_start.isEnabled = true
                fab_pause.isEnabled = false
                fab_stop.isEnabled = false
            }
            TimerState.PAUSED -> {
                fab_start.isEnabled = true
                fab_pause.isEnabled = false
                fab_stop.isEnabled = true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_timer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
