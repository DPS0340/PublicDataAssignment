package com.example.publicdataassignment;

import android.location.Location;

import com.naver.maps.map.LocationSource;


public class GpsListener implements LocationSource.OnLocationChangedListener {
    public double latitude = -1.0;
    public double longitude = -1.0;


    public GpsListener() {

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }
}
