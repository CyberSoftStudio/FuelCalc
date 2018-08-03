package com.example.user2.fuelcalc.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user2.fuelcalc.R;
import com.example.user2.fuelcalc.fuels.FuelType;
import com.example.user2.fuelcalc.fuels.RealmFuelType;
import com.example.user2.fuelcalc.mvp.FuelModel;
import com.example.user2.fuelcalc.mvp.FuelModelImpl;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class SettingsActivity extends AppCompatActivity
        implements CompoundButton.OnCheckedChangeListener,
        AddFuelDialogFragment.AddFuelDialogListener,
        ConfirmResetDialogFragment.ConfirmResetDialogListener {


    private static final String LOGTAG = "SettingsActivity";

    private FuelModel fuelModel;
    Switch switcher;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sharedPreferences.getBoolean("night_mode", false)) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.LightTheme);
        }
        setContentView(R.layout.settings_activity);
        if (sharedPreferences.getBoolean("night_mode", false)) {
            findViewById(R.id.settings_layout).setBackgroundColor(getResources().getColor(R.color.Coal));

        } else {
            findViewById(R.id.settings_layout).setBackgroundColor(getResources().getColor(R.color.White));
        }
        fuelModel = new FuelModelImpl();
        switcher = findViewById(R.id.switcher);
        switcher.setChecked(sharedPreferences.getBoolean("night_mode", false));
        switcher.setOnCheckedChangeListener(this);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e(LOGTAG, e.toString());
        }
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    public void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    public void onClickAddBtn(View v) {

        DialogFragment dialog = new AddFuelDialogFragment();
        dialog.show(getSupportFragmentManager(), "AddFuelDialogFragment");
    }


    public void onClickResetDefaultBtn(View v) {

        DialogFragment dialog = new ConfirmResetDialogFragment();
        dialog.show(getSupportFragmentManager(), "ConfirmResetDialogFragment");
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        editor = sharedPreferences.edit();

        if (b) {
            Toast.makeText(this, "NightMode", Toast.LENGTH_SHORT).show();
            editor.putBoolean("night_mode", true);
            recreate();
        } else {
            Toast.makeText(this, "DayMode", Toast.LENGTH_SHORT).show();
            editor.putBoolean("night_mode", false);
            recreate();
        }

        editor.apply();
    }


    @Override
    public void onAddFuelDialogPositiveClick(DialogFragment dialog, String fuelName,
                                             String unitName, String caloricity, String price) {


        if (fuelName.equals("") || unitName.equals("") || caloricity.equals("") || price.equals("")) {
            showToast("All input fields must not be empty");
            return;
        }


        List<FuelType> fuelTypes = fuelModel.getFuelTypes();
        for (FuelType curFuelType : fuelTypes) {
            if (curFuelType.getName().equals(fuelName)) {
                showToast("Fuel with this name already exists\nChoose other fuel name");
                return;
            }
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

        dialog.getDialog().cancel();

        showToast("New fuel added");
    }


    @Override
    public void onAddFuelDialogNegativeClick(DialogFragment dialog) {
        dialog.getDialog().cancel();
    }


    @Override
    public void onConfirmResetDialogPositiveClick(DialogFragment dialog) {

        showToast("Fuel list was reset to default");
        fuelModel.resetFuelTypesToDefault();
        dialog.getDialog().cancel();
    }

    @Override
    public void onConfirmResetDialogNegativeClick(DialogFragment dialog) {
        dialog.getDialog().cancel();
    }
}
