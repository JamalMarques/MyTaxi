package com.mytaxi.app.customViews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mytaxi.app.R;
import com.mytaxi.app.models.Vehicle;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopBanner extends LinearLayout {

    @BindView(R.id.iv_taxi_logo) ImageView ivTaxiLogo;
    @BindView(R.id.tv_fleet_type) TextView tvFleetType;
    @BindView(R.id.tv_address) TextView tvAddress;
    @BindView(R.id.vehicle_info_layout) ConstraintLayout vehicleInfoLayout;

    public TopBanner(Context context) {
        super(context);
        init(context);
    }

    public TopBanner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TopBanner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public TopBanner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        /*TODO -- add animation as well!! */
        inflate(context, R.layout.top_banner_layout, this);

        ButterKnife.bind(this, this);

        ivTaxiLogo.setVisibility(VISIBLE);
    }

    public void setDefaultState() {
        ivTaxiLogo.setVisibility(VISIBLE);
        vehicleInfoLayout.setVisibility(GONE);
    }

    public void setVehicleState(@NonNull Vehicle vehicle) {
        ivTaxiLogo.setVisibility(GONE);
        tvFleetType.setText(vehicle.getFleetType());
        tvAddress.setText((vehicle.getAddress() != null) ? vehicle.getAddress() : getContext().getString(R.string.address_not_found_msg));
        vehicleInfoLayout.setVisibility(VISIBLE);
    }
}
