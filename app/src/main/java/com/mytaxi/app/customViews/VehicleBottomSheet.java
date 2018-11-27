package com.mytaxi.app.customViews;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.mytaxi.app.R;
import com.mytaxi.app.adapters.VehiclesAdapter;
import com.mytaxi.app.listeners.RecyclerViewListener;
import com.mytaxi.app.models.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class VehicleBottomSheet extends LinearLayout {

    private TextView tvHeader;
    private RecyclerView recyclerView;
    private LinearLayoutManager llm;
    private VehiclesAdapter adapter;
    private LinearLayout sheetLayout, sheetHeader, errorLayout;
    private BottomSheetBehavior bottomSheetBehavior;
    private LottieAnimationView loadingAnimationView;

    public static final int DATA_STATE_POPULATED = 0;
    public static final int DATA_STATE_ERROR = 1;
    public static final int DATA_STATE_LOADING = 2;

    @IntDef({DATA_STATE_POPULATED, DATA_STATE_ERROR, DATA_STATE_LOADING})
    @interface DataState {
    }

    public VehicleBottomSheet(Context context) {
        super(context);
        init(context);
    }

    public VehicleBottomSheet(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VehicleBottomSheet(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public VehicleBottomSheet(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    /*Inflate base internal layout*/
    private void init(Context context) {
        inflate(context, R.layout.bottom_sheet_layout, this);

        /*Components*/
        tvHeader = findViewById(R.id.tv_message);
        loadingAnimationView = findViewById(R.id.error_animation_view);
        errorLayout = findViewById(R.id.error_layout);

        /*Sheet setup*/
        sheetHeader = findViewById(R.id.sheet_header);
        sheetLayout = findViewById(R.id.sheet_layout);
        bottomSheetBehavior = BottomSheetBehavior.from(sheetLayout);

        /*Recycler setup*/
        recyclerView = findViewById(R.id.rv_vehicles);
        llm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);
        adapter = new VehiclesAdapter(context, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        /*Init state*/
        setSheetState(DATA_STATE_LOADING);
    }

    public void refreshVehicles(@NonNull List<Vehicle> vehiclesList) {
        adapter.refreshItems(vehiclesList);
        setDataState((vehiclesList.size() > 0) ? DATA_STATE_POPULATED : DATA_STATE_ERROR);
    }

    public void notifyDataSetChanged(){
        adapter.notifyDataSetChanged();
    }

    public void setItemSelected(Vehicle vehicle){
        int position = adapter.getItemPosition(vehicle);
        llm.scrollToPosition(position);
        adapter.setSelectedItem(position);
    }

    @BottomSheetBehavior.State
    public int getSheetState() {
        return bottomSheetBehavior.getState();
    }

    public void setSheetState(final @BottomSheetBehavior.State int state) {
        bottomSheetBehavior.setState(state);
    }

    public void setOnHeaderClicked(@NonNull OnClickListener listener) {
        sheetHeader.setOnClickListener(listener);
    }

    public void setOnListItemClicked(RecyclerViewListener<Vehicle> listener) {
        adapter.setListener(listener);
    }

    public void setDataState(@DataState int dataState) {
        switch (dataState) {
            case DATA_STATE_POPULATED:
                recyclerView.setVisibility(VISIBLE);
                errorLayout.setVisibility(GONE);
                loadingAnimationView.setVisibility(GONE);
                tvHeader.setText(getContext().getString(R.string.sheet_header_msg_populated));
                break;

            case DATA_STATE_ERROR:
                recyclerView.setVisibility(GONE);
                errorLayout.setVisibility(VISIBLE);
                loadingAnimationView.setVisibility(GONE);
                tvHeader.setText(getContext().getString(R.string.sheet_header_msg_error));
                break;

            case DATA_STATE_LOADING:
                recyclerView.setVisibility(GONE);
                errorLayout.setVisibility(GONE);
                loadingAnimationView.setVisibility(VISIBLE);
                tvHeader.setText(getContext().getString(R.string.sheet_header_msg_loading));
                break;
        }
    }
}
