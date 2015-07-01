package com.tlunter.amtrak;

import android.os.Handler;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by toddlunter on 12/15/14.
 */
public class DataReloadService {
    private final String LOG_TEXT = "DataReloadService";
    RouteSettings preferences;
    TrainDrawer callback;
    Handler handler;
    TimerTask task;

    public DataReloadService(RouteSettings routeSettings, TrainDrawer callback) {
        this.preferences = routeSettings;
        this.callback = callback;
        this.handler = new Handler();
    }

    public void start() {
        Timer timer = TimerSingleton.instance();
        this.task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new FetchJsonRunnable(preferences.fromStation, preferences.toStation, callback));
            }
        };
        timer.schedule(this.task, 0, 45000);
    }

    public void runNow() {
        if (this.task != null) {
            this.task.run();
        }
    }

    public void end() {
        if (this.task != null) {
            this.task.cancel();
        }
    }

    public boolean isRunning() {
        return this.task != null;
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
