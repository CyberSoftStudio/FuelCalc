package com.example.user2.fuelcalc.ui;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user2.fuelcalc.R;
import com.example.user2.fuelcalc.model.FuelType;

import java.util.List;

public class FuelListAdapter extends RecyclerView.Adapter<FuelListAdapter.FuelViewHolder> {

    private List<FuelType> items;
    public int basePosition = -1;

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

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull FuelViewHolder holder, final int position) {
        holder.fuelNameTextView.setText(items.get(position).getName());
        holder.fuelVolTextView.setText(items.get(position).getResVolume().toString());
        holder.fuelUnitTextView.setText(items.get(position).getUnitName());

        if (basePosition == position) {
            holder.row_linearlayout.setBackgroundColor(holder.row_linearlayout.getContext()
                    .getApplicationContext().getResources().getColor(R.color.DarkYellow));
        } else {
            holder.row_linearlayout.setBackgroundColor(holder.row_linearlayout.getContext()
                    .getApplicationContext().getResources().getColor(R.color.Coal));
        }

    }

    @Override
    public int getItemCount() {

        return items.size();
    }

    public static class FuelViewHolder extends RecyclerView.ViewHolder {

        public TextView fuelNameTextView;
        public TextView fuelVolTextView;
        public TextView fuelUnitTextView;
        LinearLayout row_linearlayout;


        public FuelViewHolder(View itemView) {
            super(itemView);
            row_linearlayout = itemView.findViewById(R.id.linear_layout);
            fuelNameTextView = itemView.findViewById(R.id.fuelName);
            fuelVolTextView = itemView.findViewById(R.id.fuelVol);
            fuelUnitTextView = itemView.findViewById(R.id.unit_name);
        }
    }

}