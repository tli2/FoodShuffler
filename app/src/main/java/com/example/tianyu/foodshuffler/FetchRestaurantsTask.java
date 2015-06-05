package com.example.tianyu.foodshuffler;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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

    public FetchRestaurantsTask(Context context, TextView textView) {
        mContext = context;
        mTextView = textView;
    }

    private void getLocation(){
        Log.d(LOG_TAG, "Getting Location...");

        LocationHolder myLocation = new LocationHolder(mContext);
        if (myLocation.isLocationAvailable()) {
            currentLocation = myLocation.getmLocation();
            Log.d(LOG_TAG,"Location has been received.");
            Log.d(LOG_TAG,"Final Latitude: " + currentLocation.getLatitude());
            Log.d(LOG_TAG,"Final Longitude: " + currentLocation.getLongitude());
            myLocation.stopUsingLocation();
        } else{
            Toast.makeText(mContext, "Unable to get current location, please try again", Toast.LENGTH_LONG).show();
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
            return "Failed to Parse Result";
        }
    }

    @Override
    protected void onPreExecute() {
        Log.d(LOG_TAG, "PreExecute");
        getLocation();
    }
        @Override
    protected String doInBackground(Location... params) {
        Log.d(LOG_TAG, "starting to fetch data in background");
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
