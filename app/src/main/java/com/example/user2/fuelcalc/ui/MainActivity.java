package com.example.user2.fuelcalc.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(this);

        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        recyclerAdapter = new FuelListAdapter(new ArrayList<FuelType>());
        recyclerView.setAdapter(recyclerAdapter);

        fuelPresenter = new FuelPresenterImpl(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        fuelPresenter.onDestroy();
    }

    @Override
    public Double getBaseFuelVolume() {
        return Double.parseDouble(((EditText)(findViewById(R.id.base_vol))).getText().toString());
    }

    @Override
    public void update(List<FuelType> fuelTypes, int baseFuelInd, String newBaseName,
                       String newBaseUnit) {

        ((TextView)(findViewById(R.id.base_fuel))).setText(newBaseName);
        ((TextView)(findViewById(R.id.base_unit))).setText(newBaseUnit);
        recyclerAdapter.basePosition = baseFuelInd;
        recyclerAdapter.update(fuelTypes);
    }

    @Override
    public void onClick(View view) {
        TextView curFuelNameTextView = view.findViewById(R.id.fuelName);
        String curFuelName = curFuelNameTextView.getText().toString();

        fuelPresenter.processClick(curFuelName);

    }
}
