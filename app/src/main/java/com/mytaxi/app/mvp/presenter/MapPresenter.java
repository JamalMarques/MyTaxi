package com.mytaxi.app.mvp.presenter;

import com.mytaxi.app.mvp.contract.MapContract;

public class MapPresenter extends BasePresenter<MapContract.View, MapContract.Model> implements MapContract.Presenter {

    public MapPresenter(MapContract.View view, MapContract.Model model) {
        super(view, model);
    }
}
