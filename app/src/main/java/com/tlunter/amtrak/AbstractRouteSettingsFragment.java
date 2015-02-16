package com.tlunter.amtrak;

import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.text.InputType;

/**
 * Created by toddlunter on 1/22/15.
 */
abstract public class AbstractRouteSettingsFragment extends PreferenceFragment {
    protected PreferenceScreen getOrCreatePreferenceScreen() {
        if (this.getPreferenceScreen() == null) {
            this.setPreferenceScreen(getPreferenceManager().createPreferenceScreen(getActivity()));
        }
        return this.getPreferenceScreen();
    }
}
