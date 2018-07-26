package com.example.user2.fuelcalc.model;

import io.realm.RealmObject;

public class RealmFuelType extends RealmObject {

    private String name;
    private Double resVolume;
    private String unitName;
    private Double baseWeight;
    private Double pricePerBW;
    private Double baseVolume;
    private Double baseCaloricity;


    public RealmFuelType() {
        name = "NONAME";
        resVolume = -1.0;
        unitName = "NONAME";
        baseWeight = -1.0;
        pricePerBW = -1.0;
        baseVolume = -1.0;
        baseCaloricity = -1.0;
    }

    public RealmFuelType(String n, double resV, String unitN, double bw, double ppbw, double bv, double bc) {

        name = n;
        resVolume = resV;
        unitName = unitN;
        baseWeight = bw;
        pricePerBW = ppbw;
        baseVolume = bv;
        baseCaloricity = bc;
    }

    public FuelType getFuelType() {
        return new FuelType(name, resVolume, unitName, baseWeight, pricePerBW, baseVolume,
                baseCaloricity);
    }
}
