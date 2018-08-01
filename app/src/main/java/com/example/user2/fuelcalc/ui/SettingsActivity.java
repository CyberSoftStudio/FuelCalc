package com.example.user2.fuelcalc.ui;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.user2.fuelcalc.R;
import com.example.user2.fuelcalc.fuels.FuelType;
import com.example.user2.fuelcalc.fuels.RealmFuelType;
import com.example.user2.fuelcalc.mvp.FuelModel;
import com.example.user2.fuelcalc.mvp.FuelModelImpl;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class SettingsActivity extends AppCompatActivity {

    private static final String LOGTAG = "SettingsActivity";

    private FuelModel fuelModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        fuelModel = new FuelModelImpl();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getTitle().toString()) {
            case "Back":
                onBackPressed();
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if(findViewById(R.id.add_fuel_layout).getVisibility() == View.VISIBLE) {
            onClickAddBtnCancel(findViewById(R.id.add_fuel_btn));
        } else {
            super.onBackPressed();
        }
    }


    public void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    public void onClickAddBtn(View v) {
        View addLayout = findViewById(R.id.add_fuel_layout);
        addLayout.setVisibility(View.VISIBLE);
    }


    public void onClickAddBtnAccept(View v) {

        String fuelName = ((EditText) findViewById(R.id.fuel_name_inp_field)).getText().toString();
        String unitName = ((EditText) findViewById(R.id.unit_name_inp_field)).getText().toString();
        String caloricity = ((EditText) findViewById(R.id.caloricity_inp_field)).getText().toString();
        String price = ((EditText) findViewById(R.id.price_inp_field)).getText().toString();

        if (fuelName.equals("") || unitName.equals("") || caloricity.equals("") || price.equals("")) {
            showToast("All input fields must not be empty");
            return;
        }

        double priceDouble, caloricityDouble;

        try {
            priceDouble = Double.parseDouble(price);
            caloricityDouble = Double.parseDouble(caloricity);

        } catch (NumberFormatException e) {
            showToast("\"Caloricity\" and \"price\" fields must contain only numbers");
            return;
        }


        fuelModel.addFuel(new FuelType(fuelName, unitName, -1.0, priceDouble, -1.0,
                caloricityDouble));

        fuelModel.saveData();

        onClickAddBtnCancel(v);

        showToast("New fuel added");
    }


    public void onClickAddBtnCancel(View v) {

        View addLayout = findViewById(R.id.add_fuel_layout);
        addLayout.setVisibility(View.GONE);
        ((EditText) findViewById(R.id.fuel_name_inp_field)).setText("");
        ((EditText) findViewById(R.id.unit_name_inp_field)).setText("");
        ((EditText) findViewById(R.id.caloricity_inp_field)).setText("");
        ((EditText) findViewById(R.id.price_inp_field)).setText("");

        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) {
            Log.e(LOGTAG, e.toString());
        }
    }


    public void onClickResetDefaultBtn(View v) {

        showToast("Fuel list was reset to default");
        fuelModel.resetFuelTypesToDefault();
    }
}
