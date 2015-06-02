package com.example.tianyu.foodshuffler;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by Tianyu on 6/2/2015.
 */
public class LocationHolder implements LocationListener {
    private Location currentLocation;
    private boolean locationAvailable;

    public LocationHolder(){
        currentLocation = null;
        locationAvailable = false;
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        locationAvailable = true;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        locationAvailable = false;
    }

    public boolean isLocationAvailable(){
        return locationAvailable;
    }

    public Location getCurrentLocation(){
        return currentLocation;
    }
}
