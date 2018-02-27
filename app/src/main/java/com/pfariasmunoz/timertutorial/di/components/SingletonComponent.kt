package com.pfariasmunoz.timertutorial.di.components

import android.content.Context
import com.pfariasmunoz.timertutorial.di.modules.ContextModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class])
interface SingletonComponent {
    fun appContext(): Context
}