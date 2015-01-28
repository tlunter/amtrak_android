package com.tlunter.amtrak;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NetworkConnectivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NetworkConnectivityFragment extends Fragment {

    private OnNetworkConnectivityInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NetworkConnectivityFragment.
     */
    public static NetworkConnectivityFragment newInstance() {
        NetworkConnectivityFragment fragment = new NetworkConnectivityFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public NetworkConnectivityFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.network_connectivity_warning);
        textView.setTextSize(30);

        Button button = new Button(getActivity());
        button.setText(R.string.retry_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onButtonPressed();
            }
        });

        LinearLayout ll = new LinearLayout(getActivity());
        ll.setGravity(Gravity.CENTER);
        ll.addView(textView);
        ll.addView(button);
        return ll;
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onNetworkConnectivityInteraction();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnNetworkConnectivityInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNetworkConnectivityInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnNetworkConnectivityInteractionListener {
        public void onNetworkConnectivityInteraction();
    }
}
