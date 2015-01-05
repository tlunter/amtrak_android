package com.tlunter.amtrakstatus;

import android.app.ActionBar;
import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class StatusDisplay extends ActionBarActivity {
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

        if (testConnectivity()) {
            reloadTrains();
        }
    }

    private void reloadTrains() {
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

        if (reloadService != null) {
            reloadService.end();
        }

        Log.d(LOG_TEXT, "Making a new reload service");
        reloadService = new DataReloadService(preferences, trainListing);
        reloadService.start();
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
//            status.setText("Network Info NULL");
            return false;
        } else if (!networkInfo.isConnected()) {
//            status.setText("Cannot connect to Internet!");
            return false;
        } else {
//            status.setText(":) Let's go");
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
            Log.d(LOG_TEXT, "Settings clicked");
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (testConnectivity()) {
            reloadTrains();
        }
    }
}
