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
        presenter = TimerPresenter(this)
    }

    // To Presenter
    private lateinit var countDownTimer: CountDownTimer
    private var timerLengthSeconds = 0L
    private var timerState = TimerState.STOPPED
    private var secondsRemaining = 0L

    private val prefs: PrefUtil = PrefUtil(this)
    private val alarm: AlarmUtil = AlarmUtil(this, prefs)
    private val notifications: NotificationUtil = NotificationUtil(this)


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
        alarm.removeAlarm()
        notifications.hideTimerNotification()
    }

    override fun onPause() {
        super.onPause()
        // Presenter
        if (timerState == TimerState.RUNNING) {
            countDownTimer.cancel()
            val wakeUpTime = alarm.setAlarm(alarm.nowSeconds, secondsRemaining)
            // show notification
            notifications.showTimerRunning(wakeUpTime)
        } else if (timerState == TimerState.PAUSED) {
            // show notification
            notifications.showTimerPaused()
        }
        // Presenter
        prefs.setPreviousTimerLengthSeconds(timerLengthSeconds)
        prefs.setSecondsRemaining(secondsRemaining)
        prefs.setTimerState(timerState)
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
        timerState = prefs.getTimerState()
        if (timerState == TimerState.STOPPED) {
            setNewTimerLength()
        } else {
            setPreviousTimerLength()
        }
        secondsRemaining = if (timerState == TimerState.RUNNING || timerState == TimerState.PAUSED) {
            prefs.getSecondsRemaining()
        } else {
            timerLengthSeconds
        }

        val alarmSetTime = prefs.getAlarmSetTime()
        if (alarmSetTime > 0) {
            secondsRemaining -= alarm.nowSeconds - alarmSetTime
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
        prefs.setSecondsRemaining(timerLengthSeconds)
        secondsRemaining = timerLengthSeconds
        // UI
        updateButtons()
        updateCountdownUI()
    }

    private fun setNewTimerLength() {
        // Presenter
        val lengthInMinutes = prefs.getTimerLength()
        timerLengthSeconds = (lengthInMinutes * 60L)
        // UI
        progress_countdown.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength() {
        // Presenter
        timerLengthSeconds = prefs.getPreviousTimerLengthSeconds()
        // UI
        progress_countdown.max = timerLengthSeconds.toInt()
    }

    private fun updateCountdownUI() {
        textView_countdown.text = presenter.timerText
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
