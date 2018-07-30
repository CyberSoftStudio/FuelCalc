package com.example.user2.fuelcalc.mvp;

import android.view.View;

import com.example.user2.fuelcalc.fuels.FuelType;

import java.util.List;

public interface FuelView {

    void update(List<FuelType> fuelTypes, int baseFuelInd, String newBaseName, String newBaseUnit);

    Double getBaseFuelVolume();

    void onClick(View view);

    void updateExpanded(List<Boolean> expanded, int lastExpandedPosition);

}
