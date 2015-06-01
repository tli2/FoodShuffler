package com.example.tianyu.foodshuffler;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.util.Random;

/**
 * Created by Tianyu on 5/28/2015.
 */

/* AsyncTask to query the Yelp server about nearby restaurants*/
public class FetchRestaurantsTask extends AsyncTask<Location,Void, String> {

    private final Context mContext;

    public FetchRestaurantsTask(Context context){
        mContext = context;
    }

    @Override
    protected String doInBackground(Location... params) {
        if (params.length == 0){
            return null;
        }
        Location currentLocation = params[0];
        try{
            double coord_lat = currentLocation.getLatitude();
            double coord_long = currentLocation.getLongitude();
            Uri.Builder queryBuilder = Uri.parse("http://api.yelp.com/v2/search?term=food").buildUpon();
            queryBuilder.appendQueryParameter("ll",Double.toString(coord_lat) + "," + Double.toString(coord_long));
            URL queryUrl = new URL(queryBuilder.build().toString());

            HttpURLConnection urlConnection = (HttpURLConnection) queryUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            return reader.toString();
        }catch(IOException e){
            return null;
        }

    }
}
