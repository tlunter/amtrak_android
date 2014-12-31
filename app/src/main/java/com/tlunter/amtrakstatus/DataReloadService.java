package com.tlunter.amtrakstatus;

import android.util.Log;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by toddlunter on 12/15/14.
 */
public class DataReloadService {
    private final String LOG_TEXT = "com.tlxunter.amtrak.DataReloadService";
    String from, to;
    TrainDrawer callback;
    ScheduledThreadPoolExecutor executor;

    public DataReloadService(String from, String to, TrainDrawer callback) {
        this.from = from;
        this.to = to;
        this.callback = callback;
    }

    public void start() {
        executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleAtFixedRate(new FetchJsonRunnable(from, to, callback), 0, 60, TimeUnit.SECONDS);
    }

    public void end() {
        executor.shutdownNow();
    }

    private class FetchJsonRunnable implements Runnable {
        String from, to;
        TrainDrawer callback;

        public FetchJsonRunnable(String from, String to, TrainDrawer callback) {
            this.from = from;
            this.to = to;
            this.callback = callback;
        }
        public void run() {
            Log.d(LOG_TEXT, "Getting new trains");
            new FetchJson(from, to, callback).execute();
        }
    }
}
