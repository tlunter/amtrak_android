package com.tlunter.amtrak;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrainListingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrainListingFragment extends Fragment implements TrainDrawer {
    private final String LOG_TEXT = "TrainListingFragment";

    private RouteSettings routeSettings;
    private DataReloadService dataReloadService;
    private List<Train> trains;
    private TableLayout trainTable;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TrainListingFragment.
     */
    public static TrainListingFragment newInstance(RouteSettings rs) {
        TrainListingFragment fragment = new TrainListingFragment();
        Bundle args = new Bundle();
        args.putLong("recordId", rs.getId());
        fragment.setArguments(args);
        return fragment;
    }

    public TrainListingFragment() {
        trains = new ArrayList<Train>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Long recordId = getArguments().getLong("recordId");
            routeSettings = RouteSettings.findById(RouteSettings.class, recordId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_train_listing, container, false);
        trainTable = (TableLayout)rootView.findViewById(R.id.trainTable);

        Log.d(LOG_TEXT, "Display: " + routeSettings.toString());

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        endDataReloadService();
        dataReloadService = new DataReloadService(routeSettings, this);
        dataReloadService.start();

        Log.d(LOG_TEXT, "On Resume called: " + routeSettings.toString());
    }

    @Override
    public void onPause() {
        super.onPause();

        endDataReloadService();

        Log.d(LOG_TEXT, "On Pause called: " + routeSettings.toString());
    }

    public void endDataReloadService() {
        if (dataReloadService != null && dataReloadService.isRunning()) {
            dataReloadService.end();
            dataReloadService = null;
        }
    }

    public void drawTrains(List<Train> trains) {
        Log.d(LOG_TEXT, "Drawing trains for " + routeSettings.toString());
        this.trains = trains;

        if (getActivity() != null) {
            redraw();
        }
    }

    public void redraw() {
        trainTable.removeAllViews();

        drawAllTrains(trains);
    }

    private void drawAllTrains(List<Train> trains) {
        for (Train t : trains) {
            trainTable.addView(buildTableRow(t));
        }
    }


    private TableRow buildTableRow(Train train) {
        return buildTableRow(train, 255);
    }

    private TableRow buildTableRow(Train train, int alpha) {
        TableRow trainLayout = new TableRow(getActivity());
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
        TextView textView = new TextView(getActivity());
        TableRow.LayoutParams layout = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        textView.setPadding(15, 5, 15, 5);
        textView.setLayoutParams(layout);
        textView.setTextSize(40);
        textView.setTextColor(Color.argb(alpha, 0, 0, 0));
        textView.setText(text);
        Log.d(LOG_TEXT, "Drawing: " + text.toString());
        return textView;
    }
}
