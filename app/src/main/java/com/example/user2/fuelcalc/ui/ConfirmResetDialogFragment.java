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
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.user2.fuelcalc.R;

public class ConfirmResetDialogFragment extends DialogFragment {

    public interface ConfirmResetDialogListener {
        void onConfirmResetDialogPositiveClick(DialogFragment dialog);

        void onConfirmResetDialogNegativeClick(DialogFragment dialog);
    }

    ConfirmResetDialogListener mListener;
    LinearLayout layout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;

        if (context instanceof Activity) {
            activity = (Activity) context;
        } else {
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

        LayoutInflater inflater = getActivity().getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        layout = (LinearLayout) inflater.inflate(R.layout.dialog_reset_fuels, null);

        builder.setView(layout);

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();

        layout.findViewById(R.id.dialog_reset_yes_btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mListener.onConfirmResetDialogPositiveClick(ConfirmResetDialogFragment.this);
                    }
                });


        layout.findViewById(R.id.dialog_reset_no_btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mListener.onConfirmResetDialogNegativeClick(ConfirmResetDialogFragment.this);
                    }
                });
    }
}