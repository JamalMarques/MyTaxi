package com.mytaxi.app.customViews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.mytaxi.app.R;
import com.mytaxi.app.adapters.VehiclesAdapter;
import com.mytaxi.app.listeners.RecyclerViewListener;
import com.mytaxi.app.models.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class VehicleBottomSheet extends LinearLayout {

    private RecyclerView recyclerView;
    private VehiclesAdapter adapter;

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

        recyclerView = findViewById(R.id.rv_vehicles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new VehiclesAdapter(context, new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }

    public void refreshVehicles(List<Vehicle> vehiclesList){
        adapter.refreshItems(vehiclesList);
    }

    public void setRecyclerListener(RecyclerViewListener<Vehicle> listener){
        adapter.setListener(listener);
    }
}
