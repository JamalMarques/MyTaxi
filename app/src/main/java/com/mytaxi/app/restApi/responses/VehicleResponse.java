package com.mytaxi.app.restApi.responses;

import com.google.gson.annotations.SerializedName;
import com.mytaxi.app.models.Vehicle;

public class VehicleResponse {

    @SerializedName("id")
    private int id;
    @SerializedName("coordinate")
    private CoordinateResponse coordinate;
    @SerializedName("fleetType")
    private String fleetType;
    @SerializedName("heading")
    private Double heading;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CoordinateResponse getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(CoordinateResponse coordinate) {
        this.coordinate = coordinate;
    }

    public String getFleetType() {
        return fleetType;
    }

    public void setFleetType(String fleetType) {
        this.fleetType = fleetType;
    }

    public Double getHeading() {
        return heading;
    }

    public void setHeading(Double heading) {
        this.heading = heading;
    }

    public Vehicle toModel() {
        return new Vehicle(id, coordinate.toModel(), fleetType, heading, null);
    }
}
