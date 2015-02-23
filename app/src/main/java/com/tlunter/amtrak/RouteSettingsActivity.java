package com.tlunter.amtrak;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class RouteSettingsActivity extends ActionBarActivity {
    private final String LOG_TEXT = "RouteSettingsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout linearLayout = new LinearLayout(this);
        setContentView(linearLayout);

        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        RouteSettingsFragment prefs = new RouteSettingsFragment();
        if (getIntent().getLongExtra("routeSettings", new Long(-1)) > -1) {
            setTitle("Edit Route Settings");
        }
        prefs.setArguments(getIntent().getExtras());
        mFragmentTransaction.replace(android.R.id.content, prefs);
        mFragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (getIntent().getLongExtra("routeSettings", new Long(-1)) > -1) {
            getMenuInflater().inflate(R.menu.menu_edit_route_settings, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_destroy) {
            Long routeSettingsId = getIntent().getLongExtra("routeSettings", new Long(-1));
            if (routeSettingsId > -1) {
                RouteSettings.findById(RouteSettings.class, routeSettingsId).delete();
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
