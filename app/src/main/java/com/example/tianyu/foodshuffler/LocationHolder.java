package com.example.tianyu.foodshuffler;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Tianyu on 6/5/2015.
 */
public class LocationHolder implements LocationListener,GoogleApiClient.ConnectionCallbacks{

    //Constants for switching between Google Services and Device Services
    public static final long MIN_TIME = 1000000;

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private Context context;

    //mLocation will be null if it is not acceptable
    private Location mLocation;

    private LocationManager locationManager;
    private GoogleApiClient client;

    //constructor method. Takes in the current application context
    public LocationHolder(Context newContext){
        Log.d(LOG_TAG, "New LocationHolder instance created");
        context = newContext;
            //Initializing Google API
            //As we need to wait on this object, it is necessary that we lock it with the current
            //thread so no racing condition occurs. Thus the synchronized keyword
        client = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

    }

    //Helper methods
    public boolean isAcceptableLocation(Location location){
        if (location == null){
            return false;
        }
        long currentTime = (new Date()).getTime();
        long fixTime = location.getTime();
        return ((currentTime-fixTime) < MIN_TIME);
    }

    //Helper method. Writes the providers with non-null last known location to the log
    private ArrayList<String> logAvailableProviders() {
        ArrayList<String> resultList = new ArrayList<String>();
        Log.d(LOG_TAG, "Logging available location providers:");
        List<String> providers = locationManager.getAllProviders();
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
    private String getBestProvider(ArrayList<String> providers) {
        //Assert that the number of providers available would be small enough
        //Hence the naive method used here would be enough.
        //Also we are concerned more with time than accuracy since this app is about
        //"nearby" places
        long bestTime = 0;
        String bestProvider = providers.get(0);
        //here time denotes the interval from 1970 to fix, so the bigger the number the closer the fix
        for (String provider : providers) {
            long time = locationManager.getLastKnownLocation(provider).getTime();
            if (time > bestTime) {
                bestTime = time;
                bestProvider = provider;
            }
        }
        //Note that here bestProvider could be the empty String; this means that no location data is available
        return bestProvider;
    }

    //Getter method
    public Location getmLocation() throws NullPointerException {
        if(isAcceptableLocation(mLocation)){
            return mLocation;
        }

        synchronized (this) {
            client.connect();
            try {
                wait(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
            client.disconnect();
        }

        getLocationWithDevice();

        if(isAcceptableLocation(mLocation)){
            return mLocation;
        }

        throw new NullPointerException();
    }

    //Code to enable getting location where Google Play is not available




    //Methods for locating using Device

    private void getLocationWithDevice() {
        try {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            ArrayList<String> providers = logAvailableProviders();
            String bestProvider = getBestProvider(providers);
            if (bestProvider == "") {
                mLocation = null;
                return;
            } else {
                mLocation = locationManager.getLastKnownLocation(bestProvider);
                locationManager.removeUpdates(this);
                if (isAcceptableLocation(mLocation)){
                    return;
                }
                synchronized (this) {
                    locationManager.requestLocationUpdates(bestProvider, 1000, 10, this);
                    this.wait(1000);
                }
            }
        } catch(Exception e){
            mLocation = null;
        }
    }



    //Override methods for Google
    @Override
    public void onConnected(Bundle bundle) {
        mLocation = LocationServices.FusedLocationApi.getLastLocation(client);
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Nothing to do
    }




    //Override Methods for Device
    @Override
    //When this method receives a call back the device have obtained a recent fix on location,
    //current location is clearly available and would be updated.
    public void onLocationChanged(Location location) {
        if (location != null) {
            mLocation = location;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //Nothing to do
    }

    @Override
    public void onProviderEnabled(String provider) {
        //Nothing to do
    }

    @Override
    public void onProviderDisabled(String provider) {
        //Nothing to do
    }
}
