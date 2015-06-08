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
public class FetchRestaurantsTask extends AsyncTask<Location, String, String> {



    private final Context mContext;
    private final TextView mTextView;
    private Location currentLocation;
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    // For Yelp API
    private static final String CONSUMER_KEY = "u8W0S27Ife1UpZWRBCkYRw";
    private static final String CONSUMER_SECRET = "ARQKZ0YrqkZIkYKQipuAz7WCIC8";
    private static final String TOKEN = "lrCALM_TKCrOGdO5PnITGSAanxWKX4_3";
    private static final String TOKEN_SECRET = "-SNGmxMWnz6RTsuFbtLWmEfq3DA";

    //Constructor, requires that LocationProvider be one of the two constants provided
    public FetchRestaurantsTask(Context context, TextView textView) {
        mContext = context;
        mTextView = textView;

    }

    private void getLocation() {
        Log.d(LOG_TAG, "Getting Location...");
        LocationHolder myLocation = new LocationHolder(mContext, LocationHolder.GOOGLE_LOCATIONS);
        if (myLocation.isLocationAvailable()) {
            currentLocation = myLocation.getmLocation();
        } else if (myLocation.isGoogleUnavailable()){
            myLocation = new LocationHolder(mContext, LocationHolder.DEVICE_LOCATIONS);
            if (myLocation.isLocationAvailable()) {
                currentLocation = myLocation.getmLocation();
            }
        } else{
            return;
        }



    }

    private String getLocationDataFromJSON(String locationDataJSON) {
        try {
            //Attempts to shuffle and construct a restaurant object out of the data retrieved
            Log.d(LOG_TAG,"Begin parsing resultString");
            JSONObject fetchResult = new JSONObject(locationDataJSON);
            JSONArray businesses = fetchResult.getJSONArray("businesses");
            int index = (new Random()).nextInt(businesses.length());
            restaurant result = new restaurant(businesses,index);
            Log.d(LOG_TAG,"Done parsing resultString");
            return result.description;
        } catch (Exception e) {
            Log.d(LOG_TAG,locationDataJSON);
            Log.d(LOG_TAG,e.toString());
            return "Failed to Parse Result";
        }
    }

    @Override
    protected String doInBackground(Location... params) {
        Log.d(LOG_TAG, "starting to fetch data in background");
        getLocation();

        //Catch the null case in order to prevent application crash
        if(currentLocation == null){
            return "Null location";
        }

        YelpAPI yelpRequest = new YelpAPI(CONSUMER_KEY,CONSUMER_SECRET,TOKEN,TOKEN_SECRET);

        String resultJSON = yelpRequest.searchForBusinessesByCoordinates
                ("food",currentLocation.getLatitude(),currentLocation.getLongitude());

        return getLocationDataFromJSON(resultJSON);

    }

    @Override
    protected void onPostExecute(String result) {
        mTextView.setText(result);
    }

}
