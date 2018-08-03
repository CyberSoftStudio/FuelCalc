package com.example.user2.fuelcalc.ui;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user2.fuelcalc.R;
import com.example.user2.fuelcalc.fuels.FuelType;
import com.example.user2.fuelcalc.mvp.FuelPresenter;
import com.example.user2.fuelcalc.mvp.FuelPresenterImpl;
import com.example.user2.fuelcalc.mvp.FuelView;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.BLUE;

public class MainActivity extends AppCompatActivity implements FuelView {

    private static final String LOGTAG = "MainActivity";

    private RecyclerView recyclerView;
    private FuelListAdapter recyclerAdapter;
    private FuelPresenter fuelPresenter;
    private boolean darkThemeOn = false;
    private SharedPreferences prefs;
    private View baseLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.e(LOGTAG, "onCreate");

        prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        darkThemeOn = prefs.getBoolean("night_mode", false);
        if (darkThemeOn) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.LightTheme);
        }
        setContentView(R.layout.activity_main);
        if (darkThemeOn) {
            findViewById(R.id.main_layout).setBackgroundColor(getResources().getColor(R.color.Coal));
            findViewById(R.id.base_fuel_layout).setBackgroundColor(getResources().getColor(R.color.LightCoal));
            ((TextView) findViewById(R.id.base_fuel)).setTextColor(getResources().getColor(R.color.White));
            ((TextView) findViewById(R.id.base_unit)).setTextColor(getResources().getColor(R.color.White));
        } else {
            findViewById(R.id.main_layout).setBackgroundColor(getResources().getColor(R.color.White));
        }

        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(this);

        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        recyclerAdapter = new FuelListAdapter(new ArrayList<FuelType>());
        recyclerView.setAdapter(recyclerAdapter);


        EditText baseVolume = findViewById(R.id.base_vol);
        baseVolume.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e(LOGTAG, "onTextChanged");
                fuelPresenter.calcNewVolAndUpdateView();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        baseLayout = findViewById(R.id.main_layout);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.e(LOGTAG, "onStart");


        prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        boolean settingsNewTheme = prefs.getBoolean("night_mode", false);
        if (darkThemeOn != settingsNewTheme) {
            this.recreate();
        }

        fuelPresenter = new FuelPresenterImpl(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(LOGTAG, "onStop");
        fuelPresenter.onDestroy();
        fuelPresenter = null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e(LOGTAG, "onCreateOptionsMenu");
        if (darkThemeOn) {
            getMenuInflater().inflate(R.menu.main_dark, menu);
        } else {
            getMenuInflater().inflate(R.menu.main_light, menu);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e(LOGTAG, "onOptionsItemSelected");
        switch (item.getTitle().toString()) {
            case "Settings":
                Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
                MainActivity.this.startActivity(myIntent);
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Double getBaseFuelVolume() {
        String volumeString = ((EditText) (findViewById(R.id.base_vol))).getText().toString();
        if (volumeString.equals("") || volumeString.equals(".")) {
            return 0.0;
        } else {
            return Double.parseDouble(volumeString);
        }
    }


    @Override
    public void update(List<FuelType> fuelTypes, int baseFuelInd, String newBaseName,
                       String newBaseUnit) {

        ((TextView) (findViewById(R.id.base_fuel))).setText(newBaseName);
        ((TextView) (findViewById(R.id.base_unit))).setText(newBaseUnit);
        recyclerAdapter.setBasePosition(baseFuelInd);
        recyclerAdapter.update(fuelTypes);
    }



    @Override
    public void showSnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(baseLayout, message, Snackbar.LENGTH_SHORT);

        int snackbarTextId = android.support.design.R.id.snackbar_text;
        TextView tv = snackbar.getView().findViewById(snackbarTextId);
        tv.setTextColor(getResources().getColor(R.color.White));

        snackbar.show();
    }



    @Override
    public void onClick(View view) {
        TextView curFuelNameTextView = view.findViewById(R.id.fuelName);
        String curFuelName = curFuelNameTextView.getText().toString();

        fuelPresenter.processClick(curFuelName);

        if (darkThemeOn) {

            animateBase(getResources().getColor(R.color.gray),
                    getResources().getColor(R.color.LightCoal));
        }
        else {
            animateBase(getResources().getColor(R.color.gray),
                    getResources().getColor(R.color.white));
        }
    }

    public void onClickExpandButton(View v) {
        Log.e(LOGTAG, "onClickExpand");
        ViewGroup parentLayout = (ViewGroup) v.getParent().getParent();
        TextView tv = parentLayout.findViewById(R.id.fuelName);
        String fuelName = tv.getText().toString();
        fuelPresenter.processExpandButtonClick(fuelName, recyclerAdapter.getExpanded());
    }


    @Override
    public void updateExpanded(List<Boolean> newExpanded, int lastExpandedPosition) {
        recyclerAdapter.updateExpanded(newExpanded);
        recyclerAdapter.setLastExpandedPosition(lastExpandedPosition);
    }


    public void onClickDeleteBtn(View v) {
        Log.e(LOGTAG, "onClickDelete");
        showSnackbar("Fuel deleted");
        ViewGroup parentLayout = (ViewGroup) v.getParent().getParent();

        TextView tv = parentLayout.findViewById(R.id.fuelName);
        String fuelName = tv.getText().toString();
        fuelPresenter.processDeleteButnClick(fuelName, recyclerAdapter.getExpanded());
    }

    private void animateBase(int color1, int color2) {

        View aimView = findViewById(R.id.base_fuel_layout);

        ValueAnimator colorAnim = ObjectAnimator.ofInt(aimView,
                "backgroundColor", color1, color2);
        colorAnim.setDuration(900);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(0);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.start();
    }
}
