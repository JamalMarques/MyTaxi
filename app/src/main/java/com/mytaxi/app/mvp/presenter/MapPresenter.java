package com.mytaxi.app.mvp.presenter;

import android.os.Bundle;

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
    public void destroy() {
        view.onDestroyMap();
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
    public void onVehicleInAreaSuccess(MapContract.Model.OnVehiclesInAreaSucess event){
        view.refreshVehiclesComponent(event.getVehicles());
    }

    @Subscribe
    public void onVehicleInAreaFail(MapContract.Model.OnVehiclesInAreaFail event){
        /*TODO - check what to do here*/
    }
}
