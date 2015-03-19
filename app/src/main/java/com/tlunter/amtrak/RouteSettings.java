package com.tlunter.amtrak;

import android.util.Log;

import com.orm.SugarRecord;

/**
 * Created by toddlunter on 1/21/15.
 */
public class RouteSettings extends SugarRecord<RouteSettings> {
    String fromStation;
    String toStation;
    String preferredTrainNumber;
    Boolean hideAcela;

    public RouteSettings() {
    }

    public RouteSettings(String fromStation, String toStation, String preferredTrainNumber, Boolean hideAcela) {
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.preferredTrainNumber = preferredTrainNumber;
        this.hideAcela = hideAcela;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        if (fromStation != null) {
            str.append(fromStation.toUpperCase().toString());
        } else {
            str.append("Not set");
        }
        str.append(" - ");
        if (toStation != null) {
            str.append(toStation.toUpperCase().toString());
        } else {
            str.append("Not set");
        }
        if (preferredTrainNumber != null && preferredTrainNumber.length() > 0) {
            str.append(" - " + preferredTrainNumber.toString());
        }
        if (hideAcela != null) {
            if (hideAcela == false) {
                str.append(" (With Acela)");
            }
        }
        return str.toString();
    }

    public Boolean getHideAcela() {
        if (hideAcela == null)
            return false;
        return hideAcela;
    }

    public void setSettings(String key, Object newValue) {
        switch(key) {
            case "from_station":
                fromStation = (String)newValue;
                break;
            case "to_station":
                toStation = (String)newValue;
                break;
            case "preferred_train_number":
                preferredTrainNumber = (String)newValue;
                break;
            case "hide_acela":
                hideAcela = (Boolean)newValue;
                break;
            default:
                Log.d("RouteSettings", "Trying to set " + key + " to value " + newValue.toString());
                Log.d("RouteSettings", "Don't know that key");
        }
    }
}
