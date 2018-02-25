package com.pfariasmunoz.timertutorial.timer

import com.pfariasmunoz.timertutorial.mvp.BasePresenter
import com.pfariasmunoz.timertutorial.mvp.BaseView

interface TimerContract {

    interface View : BaseView<Presenter> {

    }

    interface Presenter : BasePresenter {

    }
}