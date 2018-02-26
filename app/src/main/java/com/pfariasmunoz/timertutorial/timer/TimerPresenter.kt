package com.pfariasmunoz.timertutorial.timer

import android.os.CountDownTimer
import com.pfariasmunoz.timertutorial.R.id.textView_countdown
import kotlinx.android.synthetic.main.content_timer.*

class TimerPresenter : TimerContract.Presenter {

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

    override fun start() {

    }

}
