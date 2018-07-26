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
import com.example.user2.fuelcalc.model.FuelType;
import com.example.user2.fuelcalc.model.RealmFuelType;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private static final String LOGTAG = "MainActivity";

    ArrayList<FuelType> fuelTypes; // Array of fuelTypes.
    Realm realm;
    RecyclerView recyclerView;
    FuelListAdapter recyclerAdapter;
    View prevView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        RealmResults<RealmFuelType> realmResults = realm.where(RealmFuelType.class).findAll();

        fuelTypes = new ArrayList<>();
        for (RealmFuelType curRealmFuelType : realmResults) {
            fuelTypes.add(curRealmFuelType.getFuelType());
        }

        if (fuelTypes.size() == 0) {
            Log.e(LOGTAG, "Filling with default fuel types");
            fuelTypes.add(new FuelType("NPG", -1, "m\u00B3", 1, 0.25, 1,
                    7600));
            fuelTypes.add(new FuelType("Diesel", -1, "l", 1, 1.00, 1,
                    10300));
            fuelTypes.add(new FuelType("Gas", -1, "l", 1, 1.20, 1,
                    10500));
            fuelTypes.add(new FuelType("Coal", -1, "kg", 1, 0.20, 1,
                    4000));
            fuelTypes.add(new FuelType("Wood", -1, "kg", 1, 0.03, 1,
                    2000));
            fuelTypes.add(new FuelType("Pellets", -1, "m\u00B3", 1, 0.10,
                    1, 4800));
            fuelTypes.add(new FuelType("Electricity", -1, "kWt/h", 1, 0.10,
                    1,-1));
            fuelTypes.add(new FuelType("LPG", -1, "l", 1, -1.00, 1,
                    10800));
        }

        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(this);

        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        recyclerAdapter = new FuelListAdapter(fuelTypes);
        recyclerView.setAdapter(recyclerAdapter);

        setNewBaseFuel(fuelTypes.get(0).getName(), fuelTypes.get(0).getUnitName());
    }

    @Override
    protected void onStop() {
        super.onStop();

        realm.beginTransaction();

        realm.deleteAll();

        ArrayList<RealmFuelType> realmFuelTypes = new ArrayList<>();
        for (FuelType curFuelType : fuelTypes) {
            realmFuelTypes.add(curFuelType.getRealmFuelType());
        }

        realm.insert(realmFuelTypes);

        realm.commitTransaction();
    }


    private void setNewBaseFuel (String newName, String newUnit) {

        /**
         * sets new data for base fuel
         */
        ((TextView)(findViewById(R.id.base_fuel))).setText(newName);
        ((TextView)(findViewById(R.id.base_unit))).setText(newUnit);

    }


    private void calculateNewVolume (FuelType baseFuel, FuelType otherFuel) {
        otherFuel.setResVolume(baseFuel.getResVolume()/2);
    }


    public void onClick(View view) {
        TextView curFuelNameTextView = view.findViewById(R.id.fuelName);
        String curFuelName = curFuelNameTextView.getText().toString();



        int baseFuelIndex = 0;
        for (int i = 0; i < fuelTypes.size(); ++i) {

            if (curFuelName.equals(fuelTypes.get(i).getName())) {
                baseFuelIndex = i;
            }
        }

        setNewBaseFuel(fuelTypes.get(baseFuelIndex).getName(),
                fuelTypes.get(baseFuelIndex).getUnitName());

        fuelTypes.get(baseFuelIndex).setResVolume(Double.
                parseDouble(((EditText)(findViewById(R.id.base_vol))).getText().toString()));


        for (int i = 0; i < fuelTypes.size(); ++i) {
            if (i != baseFuelIndex) {
                calculateNewVolume(fuelTypes.get(baseFuelIndex), fuelTypes.get(i));
            }
        }

        recyclerAdapter.basePosition = baseFuelIndex;

        recyclerAdapter.update(fuelTypes);

    }
}
