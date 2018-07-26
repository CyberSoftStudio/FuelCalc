package com.example.user2.fuelcalc.mvp;

import android.view.View;

public interface FuelPresenter {

    void onDestroy();

    void processClick(String newBaseFuelName);
}
