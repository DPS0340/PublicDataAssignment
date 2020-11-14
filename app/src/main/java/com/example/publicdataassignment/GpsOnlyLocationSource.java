package com.example.publicdataassignment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.PermissionChecker;

import com.naver.maps.map.LocationSource;

import java.util.ArrayList;


public class GpsOnlyLocationSource extends Thread implements LocationSource, LocationListener {
    @NonNull
    private final Context context;
    @Nullable
    private final LocationManager locationManager;
    @Nullable
    private LocationSource.OnLocationChangedListener listener;
    // Observer 패턴 사용
    private ArrayList<Observer> observers = new ArrayList<>();

    public GpsOnlyLocationSource(@NonNull Context context) {
        this.context = context;
        locationManager =
                (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void activate(
            @NonNull LocationSource.OnLocationChangedListener listener) {
        if (locationManager == null) {
            return;
        }

        if (PermissionChecker.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PermissionChecker.PERMISSION_GRANTED
                && PermissionChecker.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PermissionChecker.PERMISSION_GRANTED) {
            Log.i("GEOAPI", "No permission");
            // 권한 요청 로직 생략
            return;
        }

        this.listener = listener;
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000, 10, (android.location.LocationListener) this);
    }

    @Override
    public void deactivate() {
        if (locationManager == null) {
            return;
        }

        listener = null;
        locationManager.removeUpdates((android.location.LocationListener) this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (listener != null) {
            listener.onLocationChanged(location);
        }
    }
}