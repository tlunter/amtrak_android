package com.tlunter.amtrakstatus;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

/**
 * Created by toddlunter on 12/12/14.
 */
public class TrainListing implements TrainDrawer {
    private String LOG_TEXT = "com.tlunter.amtrak.TrainListing";
    private Context context;
    private ViewGroup view;

    public TrainListing(Context context, ViewGroup view) {
        this.context = context;
        this.view = new ScrollView(context);
        view.addView(this.view);
    }

    public void drawTrains(List<Train> trains) {
        view.removeAllViews();
        int train = findPreferredTrain(trains);
        if (train > -1) {
            drawPreferredTrainListing(trains, train);
        } else {
            drawAllTrains(trains);
            Log.d(LOG_TEXT, "No preferred train found");
        }
    }

    private void drawAllTrains(List<Train> trains) {
        TableLayout table = new TableLayout(context);
        TableLayout.LayoutParams layout = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        );
        table.setGravity(Gravity.CENTER);
        table.setLayoutParams(layout);
        for (Train t : trains) {
            table.addView(buildTableRow(t));
        }
        view.addView(table);
    }

    private void drawPreferredTrainListing(List<Train> trains, int train) {
        TableLayout table = new TableLayout(context);
        TableLayout.LayoutParams layout = new TableLayout.LayoutParams(
            TableLayout.LayoutParams.WRAP_CONTENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        );
        table.setGravity(Gravity.CENTER);
        table.setLayoutParams(layout);
        try {
            TableRow prev = buildTableRow(trains.get(train - 1), 100);
            table.addView(prev);
        } catch (IndexOutOfBoundsException e) {
            Log.d(LOG_TEXT, "No previous train");
        }
        TableRow preferred = buildTableRow(trains.get(train), 255);
        table.addView(preferred);
        try {
            TableRow next = buildTableRow(trains.get(train + 1), 100);
            table.addView(next);
        } catch (IndexOutOfBoundsException e) {
            Log.d(LOG_TEXT, "No previous train");
        }
        view.addView(table);
    }

    private TableRow buildTableRow(Train train) {
        return buildTableRow(train, 255);
    }

    private TableRow buildTableRow(Train train, int alpha) {
        TableRow trainLayout = new TableRow(context);
        TableRow.LayoutParams layout = new TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        );
        trainLayout.setLayoutParams(layout);
        trainLayout.addView(buildNumberView(train, alpha));
        trainLayout.addView(buildTimeView(train, alpha));
        return trainLayout;
    }

    private TextView buildNumberView(Train train, int alpha) {
        return buildTextView(train.getNumber().toString(), alpha);
    }

    private TextView buildTimeView(Train train, int alpha) {
        if (train.getEstimated().isEmpty()) {
            return buildTextView(train.getScheduled(), alpha);
        }
        return buildTextView(train.getEstimated(), alpha);
    }

    private TextView buildTextView(String text, int alpha) {
        TextView textView = new TextView(context);
        TableRow.LayoutParams layout = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        textView.setPadding(15,5,15,5);
        textView.setLayoutParams(layout);
        textView.setTextSize(40);
        textView.setTextColor(Color.argb(alpha, 0, 0, 0));
        textView.setText(text);
        Log.d(LOG_TEXT, "Drawing: " + text.toString());
        return textView;
    }

    private int findPreferredTrain(List<Train> trains) {
        Train preferredTrain = new Train(0);
        return trains.indexOf(preferredTrain);
    }
}
