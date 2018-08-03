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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.user2.fuelcalc.R;

public class AddFuelDialogFragment extends DialogFragment {

    public interface AddFuelDialogListener {
        void onAddFuelDialogPositiveClick(DialogFragment dialog, String fuelName, String unitName,
                                          String caloricity, String price);

        void onAddFuelDialogNegativeClick(DialogFragment dialog);
    }

    AddFuelDialogListener mListener;
    LinearLayout layout;

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


        layout = (LinearLayout) inflater.inflate(R.layout.dialog_add_fuel, null);

        builder.setView(layout)
                .setTitle("Add new fuel")
                .setIcon(R.drawable.gaz);

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();

        //Button acceptButton = layout.findViewById(R.id.add_fuel_btn_accept);
        /*acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fuel_name = ((EditText)layout.findViewById(R.id.add_fuel_name))
                        .getText().toString();

                String unit_name = ((EditText)layout.findViewById(R.id.add_fuel_unit))
                        .getText().toString();

                String caloricity =((EditText)layout.findViewById(R.id.add_fuel_caloricity))
                        .getText().toString();

                String price = ((EditText)layout.findViewById(R.id.add_fuel_price))
                        .getText().toString();

                mListener.onAddFuelDialogPositiveClick(AddFuelDialogFragment.this,
                        fuel_name, unit_name, caloricity, price);
            }
        });*/

        /*Button cancelButton = layout.findViewById(R.id.add_fuel_btn_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.onAddFuelDialogNegativeClick(AddFuelDialogFragment.this);
            }
        });*/
    }
}