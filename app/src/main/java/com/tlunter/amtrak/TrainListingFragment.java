package com.tlunter.amtrak;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
public class TrainListingFragment extends Fragment implements TrainDrawer, SwipeRefreshLayout.OnRefreshListener {
    private final String LOG_TEXT = "TrainListingFragment";

    private RouteSettings routeSettings;
    private DataReloadService dataReloadService;
    private List<Train> trains;
    private TableLayout trainTable;
    private SwipeRefreshLayout swipeLayout;
    private FrameLayout progressBar;

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
        this.trainTable = (TableLayout)rootView.findViewById(R.id.trainTable);
        this.swipeLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_container);
        this.swipeLayout.setOnRefreshListener(this);

        return rootView;
    }

    @Override
    public void onRefresh() {
        dataReloadService.runNow();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getArguments() != null) {
            Long recordId = getArguments().getLong("recordId");
            routeSettings = RouteSettings.findById(RouteSettings.class, recordId);
        }

        redraw();

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

    public void drawLoading() {
        Log.d(LOG_TEXT, "Getting trains for " + routeSettings.toString());
        if (getActivity() != null) {
            Log.d(LOG_TEXT, "Drawing progress bar");
            this.swipeLayout.setRefreshing(true);
        } else {
            Log.d(LOG_TEXT, "Get activity is null");
        }
    }

    public void drawProgress(Integer progress) {
    }

    public void drawTrains(List<Train> trains) {
        Log.d(LOG_TEXT, "Drawing trains for " + routeSettings.toString());
        this.trains = trains;

        if (getActivity() != null) {
            Log.d(LOG_TEXT, "Removing progress bar");
            this.swipeLayout.setRefreshing(false);
            redraw();
        } else {
            Log.d(LOG_TEXT, "Get activity is null");
        }
    }

    public void redraw() {
        trainTable.removeAllViews();

        drawAllTrains(trains);
    }

    private void drawAllTrains(List<Train> trains) {
        trainTable.addView(buildHeaderRow());
        for (Train t : trains) {
            if (t.getAcela() == false || routeSettings.getHideAcela() == false) {
                trainTable.addView(buildTableRow(t));
            }
        }
    }

    private TableRow buildHeaderRow() {
        TableRow trainLayout = new TableRow(getActivity());
        TableRow.LayoutParams layout = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        trainLayout.setLayoutParams(layout);
        trainLayout.addView(buildTextView("Train", true, 2));
        trainLayout.addView(buildTextView("Scheduled", true));
        trainLayout.addView(buildTextView("Estimated", true));
        trainLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        return trainLayout;
    }

    private TableRow buildTableRow(Train train) {
        TableRow trainLayout = new TableRow(getActivity());
        TableRow.LayoutParams layout = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        if (routeSettings.preferredTrainNumber != null) {
            Integer preferredTrainNumber = new Integer(routeSettings.preferredTrainNumber);
            if (preferredTrainNumber.equals(train.getNumber())) {
                trainLayout.setBackgroundColor(getResources().getColor(R.color.row_highlight));
            }
        }
        trainLayout.setLayoutParams(layout);
        trainLayout.addView(buildTextView(train.getNumber().toString(), false, 2));
        trainLayout.addView(buildTextView(train.getScheduled()));
        trainLayout.addView(buildTextView(train.getEstimated()));
        trainLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        return trainLayout;
    }

    private TextView buildTextView(String text) {
        return buildTextView(text, false);
    }

    private TextView buildTextView(String text, boolean bold) {
        return buildTextView(text, bold, 3);
    }

    private TextView buildTextView(String text, boolean bold, int width) {
        TextView textView = new TextView(getActivity());
        TableRow.LayoutParams layout = new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                width
        );
        textView.setPadding(30, 5, 30, 5);
        textView.setLayoutParams(layout);
        textView.setTextSize(20);
        textView.setTextColor(Color.rgb(0, 0, 0));
        textView.setText(text);
        if (bold) {
            Typeface openSansBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans/OpenSans-Semibold.ttf");
            textView.setTypeface(openSansBold);
        } else {
            Typeface openSansLight = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans/OpenSans-Light.ttf");
            textView.setTypeface(openSansLight);
        }

        return textView;
    }
}
