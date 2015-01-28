package com.tlunter.amtrak;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by toddlunter on 1/3/15.
 */
public class Preferences {
    private final String LOG_TEXT = "com.tlunter.amtrak.Preferences";
    private Context context;
    private String from;
    private String to;
    private Integer preferredTrain;
    private Boolean hideAcela;

    public Preferences(Context context) {
        this.context = context;
    }

    public void reload() {
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.from = mySharedPreferences.getString("from", null);
        this.to = mySharedPreferences.getString("to", null);
        String preferredTrainStr = mySharedPreferences.getString("preferred_train", "-1");
        if (preferredTrainStr.length() == 0) preferredTrainStr = "-1";
        this.preferredTrain = new Integer(preferredTrainStr);
        this.hideAcela = mySharedPreferences.getBoolean("hide_acela_trains", false);
        Log.d(LOG_TEXT, "hide acela: " + this.hideAcela.toString());
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Integer getPreferredTrain() {
        return preferredTrain;
    }

    public Boolean getHideAcela() {
        return hideAcela;
    }
}
