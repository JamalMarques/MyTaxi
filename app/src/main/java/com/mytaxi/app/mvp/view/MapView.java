package com.mytaxi.app.mvp.view;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.mytaxi.app.R;
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
    @BindView(R.id.map_view) com.google.android.gms.maps.MapView mapView;

    private GoogleMap googleMap;

    public MapView(BaseActivity activity, BusProvider.Bus bus, Bundle savedInstantState) {
        super(activity, bus);
        ButterKnife.bind(this, activity);

        /*Map setup*/
        mapView.onCreate(savedInstantState);
        mapView.getMapAsync(googleMap -> {
            MapView.this.googleMap = googleMap;
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);

            // Add a marker in Sydney, Australia, and move the camera.
            LatLng sydney = new LatLng(-34, 151);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        });
    }


    @Override
    public void onStartMap() {
        mapView.onStart();
    }

    @Override
    public void onResumeMap() {
        mapView.onResume();
    }

    @Override
    public void onPauseMap() {
        mapView.onPause();
    }

    @Override
    public void onStopMap() {
        mapView.onStop();
    }

    @Override
    public void onDestroyMap() {
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstantStateMap(Bundle bundle) {
        mapView.onSaveInstanceState(bundle);
    }

    @Override
    public void onLowMemoryMap() {
        mapView.onLowMemory();
    }

    @Override
    public void refreshVehiclesComponent(List<Vehicle> list) {
        bottomSheet.refreshVehicles(list);
    }
}
