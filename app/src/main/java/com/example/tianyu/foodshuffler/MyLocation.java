package com.example.tianyu.foodshuffler;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Billy on 6/2/2015.
 */
public class MyLocation implements LocationListener {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private Context context;

    private boolean gpsEnabled = false;
    private boolean networkEnabled = false;
    private boolean locationAvailable = false;

    private double latitude;
    private double longitude;
    private Location mLocation;

    private LocationManager locationManager;

    // minimum distance change for updates
    private int MIN_TIME = 1000;
    // minimum time between mLocation updates
    private int MIN_DIST = 10;

    public MyLocation(Context newContext) {
        Log.d(LOG_TAG,"New MyLocation instance created");
        context = newContext;
        getmLocation();
    }

    public Location getmLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!gpsEnabled && !networkEnabled) {
                Log.d(LOG_TAG,"No Location Service Available");
                locationAvailable = false;
            } else {
                // now check which provider is available, gps is favored over network location

                if (gpsEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, this);

                    if (locationManager != null) {
                        locationAvailable = true;
                        mLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                }

                else if (networkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME,MIN_DIST,this);

                    if (locationManager != null) {
                        locationAvailable = true;
                        mLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }

                if (mLocation == null){
                    locationAvailable = false;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mLocation;
    }

    public void stopUsingLocation() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    public double getLatitude() {
        if (mLocation != null) {
            latitude = mLocation.getLatitude();
        }

        return latitude;
    }

    public double getLongitude() {
        if (mLocation != null) {
            longitude = mLocation.getLongitude();
        }

        return longitude;
    }

    public boolean isLocationAvailable() {
        return locationAvailable;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        latitude = location.getLatitude();
        longitude = location.getLongitude();
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
