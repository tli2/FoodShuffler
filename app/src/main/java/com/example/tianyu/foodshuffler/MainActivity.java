package com.example.tianyu.foodshuffler;

import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;
import org.json.JSONArray;

import java.util.Random;


public class MainActivity extends ActionBarActivity{

    private TextView textView;
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        //This simple UI consists of a TextView for displaying results or error messages and a button to initiate shuffle action
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);

        //Constructs a getLocationTask object and stores the current location
        getLocationTask mGetLocationTask = new getLocationTask(getApplicationContext());
        mGetLocationTask.findBestLocation();
        final Location location = mGetLocationTask.getLocation();


        //Programs the button to perform shuffle action
        Button shuffleAction = (Button) findViewById(R.id.shuffle_action);
        shuffleAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //constructs a FetchRestaurantsTask instance and executes, gets back a JSON string
                FetchRestaurantsTask mFetchRestaurantsTask = new FetchRestaurantsTask(getApplicationContext());
                //Catch the null case in order to prevent application crash
                if(location == null){
                    textView.setText("Null Location");
                }
                mFetchRestaurantsTask = (FetchRestaurantsTask) mFetchRestaurantsTask.execute(location);
                try {
                    //Attempts to shuffle and construct a restaurant object out of the data retrieved
                    String resultString = mFetchRestaurantsTask.get();
                    JSONObject fetchResult = new JSONObject(resultString);
                    JSONArray businesses = fetchResult.getJSONArray("businesses");
                    int index = (new Random()).nextInt(businesses.length());
                    restaurant result = new restaurant(businesses,index);
                    //Displays the result in the TextView
                    textView.setText(result.description);
                }catch(Exception e) {
                    return;
                }
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
