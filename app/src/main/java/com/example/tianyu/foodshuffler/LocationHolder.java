package com.example.tianyu.foodshuffler;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
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

    //constructor method. Takes in the current application context
    public LocationHolder(Context newContext) {
        Log.d(LOG_TAG,"New LocationHolder instance created");
        context = newContext;
        //Obtain a reference to locationManager under the current context. Assert that locationManager is not null
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //attempts to fix current location
        fixLocation();
    }

    //Helper method. Writes the providers with non-null last known location to the log
    private ArrayList<String> logAvailableProviders() {
        ArrayList<String> resultList = new ArrayList<String>();
        Log.d(LOG_TAG, "Logging available location providers:");
        List<String> providers =  locationManager.getAllProviders();
        for (String provider : providers) {
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                Log.d(LOG_TAG, "Available provider: " + provider);
                resultList.add(provider);
            }
        }
        Log.d(LOG_TAG, "Done logging providers");
        return resultList;
    }

    //Helper method. Returns the best approximation based on last known location
    //Requires : all providers should have non-null last known location data
    //Ensures : Returns the best location provide in terms of time, null if none
    private String getBestProvider(ArrayList<String> providers){
        //Assert that the number of providers available would be small enough
        //Hence the naive method used here would be enough.
        //Also we are concerned more with time than accurancy since this app is about
        //"nearby" places
        long bestTime = 0;
        String bestProvider = providers;
        //here time denotes the interval from 1970 to fix, so the bigger the number the closer the fix
        for (String provider : providers){
            long time = locationManager.getLastKnownLocation(provider).getTime();
            if (time > bestTime){
                bestTime = time;
                bestProvider = provider;
            }
        }
        //Note that here bestProvider could be the empty String; this means that no location data is available
        return bestProvider;
    }


    //Helper method.
    private void fixLocation() {
        ArrayList<String> providers = logAvailableProviders();
        String bestProvider = getBestProvider(providers);
        if (bestProvider == "") {
            Log.d(LOG_TAG, "No Location Service Available");
            locationAvailable = false;
        } else {
            mLocation = locationManager.getLastKnownLocation(bestProvider);
            locationAvailable = true;
        }
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

