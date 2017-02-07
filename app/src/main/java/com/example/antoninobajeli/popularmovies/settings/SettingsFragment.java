package com.example.antoninobajeli.popularmovies.settings;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import com.example.antoninobajeli.popularmovies.R;

/**
 * Created by antoninobajeli on 05/02/17.
 */

public class SettingsFragment extends PreferenceFragmentCompat {
    static String LOG_TAG = SettingsActivity.class.getSimpleName();
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        Log.d(LOG_TAG,"onCreatePreferences");
        addPreferencesFromResource(R.xml.pref_general);
    }

}