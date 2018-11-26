package com.mytaxi.app.mvp.presenter;

import android.os.Bundle;

import com.mytaxi.app.models.Vehicle;
import com.mytaxi.app.mvp.contract.MapContract;

import org.greenrobot.eventbus.Subscribe;

public class MapPresenter extends BasePresenter<MapContract.View, MapContract.Model> implements MapContract.Presenter {

    public MapPresenter(MapContract.View view, MapContract.Model model) {
        super(view, model);
    }

    @Override
    public void onStart() {
        view.onStartMap();
    }

    @Override
    public void onResume() {
        view.onResumeMap();
    }

    @Override
    public void onPause() {
        view.onPauseMap();
    }

    @Override
    public void onStop() {
        view.onStopMap();
    }

    @Override
    public void onDestroy() {
        view.onDestroyMap();
        super.onDestroy();
    }

    @Override
    public void onSaveInstantState(Bundle bundle) {
        view.onSaveInstantStateMap(bundle);
    }

    @Override
    public void onLowMemory() {
        view.onLowMemoryMap();
    }

    @Subscribe
    public void onMapLoaded(MapContract.View.OnMapLoaded event) {
        view.setVisibleRegion(model.getLatestBounds());
    }

    @Subscribe
    public void onCameraMovedByUser(MapContract.View.OnCameraMovedByUser event) {
        model.updatePoints(event.getNewBounds());
        view.setLoadingVehiclesState();
    }

    @Subscribe
    public void onVehicleItemClicked(MapContract.View.OnVehicleItemClicked event) {
        Vehicle vehicle = event.getVehicleCLicked();
        if (vehicle.getAddress() != null) {
            view.showBannerTopInfo(vehicle, true);
        }else {
            model.obtainReadableAddress(vehicle);
        }
    }

    @Subscribe
    public void onMarkClicked(MapContract.View.OnMarkerClicked event) {
        Vehicle vehicle = model.getVehicleFromMarker(event.getMarker());
        if (vehicle.getAddress() != null) {
            view.showBannerTopInfo(vehicle, true);
        }else {
            model.obtainReadableAddress(vehicle);
        }
    }

    @Subscribe
    public void onAddressObtained(MapContract.Model.OnAddressObtained event){
        view.showBannerTopInfo(event.getVehicleUpdated(), false);
    }

    @Subscribe
    public void onBottomSheetItemClicked(MapContract.View.OnBottomSheetVehicleClicked event){
        view.animateCameraToMarker(model.getMarkerFromVehicle(event.getVehicleClicked()));
    }

    @Subscribe
    public void onRequestNewMarkers(MapContract.Model.OnRequestNewMarkers event){
        model.addVehicles(view.generateMapMarkers(event.getVehicles()));
        view.refreshVehiclesComponent(model.getCurrentVehicles());
    }

    @Subscribe
    public void onVehicleInAreaSuccess(MapContract.Model.OnVehiclesInAreaSuccess event) {
        view.refreshVehiclesComponent(event.getVehicles());
    }

    @Subscribe
    public void onVehicleInAreaFail(MapContract.Model.OnVehiclesInAreaFail event) {
        /*TODO - check what to do here*/
    }
}
