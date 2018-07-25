package com.example.user2.fuelcalc.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.user2.fuelcalc.R;
import com.example.user2.fuelcalc.model.FuelType;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    List<FuelType> fuelTypes;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fuelTypes = realm.where(FuelType.class).findAll();

        //if (fuelTypes.size() == 0) {
        //    fuelTypes.add(new FuelType("UGOL", 10, "RUBLY", 22, 42, 32, 2));
        //}

        RecyclerView list = findViewById(R.id.recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(this);

        llm.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(llm);
        list.setAdapter(new FuelListAdapter(fuelTypes));
    }

    @Override
    protected void onStop() {
        super.onStop();

        realm.beginTransaction();

        realm.deleteAll();

        realm.insert(fuelTypes);

        realm.commitTransaction();
    }
}
