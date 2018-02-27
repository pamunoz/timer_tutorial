package com.pfariasmunoz.timertutorial

import android.app.Application
import com.pfariasmunoz.timertutorial.di.components.DaggerSingletonComponent
import com.pfariasmunoz.timertutorial.di.AppComponent
import com.pfariasmunoz.timertutorial.di.modules.ContextModule

class App : Application() {

    companion object {
        @JvmStatic lateinit var INSTANCE: App
            private set
    }

    lateinit var component: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        component = DaggerSingletonComponent.builder()
                .contextModule(ContextModule(this))
                .build()
    }



}