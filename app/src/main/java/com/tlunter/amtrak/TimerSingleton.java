package com.tlunter.amtrak;

import java.util.Timer;

public class TimerSingleton {
    private static Timer timer;

    private TimerSingleton() { }

    public static Timer instance() {
        if (TimerSingleton.timer == null) {
            TimerSingleton.timer = new Timer();
        }

        return TimerSingleton.timer;
    }
}
