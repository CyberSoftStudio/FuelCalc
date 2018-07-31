package com.example.user2.fuelcalc.ui;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user2.fuelcalc.R;
import com.example.user2.fuelcalc.fuels.FuelType;

import java.util.ArrayList;
import java.util.List;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

public class FuelListAdapter extends RecyclerView.Adapter<FuelListAdapter.FuelViewHolder> {

    private List<FuelType> items;
    private int basePosition = -1;
    private List<Boolean> expanded;
    private int lastExpandedPosition = -1;
    private static final int additionalInfoHeightDp = 85;
    private boolean playExpantionAnimation = false;
    boolean darkModeOn = false;

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

    public void isNightMode(boolean isOn) {
        darkModeOn = isOn;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final FuelViewHolder holder, final int position) {
        holder.fuelNameTextView.setText(items.get(position).getName());
        holder.fuelVolTextView.setText(items.get(position).getResVolume().toString());
        holder.fuelUnitTextView.setText(items.get(position).getUnitName());
        holder.caloricityTextView.setText("Caloricity: " + items.get(position).getBaseCaloricity());
        holder.priceTextView.setText("Price: " + items.get(position).getPricePerBW());

        switch (holder.fuelNameTextView.getText().toString()) {
            case "Diesel":
                holder.fuelIcon.setImageResource(R.drawable.diesel);
                break;
            case "Wood":
                holder.fuelIcon.setImageResource(R.drawable.wood);
                break;
            case "Electricity":
                holder.fuelIcon.setImageResource(R.drawable.electricity);
                break;
            case "CNG":
                holder.fuelIcon.setImageResource(R.drawable.cng);
                break;
            case "Coal":
                holder.fuelIcon.setImageResource(R.drawable.coal);
                break;
            case "Pellets":
                holder.fuelIcon.setImageResource(R.drawable.pellets);
                break;
            case "LPG":
                holder.fuelIcon.setImageResource(R.drawable.lpg);
                break;
            default:
                holder.fuelIcon.setImageResource(R.drawable.gaz);
                break;
        }
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        if (basePosition == position) {
            holder.rowLinearLayout.setBackgroundColor(holder.rowLinearLayout.getContext()
                    .getApplicationContext().getResources().getColor(R.color.DarkYellow));
        } else {
            if (prefs.getBoolean("night_mode", false)) {
                holder.rowLinearLayout.setBackgroundColor(holder.rowLinearLayout.getContext()
                        .getApplicationContext().getResources().getColor(R.color.Black));
                ((TextView)holder.rowLinearLayout.findViewById(R.id.fuelName)).setTextColor(R.color.White);
            } else {
                holder.rowLinearLayout.setBackgroundColor(holder.rowLinearLayout.getContext()
                        .getApplicationContext().getResources().getColor(R.color.WhiteSmoke));
            }
        }

        ImageButton imageButton = holder.rowLinearLayout.findViewById(R.id.arrow);

        DisplayMetrics displayMetrics = holder.rowLinearLayout.getContext().getResources().getDisplayMetrics();
        int px = Math.round(additionalInfoHeightDp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));


        if (expanded.get(position).equals(false)) {

            imageButton.setImageResource(R.drawable.ic_baseline_expand_more_24px);
            holder.additionalInfoLauout.setVisibility(View.GONE);

            if (position == lastExpandedPosition && playExpantionAnimation) {
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

            if (position == lastExpandedPosition && playExpantionAnimation) {
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


    public List<Boolean> getExpanded() {
        return new ArrayList<>(expanded);
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


    public static class FuelViewHolder extends RecyclerView.ViewHolder {

        public TextView fuelNameTextView;
        public ImageView fuelIcon;
        public TextView fuelVolTextView;
        public TextView fuelUnitTextView;
        public TextView caloricityTextView;
        public TextView priceTextView;
        LinearLayout rowLinearLayout;
        LinearLayout additionalInfoLauout;


        public FuelViewHolder(View itemView) {
            super(itemView);
            rowLinearLayout = itemView.findViewById(R.id.linear_layout);
            fuelNameTextView = itemView.findViewById(R.id.fuelName);
            fuelIcon = itemView.findViewById(R.id.fuelLogo);
            fuelVolTextView = itemView.findViewById(R.id.fuelVol);
            fuelUnitTextView = itemView.findViewById(R.id.unit_name);
            additionalInfoLauout = itemView.findViewById(R.id.additional_info);
            caloricityTextView = itemView.findViewById(R.id.caloricity);
            priceTextView = itemView.findViewById(R.id.price);
        }
    }


}