package com.tlunter.amtrakstatus;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;
import java.util.ListIterator;

/**
 * Created by toddlunter on 12/12/14.
 */
public class TrainListing implements TrainDrawer {
    public static final String PREFS_NAME = "TrainListing";

    private String LOG_TEXT = "com.tlunter.amtrak.TrainListing";
    private Context context;
    private ViewGroup view;
    private Preferences preferences;
    private List<Train> trains;

    public TrainListing(Context context, ViewGroup view, Preferences preferences) {
        this.context = context;
        this.view = new ScrollView(context);
        this.preferences = preferences;
        view.addView(this.view);
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    public void drawTrains(List<Train> trains) {
        this.trains = trains;
        redraw();
    }

    public void redraw() {
        if (this.trains != null) {
            view.removeAllViews();
            int train = findPreferredTrain(trains);
            if (train > -1) {
                drawPreferredTrainListing(trains, train);
            } else {
                drawAllTrains(trains);
                Log.d(LOG_TEXT, "No preferred train found");
            }
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
            int previousTrainCount = 0;
            int alpha = 145;
            ListIterator<Train> prevTrains = trains.listIterator(train - 1);
            while (prevTrains.hasPrevious()) {
                Train previousTrain = prevTrains.previous();
                Log.d(LOG_TEXT, "Previous Train: " + previousTrain.getNumber().toString() + " Acela? " + previousTrain.getAcela().toString());
                if (!previousTrain.acela || (previousTrain.acela && !preferences.getHideAcela())) {
                    TableRow prev = buildTableRow(previousTrain, alpha);
                    table.addView(prev, 0);
                    previousTrainCount++;
                    alpha -= 45;
                    if (previousTrainCount > 1)
                        break;
                }
            }
        } catch (IndexOutOfBoundsException ex) {
            Log.d(LOG_TEXT, "No previous train");
        }
        TableRow preferred = buildTableRow(trains.get(train), 255);
        table.addView(preferred);
        try {
            int nextTrainCount = 0;
            int alpha = 145;
            ListIterator<Train> nextTrains = trains.listIterator(train + 1);
            while (nextTrains.hasNext()) {
                Train nextTrain = nextTrains.next();
                Log.d(LOG_TEXT, "Next Train: " + nextTrain.getNumber().toString() + " Acela? " + nextTrain.getAcela().toString());
                if (!nextTrain.acela || (nextTrain.acela && !preferences.getHideAcela())) {
                    TableRow next = buildTableRow(nextTrain, alpha);
                    table.addView(next);
                    nextTrainCount++;
                    alpha -= 45;
                    if (nextTrainCount > 1)
                        break;
                }
            }
        } catch (IndexOutOfBoundsException ex) {
            Log.d(LOG_TEXT, "No next train");
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
        Train train = new Train(this.preferences.getPreferredTrain());
        return trains.indexOf(train);
    }
}
