package com.mytaxi.app.mvp.view;

import com.mytaxi.app.base.BaseActivity;
import com.mytaxi.app.mvp.contract.MapContract;
import com.mytaxi.app.utils.BusProvider;

public class MapView extends BaseView implements MapContract.View {

    public MapView(BaseActivity activity, BusProvider.Bus bus) {
        super(activity, bus);
    }
}
