package com.tlunter.amtrak;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Created by toddlunter on 1/22/15.
 */
public class AllRouteSettingsFragment extends AbstractRouteSettingsFragment {
    private final String LOG_TEXT = "com.tlunter.amtrak.RouteSettingsFragment";

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TEXT, "On resume called");

        PreferenceScreen mainPreferenceScreen = this.getOrCreatePreferenceScreen();
        mainPreferenceScreen.removeAll();

        List<RouteSettings> routeSettings = RouteSettings.listAll(RouteSettings.class);
        Log.d("com.tlunter.amtrak.RouteSettings", "Route settings: ");
        for (RouteSettings rs : routeSettings) {
            Log.d("com.tlunter.amtrak.RouteSettings", "Adding RouteSettings ID: " + rs.getId().toString());
            PreferenceScreen routeSettingsPS = this.createPreferenceScreen(
                    rs.toString()
            );
            routeSettingsPS.setKey(rs.getId().toString());
            mainPreferenceScreen.addPreference(routeSettingsPS);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen prefScreen, Preference preference) {
        super.onPreferenceTreeClick(prefScreen, preference);

        Log.d(LOG_TEXT, "Preference: " + preference.toString());
        if (preference instanceof PreferenceScreen) {
            Log.d(LOG_TEXT, "Preference is PreferenceScreen");
            PreferenceScreen ps = (PreferenceScreen)preference;
            Long id = Long.parseLong(ps.getKey());

            Intent intent = new Intent(getActivity(), EditRouteSettingsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("routeSettings", id);
            intent.putExtras(bundle);
            startActivity(intent);

            return true;
        }

        return false;
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(LOG_TEXT, "On pause called");
    }

    protected PreferenceScreen createPreferenceScreen(String title) {
        PreferenceScreen ps = getPreferenceManager().createPreferenceScreen(getActivity());
        ps.setTitle(title);
        return ps;
    }
}
