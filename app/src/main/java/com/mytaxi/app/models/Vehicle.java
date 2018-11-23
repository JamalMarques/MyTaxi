package com.mytaxi.app.models;

public class Vehicle {

    private int id;
    private Coordinate coordinate;
    private String fleetType;
    private Double heading;
    private String address;

    public Vehicle(int id, Coordinate coordinate, String fleetType, Double heading, String address) {
        this.id = id;
        this.coordinate = coordinate;
        this.fleetType = fleetType;
        this.heading = heading;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Double getHeading() {
        return heading;
    }

    public void setHeading(Double heading) {
        this.heading = heading;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        Vehicle vehicle = (Vehicle) obj;
        return vehicle.getId() == id;
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(id);
    }

    public boolean isTaxi(){
        return fleetType.equalsIgnoreCase("Taxi");
    }
}
