package com.example.arravilar.firstapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Arravilar on 12.06.2016.
 */
public class LoadFileDialogFragment extends DialogFragment {

    public interface LoadFileDialogListener {
        public void onLoadFileDialogPositiveClick(DialogFragment dialog);
        public void onLoadFileDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    LoadFileDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (LoadFileDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_load_file_caption)
                .setMessage(R.string.dialog_load_file_message)
                .setPositiveButton(R.string.dialog_load_file_file, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mListener.onLoadFileDialogPositiveClick(LoadFileDialogFragment.this);

                    }
                })
                .setNegativeButton(R.string.dialog_load_file_example, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onLoadFileDialogNegativeClick(LoadFileDialogFragment.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

