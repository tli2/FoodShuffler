package com.example.tianyu.foodshuffler;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
        Log.d(LOG_TAG,"New MyLocation instance created");
        context = newContext;
        //Obtain a reference to locationManager under the current context. Assert that locationManager is not null
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //attempts to fix current location
        fixLocation();
    }

    public void fixLocation() {
        try {
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            //If neither gps or network can be used the shuffle action should not be initiated
            if (!gpsEnabled && !networkEnabled) {
                Log.d(LOG_TAG,"No Location Service Available");
                Toast.makeText(context, "Unable to obtain Location, please enable Locations", Toast.LENGTH_LONG).show();
                locationAvailable = false;
            } else {
                // now check which provider is available, gps is favored over network location.
                // we initiate a request and then attempts to read approximated location from the stored location data.
                // Here we are assuming that the last known location is somewhat accurate
                if (gpsEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_DIST, MIN_TIME, this);
                    mLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }else if (networkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_DIST,MIN_TIME,this);
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                locationAvailable = true;

                //if mLocation is null, then we do not have a good approximation of the current location, and must wait for the real-time update
                if (mLocation == null){
                    locationAvailable = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopUsingLocation() {
        locationManager.removeUpdates(this);
    }

    public Location getmLocation(){
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
        locationAvailable = true;
        mLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(context,"New Location Provider Set: " + provider, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(context,"Location Provider Disabled: " + provider, Toast.LENGTH_LONG).show();
    }
}

