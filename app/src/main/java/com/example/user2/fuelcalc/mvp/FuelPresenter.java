package com.example.user2.fuelcalc.mvp;

import android.view.View;

import com.example.user2.fuelcalc.fuels.FuelType;

import java.util.List;

public interface FuelPresenter {

    void onDestroy();

    void processClick(String newBaseFuelName);

    void calcNewVolAndUpdateView();

    void processExpandButtonClick(String fuelName, List<Boolean> oldExtended);

    void processDeleteButnClick(String fuelName, List<Boolean> oldExtended);
}
