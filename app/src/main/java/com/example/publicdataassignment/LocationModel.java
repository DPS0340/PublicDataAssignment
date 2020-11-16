package com.example.publicdataassignment;

public class LocationModel {
    private double latitude;
    private double longitude;

    public float getLatitude() {
        return (float)latitude;
    }

    public float getLongitude() {
        return (float)longitude;
    }

    public LocationModel(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
