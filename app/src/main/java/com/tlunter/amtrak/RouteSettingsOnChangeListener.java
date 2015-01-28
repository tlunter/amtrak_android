package com.tlunter.amtrak;

import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.util.Log;

/**
 * Created by toddlunter on 1/22/15.
 */
public class RouteSettingsOnChangeListener implements Preference.OnPreferenceChangeListener {
    RouteSettings rs;
    RouteSettingsOnChangeListener(RouteSettings rs) {
        this.rs = rs;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Log.d("com.tlunter.amtrak.PrefsFragment", "Preference " + preference.toString());
        Log.d("com.tlunter.amtrak.PrefsFragment", "New object value: " + newValue.toString());
        if (this.rs.getId() != null) {
            Log.d("com.tlunter.amtrak.PrefsFragment", "Current id: " + this.rs.getId().toString());
        } else {
            Log.d("com.tlunter.amtrak.PrefsFragment", "New Record");
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
