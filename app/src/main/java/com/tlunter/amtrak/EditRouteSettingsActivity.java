package com.tlunter.amtrak;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

public class EditRouteSettingsActivity extends ActionBarActivity {
    private final String LOG_TEXT = "com.tlunter.amtrak.EditRouteSettingsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout linearLayout = new LinearLayout(this);
        setContentView(linearLayout);

        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        EditRouteSettingsFragment prefs = new EditRouteSettingsFragment();
        if (getIntent().getLongExtra("routeSettings", new Long(-1)) > -1) {
            setTitle("Edit Route Settings");
        }
        prefs.setArguments(getIntent().getExtras());
        mFragmentTransaction.replace(android.R.id.content, prefs);
        mFragmentTransaction.commit();
    }
}
