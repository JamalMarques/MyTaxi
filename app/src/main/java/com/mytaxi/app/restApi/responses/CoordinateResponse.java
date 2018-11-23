package com.mytaxi.app.restApi.responses;

import com.google.gson.annotations.SerializedName;
import com.mytaxi.app.models.Coordinate;

public class CoordinateResponse {

    @SerializedName("latitude")
    private Double latitude;
    @SerializedName("longitude")
    private Double longitude;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Coordinate toModel(){
        return new Coordinate(latitude, longitude);
    }

    @Override
    public String toString() {
        return latitude + "," + longitude;
    }

}
