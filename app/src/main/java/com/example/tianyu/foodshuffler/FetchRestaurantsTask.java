package com.example.tianyu.foodshuffler;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

/**
 * Created by Tianyu on 5/28/2015.
 */

/* AsyncTask to query the Yelp server about nearby restaurants*/
public class FetchRestaurantsTask extends AsyncTask<Location, String, Restaurant> {

    private final Context mContext;
    private Location currentLocation;
    private ProgressBar mProgressBar;
    private FloatingActionButton mFab;
    private MainActivity mMainActivity;
    private static Random random = new Random();
    private final String LOG_TAG = FetchRestaurantsTask.class.getSimpleName();

    // For Yelp API
    private static String CONSUMER_KEY;
    private static String CONSUMER_SECRET;
    private static String TOKEN;
    private static String TOKEN_SECRET;

    private static final String YELP_SEARCH_TERM = "food";


    //Constructor
    public FetchRestaurantsTask(Context context, MainActivity mainActivity) {
        mContext = context;
        mProgressBar = mainActivity.getProgressBar();
        mFab = mainActivity.getShuffleFab();
        mMainActivity = mainActivity;

        CONSUMER_KEY = mContext.getResources().getString(R.string.yelp_consumerkey);
        CONSUMER_SECRET = mContext.getResources().getString(R.string.yelp_consumersecret);
        TOKEN = mContext.getResources().getString(R.string.yelp_token);
        TOKEN_SECRET = mContext.getResources().getString(R.string.yelp_tokensecret);
    }

    //Method returns true if successfully gotten location, false otherwise
    private boolean getLocation(){
        Log.d(LOG_TAG, "Getting Location...");
        try {
            LocationHolder myLocation = new LocationHolder(mContext);
            currentLocation = myLocation.getLocation();
            return true;
        } catch(Exception e){
            return false;
        }
    }

    private Restaurant getDataFromJSON(String locationDataJSON) {
        try {
            //Attempts to shuffle and construct a Restaurant object out of the data retrieved
            Log.d(LOG_TAG,"Begin parsing resultString");
            JSONObject fetchResult = new JSONObject(locationDataJSON);
            JSONArray businesses = fetchResult.getJSONArray("businesses");
            int index = random.nextInt(businesses.length());
            Restaurant result = new Restaurant(businesses,index);
            Log.d(LOG_TAG,"Done parsing resultString");
            return result;
        } catch (Exception e) {
            Log.d(LOG_TAG,locationDataJSON);
            Log.d(LOG_TAG,e.toString());
            return null;
        }
    }

    @Override
    protected Restaurant doInBackground(Location... params) {
        Log.d(LOG_TAG, "starting to fetch data in background");
        if(getLocation()) {
            YelpAPI yelpRequest = new YelpAPI(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);
            String resultJSON = yelpRequest.searchForBusinessesByCoordinates
                    (YELP_SEARCH_TERM, currentLocation.getLatitude(), currentLocation.getLongitude());
            return getDataFromJSON(resultJSON);
        }
        //returning null signals error
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Restaurant restaurant) {
        super.onPostExecute(restaurant);
        mMainActivity.showShuffleFab();
        mProgressBar.setVisibility(View.INVISIBLE);
        if(restaurant == null) {
            Toast toast = Toast.makeText(mContext,"Restaurants Unavailable",Toast.LENGTH_LONG);
            toast.show();
        } else {
            mMainActivity.setRestaurantCard(restaurant);
        }
    }

}
