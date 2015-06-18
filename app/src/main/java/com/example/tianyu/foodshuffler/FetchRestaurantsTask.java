package com.example.tianyu.foodshuffler;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

/**
 * Created by Tianyu on 5/28/2015.
 */

/* AsyncTask to query the Yelp server about nearby restaurants*/
public class FetchRestaurantsTask extends AsyncTask<Location, String, restaurant> {



    private final Context mContext;
    private Location currentLocation;
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    // For Yelp API
    private static final String CONSUMER_KEY = "u8W0S27Ife1UpZWRBCkYRw";
    private static final String CONSUMER_SECRET = "ARQKZ0YrqkZIkYKQipuAz7WCIC8";
    private static final String TOKEN = "lrCALM_TKCrOGdO5PnITGSAanxWKX4_3";
    private static final String TOKEN_SECRET = "-SNGmxMWnz6RTsuFbtLWmEfq3DA";




    //Constructor, requires that LocationProvider be one of the two constants provided
    public FetchRestaurantsTask(Context context) {
        mContext = context;
    }

    //Method returns true if successfully gotten location, false otherwise
    private boolean getLocation(){
        Log.d(LOG_TAG, "Getting Location...");
        try {
            LocationHolder myLocation = new LocationHolder(mContext);
            currentLocation = myLocation.getmLocation();
            return true;
        } catch(Exception e){
            return false;
        }
    }

    private restaurant getDataFromJSON(String locationDataJSON) {
        try {
            //Attempts to shuffle and construct a restaurant object out of the data retrieved
            Log.d(LOG_TAG,"Begin parsing resultString");
            JSONObject fetchResult = new JSONObject(locationDataJSON);
            JSONArray businesses = fetchResult.getJSONArray("businesses");
            int index = (new Random()).nextInt(businesses.length());
            restaurant result = new restaurant(businesses,index);
            Log.d(LOG_TAG,"Done parsing resultString");
            return result;
        } catch (Exception e) {
            Log.d(LOG_TAG,locationDataJSON);
            Log.d(LOG_TAG,e.toString());
            return null;
        }
    }

    @Override
    protected restaurant doInBackground(Location... params) {
        Log.d(LOG_TAG, "starting to fetch data in background");
        if(getLocation()) {
            YelpAPI yelpRequest = new YelpAPI(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);
            String resultJSON = yelpRequest.searchForBusinessesByCoordinates
                    ("food", currentLocation.getLatitude(), currentLocation.getLongitude());
            return getDataFromJSON(resultJSON);
        }
        //returning null signals error
        return null;

    }

}
