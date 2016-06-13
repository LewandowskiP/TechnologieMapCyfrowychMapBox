package com.example.arravilar.firstapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;

/**
 * Created by Arravilar on 12.06.2016.
 */
public class MarkerTypeDialogFragment extends DialogFragment {
    private LatLng coords = new LatLng();

    static MarkerTypeDialogFragment newInstance(LatLng point){
        MarkerTypeDialogFragment x = new MarkerTypeDialogFragment();
        Bundle args = new Bundle();
        args.putDouble("Lat",point.getLatitude());
        args.putDouble("Lon",point.getLongitude());
        x.setArguments(args);
        return x;
    }

    public interface MarkerTypeDialogListener {
        public void onMarkerTypePositiveClick(DialogFragment dialog); //END
        public void onMarkerTypeNegativeClick(DialogFragment dialog); //START
    }

    // Use this instance of the interface to deliver action events
    MarkerTypeDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (MarkerTypeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        coords.setLatitude(getArguments().getDouble("Lat"));
        coords.setLongitude(getArguments().getDouble("Lon"));


        // Use the Builder class for convenient dialog construction
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_marker_type_caption)
                .setMessage("Lat: "+coords.getLatitude() +"\nLon: "+coords.getLongitude())
                .setPositiveButton(R.string.dialog_marker_type_destination, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onMarkerTypePositiveClick(MarkerTypeDialogFragment.this);

                    }
                })
                .setNegativeButton(R.string.dialog_marker_type_start, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onMarkerTypeNegativeClick(MarkerTypeDialogFragment.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

