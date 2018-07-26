package com.example.user2.fuelcalc.ui;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user2.fuelcalc.R;
import com.example.user2.fuelcalc.model.FuelType;

import java.util.List;

public class FuelListAdapter extends RecyclerView.Adapter <FuelListAdapter.FuelViewHolder> {

    private List<FuelType> items;

    public FuelListAdapter(List<FuelType> fitems) {

        this.items = fitems;
    }

    @NonNull
    @Override
    public FuelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View tv = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new FuelViewHolder(tv);
    }

    public void update(List<FuelType> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull FuelViewHolder holder, int position) {

        holder.fuelNameTextView.setText(items.get(position).getName());
        holder.fuelVolTextView.setText(items.get(position).getResVolume().toString());
        holder.fuelUnitTextView.setText(items.get(position).getUnitName());
    }

    @Override
    public int getItemCount() {

        return items.size();
    }

    public static class FuelViewHolder extends RecyclerView.ViewHolder {

        public TextView fuelNameTextView;
        public TextView fuelVolTextView;
        public TextView fuelUnitTextView;

        public FuelViewHolder(View itemView) {
            super(itemView);
            fuelNameTextView = itemView.findViewById(R.id.fuelName);
            fuelVolTextView = itemView.findViewById(R.id.fuelVol);
            fuelUnitTextView = itemView.findViewById(R.id.unit_name);
        }
    }

}