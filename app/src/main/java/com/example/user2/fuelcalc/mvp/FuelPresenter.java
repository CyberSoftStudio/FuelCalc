package com.example.user2.fuelcalc.mvp;

import android.view.View;

import com.example.user2.fuelcalc.fuels.FuelType;

import java.util.List;

public interface FuelPresenter {

    void onDestroy();

    void processClick(String newBaseFuelName);

    void calcNewVolAndUpdateView();
}
