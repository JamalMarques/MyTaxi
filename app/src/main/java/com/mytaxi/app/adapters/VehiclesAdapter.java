package com.mytaxi.app.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.mytaxi.app.R;
import com.mytaxi.app.listeners.RecyclerViewListener;
import com.mytaxi.app.models.Vehicle;

import java.util.List;

public class VehiclesAdapter extends RecyclerView.Adapter<VehiclesAdapter.ViewHolder> {

    private final int HEADING_LEVEL_1_MAX = 100;
    private final int HEADING_LEVEL_2_MAX = 200;

    private Context context;
    private int positionSelected = -1;
    private int lastPositionAnimated = -1;
    private RecyclerViewListener<Vehicle> listener = (view, position, user) -> { /*Empty*/};

    /*Data*/
    private List<Vehicle> vehiclesList;

    public VehiclesAdapter(Context context, List<Vehicle> vehiclesList) {
        this.context = context;
        this.vehiclesList = vehiclesList;
    }

    public void setListener(RecyclerViewListener<Vehicle> listener) {
        this.listener = listener;
    }

    public RecyclerViewListener getListener() {
        return listener;
    }

    public void refreshItems(List<Vehicle> vehiclesList) {
        this.vehiclesList.clear();
        this.vehiclesList.addAll(vehiclesList);
        notifyDataSetChanged();
    }

    public int getItemPosition(Vehicle vehicle) {
        for (int i = 0; i < vehiclesList.size(); i++) {
            if (vehiclesList.get(i).getId() == vehicle.getId()) {
                return i;
            }
        }
        return 0;
    }

    public void setSelectedItem(int position) {
        this.positionSelected = position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicles_item_recycler_layout, parent, false),
                listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vehicle vehicle = vehiclesList.get(position);

        holder.tvVehicleFleetType.setText(vehicle.getFleetType());

        /*Address*/
        holder.tvAddress.setText((vehicle.getAddress() == null) ? context.getString(R.string.touch_to_load_address) : vehicle.getAddress());
        holder.tvAddress.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                context.getResources().getDimension((vehicle.getAddress() == null) ? R.dimen.not_address_size : R.dimen.address_size));
        holder.tvAddress.setTextColor(ContextCompat.getColor(context, (vehicle.getAddress() == null) ? R.color.not_address_color : R.color.address_color));

        /*Selected state*/
        @ColorRes int cardColorState = (positionSelected == position) ? R.color.state_selected_bg : R.color.state_not_selected_bg;
        holder.cardLayout.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, cardColorState)));

        /*Heading*/
        Double heading = vehicle.getHeading();
        @ColorRes int colorRes = (heading < HEADING_LEVEL_1_MAX) ? R.color.heading_level_1 :
                (heading < HEADING_LEVEL_2_MAX) ? R.color.heading_level_2 : R.color.heading_level_3;
        holder.headingState.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, colorRes)));

        setAnimation(holder.cardLayout, position);
    }

    @Override
    public int getItemCount() {
        return vehiclesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardLayout;
        TextView tvVehicleFleetType;
        TextView tvHeadingMsg;
        AppCompatImageView headingState;
        TextView tvAddress;

        public ViewHolder(View itemView, RecyclerViewListener listener) {
            super(itemView);

            cardLayout = itemView.findViewById(R.id.card_layout);
            tvVehicleFleetType = itemView.findViewById(R.id.tv_vehicle_fleettype);
            tvHeadingMsg = itemView.findViewById(R.id.tv_heading_msg);
            headingState = itemView.findViewById(R.id.view_heading);
            tvAddress = itemView.findViewById(R.id.tv_vehicle_address);

            cardLayout.setOnClickListener(v -> {
                if (listener != null) {
                    positionSelected = getLayoutPosition();
                    listener.recyclerViewOnItemClickListener(v, getLayoutPosition(), vehiclesList.get(getLayoutPosition()));
                }
            });
        }

        public void clearAnimation() {
            itemView.clearAnimation();
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        holder.clearAnimation();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPositionAnimated) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.recycler_animation);
            viewToAnimate.startAnimation(animation);
            lastPositionAnimated = position;
        }
    }

}
