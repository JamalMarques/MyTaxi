package com.mytaxi.app.customViews;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.mytaxi.app.R;
import com.mytaxi.app.models.Vehicle;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopBanner extends LinearLayout {

    private final int showDialogAnimationRes = R.anim.translate_top_center;
    private final int hideDialogAnimationRes = R.anim.translate_center_top;

    @BindView(R.id.iv_taxi_logo) ImageView ivTaxiLogo;
    @BindView(R.id.tv_fleet_type) TextView tvFleetType;
    @BindView(R.id.tv_address) TextView tvAddress;
    @BindView(R.id.vehicle_info_layout) ConstraintLayout vehicleInfoLayout;
    @BindView(R.id.anim_loading_view) LottieAnimationView animView;

    private static final int STATE_DEFAULT = 0;
    private static final int STATE_POPULATED = 1;

    @IntDef({STATE_DEFAULT, STATE_POPULATED})
    @interface State {

    }

    private @State int actualState = STATE_DEFAULT;

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
        inflate(context, R.layout.top_banner_layout, this);

        ButterKnife.bind(this, this);

        setDefaultState();
    }

    public void setDefaultState() {

        if (actualState == STATE_DEFAULT) {
            return;
        }

        /*Update state*/
        actualState = STATE_DEFAULT;

        /*Animation Setup*/
        Animation animationOut = AnimationUtils.loadAnimation(getContext(), hideDialogAnimationRes);
        Animation animationIn = AnimationUtils.loadAnimation(getContext(), showDialogAnimationRes);

        animationOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivTaxiLogo.setVisibility(VISIBLE);
                vehicleInfoLayout.setVisibility(GONE);
                startAnimation(animationIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(animationOut);
    }

    public void setVehicleState(@NonNull Vehicle vehicle, boolean animate) {
        /*Update state*/
        actualState = STATE_POPULATED;

        if (animate) {
            /*Animation Setup*/
            Animation animationOut = AnimationUtils.loadAnimation(getContext(), hideDialogAnimationRes);
            Animation animationIn = AnimationUtils.loadAnimation(getContext(), showDialogAnimationRes);

            animationOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    renderVehicleData(vehicle);
                    startAnimation(animationIn);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            startAnimation(animationOut);
        } else {
            /*Normal Setup*/
            renderVehicleData(vehicle);
        }
    }

    private void renderVehicleData(Vehicle vehicle) {
        ivTaxiLogo.setVisibility(GONE);
        tvFleetType.setText(vehicle.getFleetType());

        if (vehicle.getAddress() != null) {
            tvAddress.setText(vehicle.getAddress());
            animView.setVisibility(GONE);
            tvAddress.setVisibility(VISIBLE);
        } else {
            tvAddress.setVisibility(GONE);
            animView.setVisibility(VISIBLE);
        }

        vehicleInfoLayout.setVisibility(VISIBLE);
    }
}
