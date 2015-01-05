package com.tlunter.amtrakstatus;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by toddlunter on 12/11/14.
 */
public class FetchJson extends AsyncTask<String, Void, List<Train>> {
    private final String LOG_TEXT = "com.tlunter.amtrak.FetchJson";
    String from;
    String to;
    TrainDrawer callback;

    public FetchJson(String from, String to, TrainDrawer callback) {
        super();
        this.from = from;
        this.to = to;
        this.callback = callback;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    protected List<Train> doInBackground(String... urls) {
        String json = downloadStatusPage();
        List<Train> trains = new ArrayList<>();
        try {
            JSONArray jsonTrains = new JSONArray(json);
            for (int i = 0; i < jsonTrains.length(); i++) {
                JSONObject jsonTrain = jsonTrains.getJSONObject(i);
                Integer number = jsonTrain.getInt("number");
                JSONObject arrival = jsonTrain.getJSONObject("departure");
                String scheduled = arrival.getString("scheduled_time");
                String estimated = arrival.getString("estimated_time");
                Boolean acela = number.toString().length() > 3 && number.toString().startsWith("2");
                Train train = new Train(number, scheduled, estimated, acela);
                trains.add(train);
            }
            return trains;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList<Train>();
    }

    protected void onPostExecute(List<Train> result) {
        callback.drawTrains(result);
    }

    private String downloadStatusPage() {
        BufferedInputStream is = null;
        String urlString = "http://amtrak.tlunter.com/" + from + "/" + to + ".json";

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(60000);
            conn.setConnectTimeout(60000);
            conn.setDoInput(true);
            conn.connect();

            is = new BufferedInputStream(conn.getInputStream());

            return readStream(is);
        } catch (UnknownHostException e) {
            Log.d(LOG_TEXT, "Unknown host?");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            Log.d(LOG_TEXT, "Malformed URL: " + urlString);
            e.printStackTrace();
        } catch (ProtocolException e) {
            Log.d(LOG_TEXT, "Protocol Error: " + e.getClass().getName());
            e.printStackTrace();
        } catch (IOException e) {
            Log.d(LOG_TEXT, "IO Exception: " + e.getClass().getName());
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "[]";
    }

    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }
}