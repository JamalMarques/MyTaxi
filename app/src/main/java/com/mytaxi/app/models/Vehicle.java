package com.mytaxi.app.models;

public class Vehicle {

    private String id;
    private Coordinate coordinate;
    private String fleetType;
    private String heading;

    public Vehicle(String id, Coordinate coordinate, String fleetType, String heading) {
        this.id = id;
        this.coordinate = coordinate;
        this.fleetType = fleetType;
        this.heading = heading;
    }

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
}
