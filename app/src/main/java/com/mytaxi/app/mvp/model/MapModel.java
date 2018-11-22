package com.mytaxi.app.mvp.model;

import android.util.Log;

import com.mytaxi.app.models.Coordinate;
import com.mytaxi.app.models.Vehicle;
import com.mytaxi.app.mvp.contract.MapContract;
import com.mytaxi.app.restApi.responses.VehicleResponse;
import com.mytaxi.app.restApi.responses.VehiclesResponse;
import com.mytaxi.app.utils.BusProvider;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapModel extends BaseModel implements MapContract.Model {

    private Coordinate pointOne;
    private Coordinate pointTwo;

    public MapModel(BusProvider.Bus bus, Coordinate pointOne, Coordinate pointTwo) {
        super(bus);
        updatePoints(pointOne, pointTwo);
    }

    @Override
    public void updatePoints(Coordinate pointOne, Coordinate pointTwo) {
        this.pointOne = pointOne;
        this.pointTwo = pointTwo;
        getVehiclesInArea(pointOne, pointTwo);
    }

    private void getVehiclesInArea(Coordinate pointOne, Coordinate pointTwo) {
        callService(getApiService().getVehiclesInArea(
                pointOne.getLatitude(), pointOne.getLongitude(), pointTwo.getLatitude(), pointTwo.getLongitude()),
                new Callback<VehiclesResponse>() {
                    @Override
                    public void onResponse(Call<VehiclesResponse> call, Response<VehiclesResponse> response) {
                        List<Vehicle> vehiclesModelList = new ArrayList<>();
                        for (VehicleResponse vehicle : response.body().getVehicles()){
                            vehiclesModelList.add(vehicle.toModel());
                        }
                        post(new OnVehiclesInAreaSucess(vehiclesModelList));
                    }

                    @Override
                    public void onFailure(Call<VehiclesResponse> call, Throwable t) {
                        post(new OnVehiclesInAreaFail(t));
                    }
                });
    }

}
