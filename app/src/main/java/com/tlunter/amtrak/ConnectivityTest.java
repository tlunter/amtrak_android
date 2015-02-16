package com.tlunter.amtrak;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by toddlunter on 2/13/15.
 */
public class ConnectivityTest {
    private static final String LOG_TEXT = "com.tlunter.amtrak.ConnectivityTest";

    // This should be called like: test(this, this)
    // Need to figure out how to do this later
    public static void test(Context context, ConnectivityTestInterface cti) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (null == networkInfo) {
            Log.d(LOG_TEXT, "TestFailure");
            cti.testFailure();
        } else if (!networkInfo.isConnected()) {
            Log.d(LOG_TEXT, "TestFailure");
            cti.testFailure();
        } else {
            Log.d(LOG_TEXT, "TestSuccess");
            cti.testSuccess();
        }
    }

    public interface ConnectivityTestInterface {
        public void testSuccess();
        public void testFailure();
    }
}
