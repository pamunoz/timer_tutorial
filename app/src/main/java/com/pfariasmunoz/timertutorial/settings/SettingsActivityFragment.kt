package com.pfariasmunoz.timertutorial.settings

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.View
import com.pavelsikun.seekbarpreference.SeekBarPreferenceCompat
import com.pfariasmunoz.timertutorial.R

class SettingsActivityFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

}