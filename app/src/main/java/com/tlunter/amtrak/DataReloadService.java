package com.tlunter.amtrak;

import android.util.Log;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by toddlunter on 12/15/14.
 */
public class DataReloadService {
    private final String LOG_TEXT = "DataReloadService";
    RouteSettings preferences;
    TrainDrawer callback;
    ScheduledThreadPoolExecutor executor;

    public DataReloadService(RouteSettings routeSettings, TrainDrawer callback) {
        this.preferences = routeSettings;
        this.callback = callback;
    }

    public void start() {
        executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleAtFixedRate(new FetchJsonRunnable(preferences.fromStation, preferences.toStation, callback), 0, 30, TimeUnit.SECONDS);
    }

    public void end() {
        executor.shutdownNow();
    }

    public boolean isRunning() {
        if (executor != null) {
            return !(executor.isTerminated() || executor.isTerminated() || executor.isShutdown());
        } else {
            return false;
        }
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
