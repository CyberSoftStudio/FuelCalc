package com.example.user2.fuelcalc.ui;


import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user2.fuelcalc.R;
import com.example.user2.fuelcalc.fuels.FuelType;

import java.util.ArrayList;
import java.util.List;

public class FuelListAdapter extends RecyclerView.Adapter<FuelListAdapter.FuelViewHolder> {

    private List<FuelType> items;
    private int basePosition = -1;
    public List<Boolean> expanded;

    public void setBasePosition(int basePosition) {
        this.basePosition = basePosition;
    }

    public FuelListAdapter(List<FuelType> fitems) {

        this.items = fitems;
        expanded = new ArrayList<>(fitems.size());
        for (int i = 0; i < expanded.size(); ++i) {
            expanded.set(i, false);
        }
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
            holder.rowLinearLayout.setBackgroundColor(holder.rowLinearLayout.getContext()
                    .getApplicationContext().getResources().getColor(R.color.DarkYellow));
        } else {
            holder.rowLinearLayout.setBackgroundColor(holder.rowLinearLayout.getContext()
                    .getApplicationContext().getResources().getColor(R.color.WhiteSmoke));
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
        LinearLayout rowLinearLayout;


        public FuelViewHolder(View itemView) {
            super(itemView);
            rowLinearLayout = itemView.findViewById(R.id.linear_layout);
            fuelNameTextView = itemView.findViewById(R.id.fuelName);
            fuelVolTextView = itemView.findViewById(R.id.fuelVol);
            fuelUnitTextView = itemView.findViewById(R.id.unit_name);
        }
    }

}