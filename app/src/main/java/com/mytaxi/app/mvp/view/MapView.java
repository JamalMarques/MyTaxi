package com.mytaxi.app.mvp.view;

import android.support.v7.widget.RecyclerView;

import com.mytaxi.app.R;
import com.mytaxi.app.adapters.VehiclesAdapter;
import com.mytaxi.app.base.BaseActivity;
import com.mytaxi.app.customViews.VehicleBottomSheet;
import com.mytaxi.app.models.Vehicle;
import com.mytaxi.app.mvp.contract.MapContract;
import com.mytaxi.app.utils.BusProvider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapView extends BaseView implements MapContract.View {

    @BindView(R.id.vehicle_bottom_sheet) VehicleBottomSheet bottomSheet;

    public MapView(BaseActivity activity, BusProvider.Bus bus) {
        super(activity, bus);
        ButterKnife.bind(this, activity);
    }

    @Override
    public void refreshVehiclesComponent(List<Vehicle> list) {
        bottomSheet.refreshVehicles(list);
    }
}
