package com.mytaxi.app.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.mytaxi.app.R;
import com.mytaxi.app.listeners.RecyclerViewListener;
import com.mytaxi.app.models.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class VehiclesAdapter extends RecyclerView.Adapter<VehiclesAdapter.ViewHolder> {

    private Context context;
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

        holder.tvUserName.setText(vehicle.getId());

        /*Glide.with(context)
                .load(user.getPicture().getThumbnail())
                .into(holder.ivUser);*/

        setAnimation(holder.cardLayout, position);
    }

    @Override
    public int getItemCount() {
        return vehiclesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardLayout;
        TextView tvUserName;

        public ViewHolder(View itemView, RecyclerViewListener listener) {
            super(itemView);

            cardLayout = itemView.findViewById(R.id.card_layout);
            tvUserName = itemView.findViewById(R.id.tv_vehicle_id);

            cardLayout.setOnClickListener(v -> {
                if (listener != null)
                    listener.recyclerViewOnItemClickListener(v, getLayoutPosition(), vehiclesList.get(getLayoutPosition()));
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
