package com.example.user2.fuelcalc.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user2.fuelcalc.R;
import com.example.user2.fuelcalc.fuels.FuelType;
import com.example.user2.fuelcalc.fuels.RealmFuelType;
import com.example.user2.fuelcalc.mvp.FuelPresenter;
import com.example.user2.fuelcalc.mvp.FuelPresenterImpl;
import com.example.user2.fuelcalc.mvp.FuelView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements FuelView {

    private static final String LOGTAG = "MainActivity";

    RecyclerView recyclerView;
    FuelListAdapter recyclerAdapter;
    FuelPresenter fuelPresenter;
    boolean darkThemeOn = false;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.e(LOGTAG, "onCreate");

        prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        darkThemeOn = prefs.getBoolean("night_mode", false);
        if (darkThemeOn){
            setTheme(R.style.DarkTheme);

        } else {
            setTheme(R.style.LightTheme);
        }

        setContentView(R.layout.activity_main);
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
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.e(LOGTAG, "onStart");
        prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        boolean settingsNewTheme = prefs.getBoolean("night_mode", false);
        if (darkThemeOn != settingsNewTheme){
            this.recreate();
            recyclerAdapter.isNightMode(darkThemeOn);
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
        getMenuInflater().inflate(R.menu.main, menu);
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
    public void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void onClick(View view) {
        TextView curFuelNameTextView = view.findViewById(R.id.fuelName);
        String curFuelName = curFuelNameTextView.getText().toString();

        fuelPresenter.processClick(curFuelName);
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
        ViewGroup parentLayout = (ViewGroup) v.getParent().getParent();

        TextView tv = parentLayout.findViewById(R.id.fuelName);
        String fuelName = tv.getText().toString();
        fuelPresenter.processDeleteButnClick(fuelName, recyclerAdapter.getExpanded());
    }

}
