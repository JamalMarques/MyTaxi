package com.mytaxi.app.mvp.presenter;

import com.mytaxi.app.mvp.contract.MapContract;

import org.greenrobot.eventbus.Subscribe;

public class MapPresenter extends BasePresenter<MapContract.View, MapContract.Model> implements MapContract.Presenter {

    public MapPresenter(MapContract.View view, MapContract.Model model) {
        super(view, model);
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
