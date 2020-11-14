package com.example.publicdataassignment;

import android.location.Location;

import com.naver.maps.map.LocationSource;


public class GpsListener implements LocationSource.OnLocationChangedListener {
    private double latitude = -1.0;
    private double longitude = -1.0;


    public GpsListener() {

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
