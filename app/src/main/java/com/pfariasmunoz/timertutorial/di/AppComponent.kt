package com.pfariasmunoz.timertutorial.di

import android.content.Context
import com.pfariasmunoz.timertutorial.di.modules.ContextModule
import com.pfariasmunoz.timertutorial.timer.TimerPresenter
import com.pfariasmunoz.timertutorial.util.AlarmUtil
import com.pfariasmunoz.timertutorial.util.NotificationUtil
import com.pfariasmunoz.timertutorial.util.PrefUtil
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class])
interface AppComponent {
    fun appContext(): Context

    fun prefUtil(): PrefUtil

    fun alarmUtil(): AlarmUtil

    fun notificationUtil(): NotificationUtil

    fun timerPresenter(): TimerPresenter

}