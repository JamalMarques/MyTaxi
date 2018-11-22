package com.mytaxi.app.restApi.responses;

import com.google.gson.annotations.SerializedName;
import com.mytaxi.app.models.Vehicle;

import java.util.List;

public class VehiclesResponse {

    @SerializedName("poiList")
    private List<VehicleResponse> vehicles;

    public List<VehicleResponse> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehicleResponse> vehicles) {
        this.vehicles = vehicles;
    }
}
