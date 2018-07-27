package com.example.user2.fuelcalc.ui;

import android.os.Bundle;

import com.example.user2.fuelcalc.R;

import android.support.v7.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        //setPreferencesFromResource(R.xml.settings, rootKey);
        addPreferencesFromResource(R.xml.settings);
    }
}