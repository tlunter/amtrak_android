package com.tlunter.amtrak;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;


public class NetworkConnectivity extends ActionBarActivity implements ConnectivityTest.ConnectivityTestInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_connectivity);
    }

    public void onRetryClick(View view) {
        ConnectivityTest.test(this, this);
    }

    public void onResume() {
        super.onResume();

        ConnectivityTest.test(this, this);
    }

    public void testSuccess() {
        Intent intent = new Intent(this, StatusDisplay.class);
        startActivity(intent);
        finish();
    }

    public void testFailure() { }
}
