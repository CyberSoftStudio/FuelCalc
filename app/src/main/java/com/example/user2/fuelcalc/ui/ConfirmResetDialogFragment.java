package com.example.user2.fuelcalc.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.user2.fuelcalc.R;

public class ConfirmResetDialogFragment extends DialogFragment {

    public interface ConfirmResetDialogListener {
        void onConfirmResetDialogPositiveClick(DialogFragment dialog);

        void onConfirmResetDialogNegativeClick(DialogFragment dialog);
    }

    ConfirmResetDialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;

        if (context instanceof Activity){
            activity = (Activity) context;
        }
        else {
            return;
        }

        try {
            mListener = (ConfirmResetDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }


    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setTitle("Reset fuel list to default")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        mListener.onConfirmResetDialogPositiveClick(ConfirmResetDialogFragment.this);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mListener.onConfirmResetDialogNegativeClick(ConfirmResetDialogFragment.this);
                    }
                });

        return builder.create();
    }
}