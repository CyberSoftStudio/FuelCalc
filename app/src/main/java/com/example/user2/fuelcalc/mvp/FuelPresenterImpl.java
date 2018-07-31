package com.example.user2.fuelcalc.mvp;

import android.util.Log;

import com.example.user2.fuelcalc.fuels.FuelType;

import java.util.ArrayList;
import java.util.List;

public class FuelPresenterImpl implements FuelPresenter {

    private static final String LOGTAG = "FuelPresenter";
    private FuelView fuelView;
    private FuelModel fuelModel;
    private int baseFuelIndex = 0;

    public FuelPresenterImpl(FuelView v) {
        Log.e(LOGTAG, "PresenterCreated");
        fuelView = v;
        fuelModel = new FuelModelImpl();

        calcNewVolAndUpdateView(baseFuelIndex, fuelModel.getFuelTypes());

        ArrayList<Boolean> newExtended = new ArrayList<>(fuelModel.getFuelTypes().size());
        for (int i = 0; i < fuelModel.getFuelTypes().size(); ++i) {
            newExtended.add(false);
        }

        fuelView.updateExpanded(newExtended, -1);

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
        Log.e(LOGTAG, "onDestroy");
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

    @Override
    public void processExpandButtonClick(String fuelName, List<Boolean> oldExtended) {

        List<FuelType> fuelTypes = fuelModel.getFuelTypes();

        if (oldExtended.size() != fuelTypes.size()) {

            oldExtended = new ArrayList<Boolean>(fuelTypes.size());
            for (int i = 0; i < fuelTypes.size(); ++i) {
                oldExtended.add(false);
            }
        }

        int aimFuelIndex = 0;
        for (int i = 0; i < fuelTypes.size(); ++i) {

            if (fuelName.equals(fuelTypes.get(i).getName())) {
                aimFuelIndex = i;
            }
        }

        ArrayList<Boolean> newExtended = new ArrayList<>(oldExtended);
        newExtended.set(aimFuelIndex, !oldExtended.get(aimFuelIndex));
        fuelView.updateExpanded(newExtended, aimFuelIndex);
    }

    @Override
    public void processDeleteButnClick(String fuelName,  List<Boolean> extended) {

        List<FuelType> fuelTypes = fuelModel.getFuelTypes();

        if(fuelTypes.size() == 1) {
            fuelView.showToast("You can't delete last fuel");

            return;
        }

        int aimFuelIndex = 0;
        for (int i = 0; i < fuelTypes.size(); ++i) {

            if (fuelName.equals(fuelTypes.get(i).getName())) {
                aimFuelIndex = i;
            }
        }

        fuelTypes.remove(aimFuelIndex);

        ArrayList<Boolean> newExtended = new ArrayList<>(extended);
        newExtended.remove(aimFuelIndex);

        fuelModel.deleteFuel(aimFuelIndex);
        fuelView.updateExpanded(newExtended, -1);

        calcNewVolAndUpdateView(0, fuelTypes);
    }
}
