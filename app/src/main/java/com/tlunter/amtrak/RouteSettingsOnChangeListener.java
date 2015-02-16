package com.tlunter.amtrak;

import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.util.Log;

/**
 * Created by toddlunter on 1/22/15.
 */
public class RouteSettingsOnChangeListener implements Preference.OnPreferenceChangeListener {
    private final String LOG_TEXT = "PrefsFragment";
    RouteSettings rs;
    RouteSettingsOnChangeListener(RouteSettings rs) {
        this.rs = rs;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Log.d(LOG_TEXT, "Preference " + preference.toString());
        Log.d(LOG_TEXT, "Key: " + preference.getKey().toString());
        Log.d(LOG_TEXT, "New object value: " + newValue.toString());
        if (this.rs.getId() != null) {
            Log.d(LOG_TEXT, "Current id: " + this.rs.getId().toString());
        } else {
            Log.d(LOG_TEXT, "New Record");
        }

        rs.setSettings(preference.getKey(), newValue);
        rs.save();

        if (preference instanceof EditTextPreference) {
            ((EditTextPreference) preference).setText((String)newValue);
        } else if (preference instanceof CheckBoxPreference) {
            ((CheckBoxPreference) preference).setChecked((Boolean)newValue);
        }

        return false;
    }
}
