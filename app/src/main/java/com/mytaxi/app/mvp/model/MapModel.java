package com.mytaxi.app.mvp.model;

import com.mytaxi.app.mvp.contract.MapContract;
import com.mytaxi.app.utils.BusProvider;

public class MapModel extends BaseModel implements MapContract.Model {

    public MapModel(BusProvider.Bus bus) {
        super(bus);
    }
}
