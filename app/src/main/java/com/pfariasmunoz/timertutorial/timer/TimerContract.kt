package com.pfariasmunoz.timertutorial.timer

import com.pfariasmunoz.timertutorial.mvp.BasePresenter
import com.pfariasmunoz.timertutorial.mvp.BaseView

interface TimerContract {

    interface View : BaseView<Presenter> {
        fun updateButtons(timerState: TimerState)
        fun updateCountdownUI()
        fun resetProgressCountdown()
        fun setMaxProgressCountdown(max: Int)
    }

    interface Presenter : BasePresenter {
        val progress: Int
        val timerText: String
        fun setView(view: TimerContract.View)
        fun startTimer()
        fun stopTimer()
        fun pauseTimer()
        fun onPauseView()
    }
}