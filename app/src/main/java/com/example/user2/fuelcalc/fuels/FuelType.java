package com.example.user2.fuelcalc.fuels;

public class FuelType {

    private String name;
    private Double resVolume;
    private String unitName;
    private Double baseWeight;
    private Double pricePerBW;
    private Double baseVolume;
    private Double baseCaloricity;

    public FuelType() {
        name = "NONAME";
        resVolume = -1.0;
        unitName = "NONAME";
        baseWeight = -1.0;
        pricePerBW = -1.0;
        baseVolume = -1.0;
        baseCaloricity = -1.0;
    }


    public FuelType(String n, String unitN, double bw, double ppbw, double bv, double bc) {

        name = n;
        resVolume = -1.0;
        unitName = unitN;
        baseWeight = bw;
        pricePerBW = ppbw;
        baseVolume = bv;
        baseCaloricity = bc;
    }


    public FuelType(String n, double resV, String unitN, double bw, double ppbw, double bv, double bc) {

        name = n;
        resVolume = resV;
        unitName = unitN;
        baseWeight = bw;
        pricePerBW = ppbw;
        baseVolume = bv;
        baseCaloricity = bc;
    }


    public RealmFuelType getRealmFuelType() {
        return new RealmFuelType(name, resVolume, unitName, baseWeight, pricePerBW, baseVolume,
                baseCaloricity);
    }


    public Double getBaseWeight() {
        return baseWeight;
    }

    public Double getPricePerBW() {
        return pricePerBW;
    }

    public Double getBaseVolume() {
        return baseVolume;
    }

    public Double getBaseCaloricity() {
        return baseCaloricity;
    }

    public String getName() {
        return name;
    }

    public Double getResVolume() {
        return resVolume;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setResVolume(Double resVolume) {
        this.resVolume = resVolume;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public void setBaseWeight(Double baseWeight) {
        this.baseWeight = baseWeight;
    }

    public void setPricePerBW(Double pricePerBW) {
        this.pricePerBW = pricePerBW;
    }

    public void setBaseVolume(Double baseVolume) {
        this.baseVolume = baseVolume;
    }

    public void setBaseCaloricity(Double baseCaloricity) {
        this.baseCaloricity = baseCaloricity;
    }

}
