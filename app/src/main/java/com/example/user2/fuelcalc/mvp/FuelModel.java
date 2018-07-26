package com.example.user2.fuelcalc.mvp;

import com.example.user2.fuelcalc.fuels.FuelType;

import java.util.List;

public interface FuelModel {

    void loadSavedData();

    void saveData();

    void calculateNewVolumes(int baseFuelIndex, double baseFuelVolume);

    List<FuelType> getFuelTypes ();
}
