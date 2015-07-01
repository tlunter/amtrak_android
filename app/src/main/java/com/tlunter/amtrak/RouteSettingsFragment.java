package com.tlunter.amtrak;

import android.app.FragmentManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.text.InputType;
import android.util.Log;
import android.view.View;

/**
 * Created by toddlunter on 1/22/15.
 */
public class RouteSettingsFragment extends PreferenceFragment {
    private final String LOG_TEXT = "RouteSettingsFragment";
    RouteSettings rs;

    protected PreferenceScreen getOrCreatePreferenceScreen() {
        if (this.getPreferenceScreen() == null) {
            this.setPreferenceScreen(getPreferenceManager().createPreferenceScreen(getActivity()));
        }
        return this.getPreferenceScreen();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PreferenceScreen mainPreferenceScreen = this.getOrCreatePreferenceScreen();

        Long i = getArguments().getLong("routeSettings", -1);
        if (i < 0) {
            this.rs = new RouteSettings();
        } else {
            this.rs = RouteSettings.findById(RouteSettings.class, i);
        }
        this.createFields(this.rs, mainPreferenceScreen);
    }
    protected void createFields(RouteSettings rs, PreferenceScreen ps) {
        RouteSettingsOnChangeListener onChangeListener = new RouteSettingsOnChangeListener(rs);
        PreferenceCategory destinations = this.createPreferenceCategory(
                R.string.destinations
        );
        EditTextPreference fromField = this.createEditTextField(
                "from_station", R.string.from,
                R.string.from_summary, rs.fromStation,
                onChangeListener
        );
        EditTextPreference toField = this.createEditTextField(
                "to_station", R.string.to,
                R.string.to_summary, rs.toStation,
                onChangeListener
        );
        PreferenceCategory trains = this.createPreferenceCategory(
                R.string.trains
        );
        EditTextPreference preferredTrainNumber = this.createEditTextField(
                "preferred_train_number", R.string.preferred_train,
                R.string.preferred_train_summary, rs.preferredTrainNumber,
                onChangeListener
        );
        preferredTrainNumber.getEditText().setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        CheckBoxPreference hideAcelaTrains = this.createCheckBox(
                "hide_acela", R.string.hide_acela_trains,
                R.string.hide_acela_trains_summary, rs.hideAcela,
                onChangeListener
        );


        ps.addPreference(destinations);
        ps.addPreference(trains);
        destinations.addPreference(fromField);
        destinations.addPreference(toField);
        trains.addPreference(preferredTrainNumber);
        trains.addPreference(hideAcelaTrains);
    }

    private PreferenceCategory createPreferenceCategory(int title) {
        PreferenceCategory pc = new PreferenceCategory(getActivity());
        pc.setTitle(title);
        return pc;
    }

    private EditTextPreference createEditTextField(String key, int title,
                                                   int summary, String defaultText,
                                                   Preference.OnPreferenceChangeListener onChangeListener) {
        EditTextPreference etp = new EditTextPreference(getActivity());
        etp.setKey(key);
        etp.setTitle(title);
        etp.setPersistent(false);
        if (defaultText != null) {
            etp.setSummary(defaultText);
        } else {
            etp.setSummary(summary);
        }
        if (defaultText != null) {
            etp.setText(defaultText);
        } else {
            etp.setText("");
        }
        etp.setOnPreferenceChangeListener(onChangeListener);
        return etp;
    }

    private CheckBoxPreference createCheckBox(String key, int title,
                                              int summary, Boolean defaultValue,
                                              Preference.OnPreferenceChangeListener onChangeListener) {
        CheckBoxPreference cbp = new CheckBoxPreference(getActivity());
        cbp.setKey(key);
        cbp.setTitle(title);
        cbp.setPersistent(false);
        cbp.setSummary(summary);
        cbp.setChecked(false);
        if (defaultValue != null) {
            cbp.setChecked(defaultValue);
        } else {
            cbp.setChecked(false);
        }
        cbp.setOnPreferenceChangeListener(onChangeListener);
        return cbp;
    }

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
}
