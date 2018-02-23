package com.pfariasmunoz.timertutorial.extensions

import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

val Context.defaultSharedPreferences: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(this)

val Context.notificationManager: NotificationManager
    get() = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager