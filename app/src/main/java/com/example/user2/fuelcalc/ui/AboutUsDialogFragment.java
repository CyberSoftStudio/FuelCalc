package com.example.user2.fuelcalc.ui;

import android.app.DialogFragment;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.user2.fuelcalc.R;


public class AboutUsDialogFragment extends android.support.v4.app.DialogFragment {

    View layout;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        layout = inflater.inflate(R.layout.dialog_about_us, null);

        builder.setView(layout);

        return builder.create();
    }

    @Override
    public void onStart() {

        super.onStart();
        if (getDialog() == null) {
            return;
        }

        getDialog().getWindow().setWindowAnimations(
                R.style.dialog_animation_fade);
    }

    @Override
    public void onResume() {

        super.onResume();

        layout.findViewById(R.id.about_us_dialog_close_btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        getDialog().cancel();
                    }
                });
    }



}

