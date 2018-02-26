package com.pfariasmunoz.timertutorial.timer

import android.os.CountDownTimer
import com.pfariasmunoz.timertutorial.R.id.textView_countdown
import kotlinx.android.synthetic.main.content_timer.*

class TimerPresenter(val timerView: TimerContract.View) : TimerContract.Presenter {

    // TimerState
    // CoundownTimer
    // PrefUtil
    // AlarmUtil

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

    }

}
