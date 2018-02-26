package com.pfariasmunoz.timertutorial.timer

import android.os.CountDownTimer
import com.pfariasmunoz.timertutorial.R.id.*
import com.pfariasmunoz.timertutorial.util.AlarmUtil
import com.pfariasmunoz.timertutorial.util.NotificationUtil
import com.pfariasmunoz.timertutorial.util.PrefUtil
import kotlinx.android.synthetic.main.content_timer.*

class TimerPresenter(
        val timerView: TimerContract.View,
        val alarmUtil: AlarmUtil,
        val prefUtil: PrefUtil,
        val notificationUtil: NotificationUtil) : TimerContract.Presenter {

    private lateinit var countDownTimer: CountDownTimer
    private var timerLengthSeconds = 0L
    private var timerState = TimerState.STOPPED
    private var secondsRemaining = 0L

    override val progress : Int
        get() = (timerLengthSeconds - secondsRemaining).toInt()

    override val timerText: String
        get() {
            val minutesUntilFinished = secondsRemaining / 60
            val secondsInMinutesUntilFinished = secondsRemaining - minutesUntilFinished * 60
            val sedondsStr = secondsInMinutesUntilFinished.toString()
            return "$minutesUntilFinished:${
                if(sedondsStr.length == 2) sedondsStr else "0" + sedondsStr
            }"
        }

    override fun start() {
        initTimer()
        alarmUtil.removeAlarm()
        notificationUtil.hideTimerNotification()
    }

    override fun startTimer() {
        timerState = TimerState.RUNNING
        timerView.updateButtons(timerState)
        countDownTimer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()
            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                // UI
                timerView.updateCountdownUI()
            }
        }.start()
    }

    private fun onTimerFinished() {
        // Presenter
        timerState = TimerState.STOPPED
        // We set the length of the countDownTimer to be the one set on the settings activity
        // if the length was changed when the countDownTimer was running
        setNewTimerLength()

        timerView.resetProgressCountdown()
        prefUtil.setSecondsRemaining(timerLengthSeconds)
        secondsRemaining = timerLengthSeconds
        // UI
        timerView.updateButtons(timerState)
        timerView.updateCountdownUI()
    }

    private fun setNewTimerLength() {
        // Presenter
        val lengthInMinutes = prefUtil.getTimerLength()
        timerLengthSeconds = (lengthInMinutes * 60L)
        // UI
        timerView.setMaxProgressCountdown(timerLengthSeconds.toInt())
    }

    // Timer methods
    private fun initTimer() {
        // Presenter
        timerState = prefUtil.getTimerState()
        if (timerState == TimerState.STOPPED) {
            setNewTimerLength()
        } else {
            setPreviousTimerLength()
        }
        secondsRemaining = if (timerState == TimerState.RUNNING || timerState == TimerState.PAUSED) {
            prefUtil.getSecondsRemaining()
        } else {
            timerLengthSeconds
        }

        val alarmSetTime = prefUtil.getAlarmSetTime()
        if (alarmSetTime > 0) {
            secondsRemaining -= alarmUtil.nowSeconds - alarmSetTime
        } else if (timerState == TimerState.RUNNING) {
            startTimer()
        }

        if (timerState == TimerState.RUNNING) startTimer()

        // UI
        timerView.updateButtons(timerState)
        timerView.updateCountdownUI()

    }

    private fun setPreviousTimerLength() {
        // Presenter
        timerLengthSeconds = prefUtil.getPreviousTimerLengthSeconds()
        // UI
        timerView.setMaxProgressCountdown(timerLengthSeconds.toInt())
    }

    override fun stopTimer() {
        if (countDownTimer != null) countDownTimer.cancel()
        onTimerFinished()
    }

    override fun pauseTimer() {
        countDownTimer.cancel()
        timerState = TimerState.PAUSED
        // UI
        timerView.updateButtons(timerState)
    }

    override fun onPauseView() {
        // Presenter
        if (timerState == TimerState.RUNNING) {
            countDownTimer.cancel()
            val wakeUpTime = alarmUtil.setAlarm(alarmUtil.nowSeconds, secondsRemaining)
            // show notification
            notificationUtil.showTimerRunning(wakeUpTime)
        } else if (timerState == TimerState.PAUSED) {
            // show notification
            notificationUtil.showTimerPaused()
        }
        // Presenter
        prefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds)
        prefUtil.setSecondsRemaining(secondsRemaining)
        prefUtil.setTimerState(timerState)
    }


}
