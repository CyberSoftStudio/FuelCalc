package com.example.user2.fuelcalc.mvp;

import com.example.user2.fuelcalc.fuels.FuelType;

import java.util.List;

public class FuelPresenterImpl implements FuelPresenter {

    private FuelView fuelView;
    private FuelModel fuelModel;
    private int baseFuelIndex = 0;

    public FuelPresenterImpl(FuelView v) {
        fuelView = v;
        fuelModel = new FuelModelImpl();

        calcNewVolAndUpdateView(baseFuelIndex, fuelModel.getFuelTypes());
    }


    private void calcNewVolAndUpdateView(int baseFuelIndex, List<FuelType> fuelTypes) {

        fuelModel.calculateNewVolumes(baseFuelIndex, fuelView.getBaseFuelVolume());

        fuelView.update(fuelTypes, baseFuelIndex, fuelTypes.get(baseFuelIndex).getName(),
                fuelTypes.get(baseFuelIndex).getUnitName());
    }


    @Override
    public void calcNewVolAndUpdateView() {
        calcNewVolAndUpdateView(baseFuelIndex, fuelModel.getFuelTypes());
    }


    @Override
    public void onDestroy() {
        fuelModel.saveData();
        fuelModel = null;
        fuelView = null;
    }

    @Override
    public void processClick(String newBaseFuelName) {

        List<FuelType> fuelTypes = fuelModel.getFuelTypes();

        baseFuelIndex = 0;
        for (int i = 0; i < fuelTypes.size(); ++i) {

            if (newBaseFuelName.equals(fuelTypes.get(i).getName())) {
                baseFuelIndex = i;
            }
        }

        calcNewVolAndUpdateView(baseFuelIndex, fuelTypes);
    }
}
