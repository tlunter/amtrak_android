package com.tlunter.amtrak;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
public class FetchJson extends AsyncTask<String, Integer, List<Train>> {
    private final String LOG_TEXT = "FetchJson";
    String from;
    String to;
    TrainDrawer trainDrawer;

    public FetchJson(String from, String to, TrainDrawer trainDrawer) {
        super();
        this.from = from;
        this.to = to;
        this.trainDrawer = trainDrawer;
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

    protected void onPreExecute() {
        trainDrawer.drawLoading();
    }

    @Override
    protected List<Train> doInBackground(String... urls) {
        Log.d(LOG_TEXT, "Starting background task");
        String json = downloadStatusPage();
        Log.d(LOG_TEXT, "Downloaded status page");
        List<Train> trains = new ArrayList<>();
        try {
            Log.d(LOG_TEXT, "Parsing trains");
            JSONArray jsonTrains = new JSONArray(json);
            for (int i = 0; i < jsonTrains.length(); i++) {
                Log.d(LOG_TEXT, "Parsing train #" + i);
                JSONObject jsonTrain = jsonTrains.getJSONObject(i);
                Integer number = jsonTrain.getInt("number");
                JSONObject arrival = jsonTrain.getJSONObject("departure");
                String scheduled = arrival.getString("scheduled_time");
                String estimated = arrival.getString("estimated_time");
                Boolean acela = number.toString().length() > 3 && number.toString().startsWith("2");
                Train train = new Train(number, scheduled, estimated, acela);
                trains.add(train);
            }
            Log.d(LOG_TEXT, "Done parsing trains");
            return trains;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList<Train>();
    }

    protected void onProgressUpdate (Integer... progress) {
        trainDrawer.drawProgress(progress[0]);
    }

    protected void onPostExecute(List<Train> result) {
        trainDrawer.drawTrains(result);
    }

    private String downloadStatusPage() {
        BufferedInputStream is = null;
        if (from != null && from.length() > 0 && to != null && to.length() > 0) {
            String urlString = "http://amtrak.tlunter.com/" + from + "/" + to + ".json";

            try {
                Log.d(LOG_TEXT, "Building URL");
                URL url = new URL(urlString);
                Log.d(LOG_TEXT, "Opening connection");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(2000);
                conn.setDoInput(true);
                Log.d(LOG_TEXT, "Connecting");
                conn.connect();

                is = new BufferedInputStream(conn.getInputStream());

                Log.d(LOG_TEXT, "Reading stream");
                return readStream(is, conn.getContentLength());
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
        }
        return "[]";
    }

    private String readStream(InputStream is, Integer length) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder bo = new StringBuilder();
            String line;
            Integer charCount = 0;
            Log.d(LOG_TEXT, "Starting to read");
            while((line = reader.readLine()) != null) {
                Log.d(LOG_TEXT, "Read one line");
                bo.append(line);
                charCount += line.length();
                Integer progress = charCount * 100 / length;
                publishProgress(progress);
            }
            Log.d(LOG_TEXT, "Done reading");
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }
}