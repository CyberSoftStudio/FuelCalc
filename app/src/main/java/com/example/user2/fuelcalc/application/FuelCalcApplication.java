package com.example.user2.fuelcalc.application;


import android.app.Application;

import io.realm.Realm;
public class FuelCalcApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}