package com.tlunter.amtrakstatus;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by toddlunter on 12/30/14.
 */
public class Settings extends ActionBarActivity {
    private final String LOG_TEXT = "com.tlunter.amtrak.Settings";
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLinearLayout();
        setContentView(this.linearLayout);
    }

    void setupLinearLayout() {
        this.linearLayout = new LinearLayout(this);
        TextView textView = new TextView(this);
        textView.setText("Test");
        this.linearLayout.addView(textView);
    }
}
