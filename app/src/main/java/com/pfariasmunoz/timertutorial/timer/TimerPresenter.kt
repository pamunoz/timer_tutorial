package com.pfariasmunoz.timertutorial.timer

import android.os.CountDownTimer
import com.pfariasmunoz.timertutorial.util.AlarmUtil
import com.pfariasmunoz.timertutorial.util.NotificationUtil
import com.pfariasmunoz.timertutorial.util.PrefUtil

class TimerPresenter(
        val timerView: TimerContract.View,
        val alarmUtil: AlarmUtil,
        val prefUtil: PrefUtil,
        val notificationUtil: NotificationUtil) : TimerContract.Presenter {

    private var countDownTimer: CountDownTimer? = null
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
                timerView.updateCountdownUI()
            }
        }.start()
    }

    private fun onTimerFinished() {
        timerState = TimerState.STOPPED
        // We set the length of the countDownTimer to be the one set on the settings activity
        // if the length was changed when the countDownTimer was running
        setNewTimerLength()
        prefUtil.setSecondsRemaining(timerLengthSeconds)
        secondsRemaining = timerLengthSeconds
        timerView.apply {
            resetProgressCountdown()
            updateButtons(timerState)
            updateCountdownUI()
        }
    }

    private fun setNewTimerLength() {
        val lengthInMinutes = prefUtil.getTimerLength()
        timerLengthSeconds = (lengthInMinutes * 60L)
        timerView.setMaxProgressCountdown(timerLengthSeconds.toInt())
    }

    private fun initTimer() {
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
        timerView.apply {
            updateButtons(timerState)
            updateCountdownUI()
        }
    }

    private fun setPreviousTimerLength() {
        timerLengthSeconds = prefUtil.getPreviousTimerLengthSeconds()
        timerView.setMaxProgressCountdown(timerLengthSeconds.toInt())
    }

    override fun stopTimer() {
        countDownTimer?.cancel()
        onTimerFinished()
    }

    override fun pauseTimer() {
        countDownTimer?.cancel()
        timerState = TimerState.PAUSED
        timerView.updateButtons(timerState)
    }

    override fun onPauseView() {
        if (timerState == TimerState.RUNNING) {
            countDownTimer?.cancel()
            val wakeUpTime = alarmUtil.setAlarm(alarmUtil.nowSeconds, secondsRemaining)
            // show notification
            notificationUtil.showTimerRunning(wakeUpTime)
        } else if (timerState == TimerState.PAUSED) {
            // show notification
            notificationUtil.showTimerPaused()
        }
        with(prefUtil) {
            setPreviousTimerLengthSeconds(timerLengthSeconds)
            setSecondsRemaining(secondsRemaining)
            setTimerState(timerState)
        }
    }
}
