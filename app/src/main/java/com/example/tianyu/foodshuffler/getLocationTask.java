package com.example.tianyu.foodshuffler;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.List;

/**
 * Created by Tianyu on 5/27/2015.
 */

/* This class is used to retrieve the current location */
public class getLocationTask{
    private Context context;
    private Location currentLocation;
    private LocationManager locationManager;

    public getLocationTask(Context currentContext) {
        context = currentContext;
        currentLocation = null;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

        public void findBestLocation(){
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        currentLocation = bestLocation;
    }

    public Location getLocation() {
        return currentLocation;
    }

}


