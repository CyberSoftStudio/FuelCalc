package com.example.user2.fuelcalc.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user2.fuelcalc.R;

public class AddFuelDialogFragment extends DialogFragment {

    public interface AddFuelDialogListener {
        void onAddFuelDialogPositiveClick(DialogFragment dialog, String fuelName, String unitName,
                                          String caloricity, String price);

        void onAddFuelDialogNegativeClick(DialogFragment dialog);
    }

    AddFuelDialogListener mListener;
    LinearLayout layout;
    EditText nameET;
    EditText unitET;
    EditText caloricityET;
    EditText priceET;
    int btnAcceptDisabledColor;
    int btnAcceptEnabledColor;


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
            mListener = (AddFuelDialogListener) activity;
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
        btnAcceptDisabledColor = getActivity().getResources().getColor(R.color.gray);
        btnAcceptEnabledColor = getActivity().getResources().getColor(R.color.black);


        layout = (LinearLayout) inflater.inflate(R.layout.dialog_add_fuel, null);

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

        final Button acceptButton = layout.findViewById(R.id.add_fuel_btn_accept);
        acceptButton.setTextColor(btnAcceptDisabledColor);
        acceptButton.setEnabled(false);

        nameET = layout.findViewById(R.id.add_fuel_name);
        unitET = layout.findViewById(R.id.add_fuel_unit);
        caloricityET = layout.findViewById(R.id.add_fuel_caloricity);
        priceET = layout.findViewById(R.id.add_fuel_price);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String fuelName = nameET.getText().toString();
                String unitName = unitET.getText().toString();
                String caloricity = caloricityET.getText().toString();
                String price = priceET.getText().toString();

                if (fuelName.equals("") || unitName.equals("") || caloricity.equals("") || price.equals("")) {
                    acceptButton.setTextColor(btnAcceptDisabledColor);
                    acceptButton.setEnabled(false);
                } else {
                    acceptButton.setTextColor(btnAcceptEnabledColor);
                    acceptButton.setEnabled(true);
                }
            }
        };

        nameET.addTextChangedListener(textWatcher);
        unitET.addTextChangedListener(textWatcher);
        caloricityET.addTextChangedListener(textWatcher);
        priceET.addTextChangedListener(textWatcher);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fuelName = nameET.getText().toString();

                String unitName = unitET.getText().toString();

                String caloricity = caloricityET.getText().toString();

                String price = priceET.getText().toString();

                mListener.onAddFuelDialogPositiveClick(AddFuelDialogFragment.this,
                        fuelName, unitName, caloricity, price);

            }
        });

        Button cancelButton = layout.findViewById(R.id.add_fuel_btn_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.onAddFuelDialogNegativeClick(AddFuelDialogFragment.this);
            }
        });
    }
}