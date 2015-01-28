package com.tlunter.amtrak;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.tlunter.amtrak.TrainListingFragment.OnTrainListingInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrainListingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrainListingFragment extends Fragment {

    private OnTrainListingInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TrainListingFragment.
     */
    public static TrainListingFragment newInstance() {
        TrainListingFragment fragment = new TrainListingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public TrainListingFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onTrainListingInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnTrainListingInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTrainListingInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnTrainListingInteractionListener {
        public void onTrainListingInteraction(Uri uri);
    }
}
