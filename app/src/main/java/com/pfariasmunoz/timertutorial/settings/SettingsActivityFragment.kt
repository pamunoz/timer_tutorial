package com.pfariasmunoz.timertutorial.settings

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import com.pfariasmunoz.timertutorial.R

/**
 * Created by GAMER on 23-02-2018.
 */
class SettingsActivityFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

}