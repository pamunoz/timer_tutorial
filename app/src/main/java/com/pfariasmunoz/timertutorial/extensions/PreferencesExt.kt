package com.pfariasmunoz.timertutorial.extensions

import android.content.SharedPreferences

fun SharedPreferences.put(key: String, value: Any) {
    when(value) {
        is String -> this.edit().putString(key, value).apply()
        is Int -> this.edit().putInt(key, value).apply()
        is Float -> this.edit().putFloat(key, value).apply()
        is Boolean -> this.edit().putBoolean(key, value).apply()
        is Long -> this.edit().putLong(key, value).apply()
    }
}


