package com.mytaxi.app.restApi.responses;

import com.google.gson.annotations.SerializedName;
import com.mytaxi.app.models.Coordinate;
import com.mytaxi.app.models.Vehicle;

public class VehicleResponse {

    @SerializedName("id")
    private String id;
    @SerializedName("coordinate")
    private Coordinate coordinate;
    @SerializedName("fleetType")
    private String fleetType;
    @SerializedName("heading")
    private String heading;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public String getFleetType() {
        return fleetType;
    }

    public void setFleetType(String fleetType) {
        this.fleetType = fleetType;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public Vehicle toModel(){
        return new Vehicle(id, coordinate, fleetType, heading);
    }
}
