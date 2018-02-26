package com.pfariasmunoz.timertutorial.timer

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.pfariasmunoz.timertutorial.R
import com.pfariasmunoz.timertutorial.settings.SettingsActivity
import com.pfariasmunoz.timertutorial.util.AlarmUtil
import com.pfariasmunoz.timertutorial.util.NotificationUtil
import com.pfariasmunoz.timertutorial.util.PrefUtil
import kotlinx.android.synthetic.main.activity_timer.*
import kotlinx.android.synthetic.main.content_timer.*

class TimerActivity : AppCompatActivity(), TimerContract.View {

    override lateinit var presenter: TimerContract.Presenter

    init {
        presenter = TimerPresenter()
    }

    private lateinit var countDownTimer: CountDownTimer
    private var timerLengthSeconds = 0L
    private var timerState = TimerState.STOPPED
    private var secondsRemaining = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        setSupportActionBar(toolbar)
        supportActionBar?.setIcon(R.drawable.ic_timer)
        supportActionBar?.title = "      Timer"

        // add functionality to the fabButtons
        fab_start.setOnClickListener {
            // Presenter
            startTimer()
            timerState = TimerState.RUNNING
            // UI
            updateButtons()
        }

        fab_pause.setOnClickListener {
            // Presenter
            countDownTimer.cancel()
            timerState = TimerState.PAUSED
            // UI
            updateButtons()
        }

        fab_stop.setOnClickListener {
            // Presenter
            if (countDownTimer != null) countDownTimer.cancel()
            onTimerFinished()
        }
    }

    override fun onResume() {
        super.onResume()
        // Presenter
        initTimer()
        AlarmUtil.removeAlarm(this)
        NotificationUtil.hideTimerNotification(this)
    }

    override fun onPause() {
        super.onPause()
        // Presenter
        if (timerState == TimerState.RUNNING) {
            countDownTimer.cancel()
            val wakeUpTime = AlarmUtil.setAlarm(this, AlarmUtil.nowSeconds, secondsRemaining)
            // show notification
            NotificationUtil.showTimerRunning(this, wakeUpTime)
        } else if (timerState == TimerState.PAUSED) {
            // show notification
            NotificationUtil.showTimerPaused(this)
        }
        // Presenter
        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, this)
        PrefUtil.setSecondsRemaining(secondsRemaining, this)
        PrefUtil.setTimerState(timerState, this)
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
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Timer methods
    private fun initTimer() {
        // Presenter
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

        // UI
        updateButtons()
        updateCountdownUI()

    }

    private fun startTimer() {
        // Presenter
        timerState = TimerState.RUNNING
        countDownTimer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()
            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                // UI
                updateCountdownUI()
            }
        }.start()
    }

    private fun onTimerFinished() {
        // Presenter
        timerState = TimerState.STOPPED
        // We set the length of the countDownTimer to be the one set on the settings activity
        // if the length was changed when the countDownTimer was running
        setNewTimerLength()

        progress_countdown.progress = 0
        PrefUtil.setSecondsRemaining(timerLengthSeconds, this)
        secondsRemaining = timerLengthSeconds
        // UI
        updateButtons()
        updateCountdownUI()
    }

    private fun setNewTimerLength() {
        // Presenter
        val lengthInMinutes = PrefUtil.getTimerLength(this)
        timerLengthSeconds = (lengthInMinutes * 60L)
        // UI
        progress_countdown.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength() {
        // Presenter
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(this)
        // UI
        progress_countdown.max = timerLengthSeconds.toInt()
    }

    private fun updateCountdownUI() {
        // Presenter
        // this values are for the textview that we need to update
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinutesUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val sedondsStr = secondsInMinutesUntilFinished.toString()
        textView_countdown.text = "$minutesUntilFinished:${
            if(sedondsStr.length == 2) sedondsStr else "0" + sedondsStr
        }"
        // UI

        progress_countdown.progress = presenter.progress
    }

    override fun updateButtons() {
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
}
