package com.example.tianyu.foodshuffler;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

/**
 * Created by Tianyu on 6/5/2015.
 */
public class LocationHolder implements LocationListener {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private Context context;

    private boolean gpsEnabled = false;
    private boolean networkEnabled = false;
    private boolean locationAvailable = false;

    private Location mLocation;

    private LocationManager locationManager;

    // minimum distance change for updates
    private int MIN_TIME = 10;
    // minimum time between mLocation updates
    private int MIN_DIST = 1000;

    public LocationHolder(Context newContext) {
        Log.d(LOG_TAG,"New LocationHolder instance created");
        context = newContext;
        //Obtain a reference to locationManager under the current context. Assert that locationManager is not null
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //attempts to fix current location
        fixLocation();
    }

    private void fixLocation() {
        try {
            logAvailableProviders();

            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            //If neither gps or network can be used the shuffle action should not be initiated
            if (!gpsEnabled && !networkEnabled) {
                Log.d(LOG_TAG, "No Location Service Available");
                locationAvailable = false;
            } else {
                // now check which provider is available, gps is favored over network location.
                // we initiate a request and then attempts to read approximated location from the stored location data.
                // Here we are assuming that the last known location is somewhat accurate
                if (gpsEnabled) {
                    mLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }else if (networkEnabled) {
                    mLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                locationAvailable = true;

                //if mLocation is null, then we do not have a good approximation of the current location
                if (mLocation == null){
                    locationAvailable = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logAvailableProviders() {
        Log.d(LOG_TAG, "Logging available location providers:");
        List<String> providers =  locationManager.getAllProviders();
        for (String provider : providers) {
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                Log.d(LOG_TAG, "Available provider: " + provider);
            }
        }
        Log.d(LOG_TAG, "Done logging providers");
    }

    public void stopUsingLocation() {
        locationManager.removeUpdates(this);
    }

    public Location getmLocation(){
        if (mLocation != null) {
            return mLocation;
        }
        fixLocation();
        return mLocation;
    }

    public double getLatitude() {
        return mLocation.getLatitude();
    }

    public double getLongitude() {
        return mLocation.getLongitude();
    }

    public boolean isLocationAvailable() {
        return locationAvailable;
    }

    @Override
    //When this method receives a call back the device have obtained a recent fix on location,
    //current location is clearly available and would be updated.
    public void onLocationChanged(Location location) {
        if (location != null) {
            locationAvailable = true;
            mLocation = location;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
}

