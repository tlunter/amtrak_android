package com.tlunter.amtrak;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

/**
 * Created by toddlunter on 12/30/14.
 */
public class RouteSettingsActivity extends ActionBarActivity {
    private final String LOG_TEXT = "com.tlunter.amtrak.TrainsSettings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout linearLayout = new LinearLayout(this);
        setContentView(linearLayout);
        resetFragment();
    }

    private void resetFragment() {
        FragmentManager mFragmentManager = getFragmentManager();

        Log.d(LOG_TEXT, "Back stack length: " + mFragmentManager.getBackStackEntryCount());

        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        AllRouteSettingsFragment prefs = new AllRouteSettingsFragment();
        mFragmentTransaction.add(android.R.id.content, prefs);
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();

        mFragmentManager.executePendingTransactions();

        Log.d(LOG_TEXT, "Back stack length: " + mFragmentManager.getBackStackEntryCount());
    }

    @Override
    public void onBackPressed() {
        FragmentManager mFragmentManager = getFragmentManager();
        if (mFragmentManager.getBackStackEntryCount() > 1) {
            mFragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(LOG_TEXT, "On pause called");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TEXT, "Resetting fragment");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_route_settings) {
            Log.d(LOG_TEXT, "AddRouteSettings clicked");
            Intent intent = new Intent(this, EditRouteSettingsActivity.class);
            intent.putExtras(new Bundle());
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
