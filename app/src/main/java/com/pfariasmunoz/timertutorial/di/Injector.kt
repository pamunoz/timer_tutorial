package com.pfariasmunoz.timertutorial.di

import com.pfariasmunoz.timertutorial.App

object Injector {
    fun get(): AppComponent = App.INSTANCE.component
}
