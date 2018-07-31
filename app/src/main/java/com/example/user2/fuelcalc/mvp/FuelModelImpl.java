package com.example.user2.fuelcalc.mvp;

import android.util.Log;

import com.example.user2.fuelcalc.fuels.FuelType;
import com.example.user2.fuelcalc.fuels.RealmFuelType;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


public class FuelModelImpl implements FuelModel {

    private static final String LOGTAG = "FuelModel";

    private ArrayList<FuelType> fuelTypes; // Array of fuelTypes.
    private Realm realm;

    public FuelModelImpl() {
        Log.e(LOGTAG, "FuelModel Created");
        realm = Realm.getDefaultInstance();

        loadSavedData();
    }

    @Override
    public void loadSavedData() {
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
                    1, -1));
            fuelTypes.add(new FuelType("LPG", -1, "l", 1, -1.00, 1,
                    10800));
        }
    }

    @Override
    public void saveData() {

        realm.beginTransaction();

        realm.deleteAll();

        ArrayList<RealmFuelType> realmFuelTypes = new ArrayList<>();
        for (FuelType curFuelType : fuelTypes) {
            realmFuelTypes.add(curFuelType.getRealmFuelType());
        }

        realm.insert(realmFuelTypes);

        realm.commitTransaction();
    }

    private void calculateNewVolume (FuelType baseFuel, FuelType otherFuel) {
        otherFuel.setResVolume(baseFuel.getResVolume()/2);
    }

    @Override
    public void calculateNewVolumes (int baseFuelIndex, double baseFuelVolume) {

        fuelTypes.get(baseFuelIndex).setResVolume(baseFuelVolume);

        for (int i = 0; i < fuelTypes.size(); ++i) {
            if (i != baseFuelIndex) {
                calculateNewVolume(fuelTypes.get(baseFuelIndex), fuelTypes.get(i));
            }
        }
    }

    @Override
    public List<FuelType> getFuelTypes() {
        return new ArrayList<>(fuelTypes);
    }


    @Override
    public void deleteFuel(int position) {
        fuelTypes.remove(position);
    }
}
