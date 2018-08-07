package com.example.user2.fuelcalc.ui;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user2.fuelcalc.R;
import com.example.user2.fuelcalc.fuels.FuelType;
import com.example.user2.fuelcalc.mvp.FuelModel;
import com.example.user2.fuelcalc.mvp.FuelModelImpl;

import java.util.List;

public class SettingsActivity extends AppCompatActivity
        implements CompoundButton.OnCheckedChangeListener,
        AddFuelDialogFragment.AddFuelDialogListener,
        ConfirmResetDialogFragment.ConfirmResetDialogListener {


    private static final String LOGTAG = "SettingsActivity";
    private int oldTextColorMode;
    private int oldBackgroundColorMode;
    private int textColorMode;
    private int backgroundColorMode;

    private FuelModel fuelModel;
    Switch switcher;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ValueAnimator arrowBackAlphaAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        setContentView(R.layout.settings_activity);

        fuelModel = new FuelModelImpl();
        switcher = findViewById(R.id.switcher);
        switcher.setChecked(sharedPreferences.getBoolean("night_mode", false));
        switcher.setOnCheckedChangeListener(this);

        arrowBackAlphaAnimator = ValueAnimator.ofFloat(0f, 1f);
        arrowBackAlphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                findViewById(R.id.actonbar_icon).setAlpha((Float) animation.getAnimatedValue());
            }
        });

        arrowBackAlphaAnimator.setDuration(250);
        arrowBackAlphaAnimator.setRepeatMode(ValueAnimator.REVERSE);
        arrowBackAlphaAnimator.setRepeatCount(0);

        changeTheme(sharedPreferences.getBoolean("night_mode", false));

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void animateBase(View aimView, String attribute, int color1, int color2) {

        ValueAnimator colorAnim = ObjectAnimator.ofInt(aimView,
                attribute, color1, color2);
        colorAnim.setDuration(250);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(0);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.start();
    }

    private void animateClick(View aimView) {

        ValueAnimator colorAnim = ObjectAnimator.ofInt(aimView,
                "backgroundColor", getResources().getColor(R.color.EarlGray),
                getResources().getColor(R.color.invisible));
        colorAnim.setDuration(150);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(0);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.start();
    }

    private void showSnackbar(View background, String message) {

        Snackbar snackbar = Snackbar
                .make(background, message, Snackbar.LENGTH_SHORT);
        int snackbarTextId = android.support.design.R.id.snackbar_text;
        TextView tv = snackbar.getView().findViewById(snackbarTextId);
        tv.setTextColor(getResources().getColor(R.color.White));
        snackbar.show();
    }


    private void showToast(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }


    public void onClickAddBtn(View v) {
        animateClick(v);
        DialogFragment dialog = new AddFuelDialogFragment();
        dialog.show(getSupportFragmentManager(), "AddFuelDialogFragment");
    }


    public void onClickResetDefaultBtn(View v) {
        animateClick(v);
        DialogFragment dialog = new ConfirmResetDialogFragment();
        dialog.show(getSupportFragmentManager(), "ConfirmResetDialogFragment");
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean nightMode) {

        editor = sharedPreferences.edit();
        if (nightMode) {
            editor.putBoolean("night_mode", true);
        } else {
            editor.putBoolean("night_mode", false);
        }
        changeTheme(nightMode);
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
        showSnackbar(findViewById(R.id.settings_layout), "New fuel added");
    }


    @Override
    public void onAddFuelDialogNegativeClick(DialogFragment dialog) {
        dialog.getDialog().cancel();
    }


    @Override
    public void onConfirmResetDialogPositiveClick(DialogFragment dialog) {

        showSnackbar(findViewById(R.id.settings_layout), "Fuel list was reset to default");
        fuelModel.resetFuelTypesToDefault();
        dialog.getDialog().cancel();
    }

    @Override
    public void onConfirmResetDialogNegativeClick(DialogFragment dialog) {
        dialog.getDialog().cancel();
    }


    private void changeTheme(boolean nightMode) {

        if (nightMode) {
            oldTextColorMode = getResources().getColor(R.color.Black);
            oldBackgroundColorMode = getResources().getColor(R.color.White);
            textColorMode = getResources().getColor(R.color.White);
            backgroundColorMode = getResources().getColor(R.color.Coal);

            ((ImageView) findViewById(R.id.actonbar_icon))
                    .setImageResource(R.drawable.ic_arrow_back_white_24dp);
        } else {
            oldTextColorMode = getResources().getColor(R.color.White);
            oldBackgroundColorMode = getResources().getColor(R.color.Coal);
            textColorMode = getResources().getColor(R.color.Black);
            backgroundColorMode = getResources().getColor(R.color.White);

            ((ImageView) findViewById(R.id.actonbar_icon))
                    .setImageResource(R.drawable.ic_arrow_back_black_24dp);
        }

        animateBase(findViewById(R.id.settings_layout), "backgroundColor",
                oldBackgroundColorMode,
                backgroundColorMode);
        animateBase(findViewById(R.id.nmText), "textColor",
                oldTextColorMode,
                textColorMode);
        animateBase(findViewById(R.id.actonbar_title), "textColor",
                oldTextColorMode,
                textColorMode);
        animateBase(findViewById(R.id.reset_fuel_btn), "textColor",
                oldTextColorMode,
                textColorMode);
        animateBase(findViewById(R.id.add_fuel_btn), "textColor",
                oldTextColorMode,
                textColorMode);
        animateBase(findViewById(R.id.help_button), "textColor",
                oldTextColorMode,
                textColorMode);
        animateBase(findViewById(R.id.about_button), "textColor",
                oldTextColorMode,
                textColorMode);

        arrowBackAlphaAnimator.start();
    }

    public void onClickSwitch(View view) {
        Switch mySwitch = findViewById(R.id.switcher);
        mySwitch.setChecked(!mySwitch.isChecked());
    }

    public void onBackButtonClick(View v) {
        finish();
    }

    public void onHelpButtonClick(View v) {
        animateClick(v);

        DialogFragment dialog = new HelpDialogFragment();
        dialog.show(getSupportFragmentManager(), "HelpDialogFragment");
    }

    public void onAboutUsButtonClick(View v) {
        animateClick(v);
        DialogFragment dialog = new AboutUsDialogFragment();
        dialog.show(getSupportFragmentManager(), "aboutUsDialogFragment");
    }
}