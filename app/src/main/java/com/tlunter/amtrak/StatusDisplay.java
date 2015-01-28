package com.tlunter.amtrak;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class StatusDisplay extends ActionBarActivity implements
        TrainListingFragment.OnTrainListingInteractionListener,
        NetworkConnectivityFragment.OnNetworkConnectivityInteractionListener {
    private final String LOG_TEXT = "com.tlunter.amtrak.StatusDisplay";
    private LinearLayout linearLayout;
    private TrainListing trainListing;
    private DataReloadService reloadService;
    private Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLinearLayout();
        setContentView(linearLayout);
    }

    private void reloadTrains() {
        /*
        if (preferences == null)
            preferences = new Preferences(this);

        preferences.reload();

        if (preferences.getFrom() == null || preferences.getTo() == null)
            return;

        if (trainListing == null) {
            trainListing = new TrainListing(this, linearLayout, preferences);
        } else {
            trainListing.redraw();
        }

        if (reloadService != null && reloadService.isRunning()) {
            reloadService.end();
        }

        Log.d(LOG_TEXT, "Making a new reload service");
        reloadService = new DataReloadService(preferences, trainListing);
        reloadService.start();
        */
    }

    private void setupLinearLayout() {
        linearLayout = new LinearLayout(this);
        ViewGroup.LayoutParams layout = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        linearLayout.setLayoutParams(layout);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
    }

    private boolean testConnectivity() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (null == networkInfo) {
            Log.d(LOG_TEXT, "Load NetworkConnectivityFragment");
            loadNetworkConnectivityFragment();
            return false;
        } else if (!networkInfo.isConnected()) {
            Log.d(LOG_TEXT, "Load NetworkConnectivityFragment");
            loadNetworkConnectivityFragment();
            return false;
        } else {
            Log.d(LOG_TEXT, "Load TrainListingFragment");
            loadTrainListingFragment();
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_status_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.d(LOG_TEXT, "Route settings clicked");
            Intent intent = new Intent(this, RouteSettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(LOG_TEXT, "Pausing reload");

        if (reloadService != null && reloadService.isRunning()) {
            reloadService.end();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TEXT, "Resuming reload");

        testConnectivity();
    }

    private void loadNetworkConnectivityFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        NetworkConnectivityFragment fragment = NetworkConnectivityFragment.newInstance();
        ft.replace(android.R.id.content, fragment);
        ft.commit();
    }

    private void loadTrainListingFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        TrainListingFragment fragment = TrainListingFragment.newInstance();
        ft.replace(android.R.id.content, fragment);
        ft.commit();
    }

    public void onNetworkConnectivityInteraction() {
        testConnectivity();
    }

    public void onTrainListingInteraction(Uri uri) {}
}
