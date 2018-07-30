package com.example.user2.fuelcalc.ui;


import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user2.fuelcalc.R;
import com.example.user2.fuelcalc.fuels.FuelType;

import java.util.ArrayList;
import java.util.List;

public class FuelListAdapter extends RecyclerView.Adapter<FuelListAdapter.FuelViewHolder> {

    private List<FuelType> items;
    private int basePosition = -1;
    private List<Boolean> expanded;
    private int lastExpandedPosition = -1;
    private static final int additionalInfoHeightDp = 80;
    private boolean playExpantionAnimation = false;

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
    public void onBindViewHolder(@NonNull final FuelViewHolder holder, final int position) {
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


        ImageButton imageButton = holder.rowLinearLayout.findViewById(R.id.arrow);

        DisplayMetrics displayMetrics = holder.rowLinearLayout.getContext().getResources().getDisplayMetrics();
        int px = Math.round(additionalInfoHeightDp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));


        if (expanded.get(position).equals(false)) {

            imageButton.setImageResource(R.drawable.ic_baseline_expand_more_24px);
            holder.additionalInfoLauout.setVisibility(View.GONE);

            if(position == lastExpandedPosition && playExpantionAnimation) {
                Animation animation = new SlideAnimationDown(holder.additionalInfoLauout,
                        px, 0);
                holder.additionalInfoLauout.getLayoutParams().height = px;
                holder.additionalInfoLauout.setVisibility(View.VISIBLE);
                animation.setInterpolator(new AccelerateInterpolator());
                animation.setDuration(400);
                holder.additionalInfoLauout.setAnimation(animation);
                holder.additionalInfoLauout.startAnimation(animation);

                playExpantionAnimation = false;
            }
        } else {

            imageButton.setImageResource(R.drawable.ic_baseline_expand_less_24px);
            holder.additionalInfoLauout.setVisibility(View.VISIBLE);

            if(position == lastExpandedPosition && playExpantionAnimation) {
                Animation animation = new SlideAnimationDown(holder.additionalInfoLauout,
                        0, px);
                holder.additionalInfoLauout.getLayoutParams().height = 0;
                holder.additionalInfoLauout.setVisibility(View.VISIBLE);
                animation.setInterpolator(new AccelerateInterpolator());
                animation.setDuration(400);
                holder.additionalInfoLauout.setAnimation(animation);
                holder.additionalInfoLauout.startAnimation(animation);

                playExpantionAnimation = false;
            }
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
        LinearLayout additionalInfoLauout;


        public FuelViewHolder(View itemView) {
            super(itemView);
            rowLinearLayout = itemView.findViewById(R.id.linear_layout);
            fuelNameTextView = itemView.findViewById(R.id.fuelName);
            fuelVolTextView = itemView.findViewById(R.id.fuelVol);
            fuelUnitTextView = itemView.findViewById(R.id.unit_name);
            additionalInfoLauout = itemView.findViewById(R.id.additional_info);
        }
    }

    public List<Boolean> getExpanded() {
        return expanded;
    }

    public void updateExpanded(List<Boolean> newExpanded) {
        expanded.clear();
        expanded.addAll(newExpanded);
        playExpantionAnimation = true;
        notifyDataSetChanged();
    }

    public void setLastExpandedPosition(int lastExpandedPosition) {
        this.lastExpandedPosition = lastExpandedPosition;
    }

}