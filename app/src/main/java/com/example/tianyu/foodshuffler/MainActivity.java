package com.example.tianyu.foodshuffler;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.scribe.builder.api.GoogleApi;

import static com.google.android.gms.common.api.GoogleApiClient.*;


public class MainActivity extends ActionBarActivity{

    private TextView textView;
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState){
        //This simple UI consists of a TextView for displaying results or error messages and a button to initiate shuffle action
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obtain a reference to the TextView
        textView = (TextView) findViewById(R.id.textView);

        //Programs the shuffle button to perform shuffle action
        Button shuffleAction = (Button) findViewById(R.id.shuffle_action);
        shuffleAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //constructs a FetchRestaurantsTask instance and executes, gets back a JSON string
                FetchRestaurantsTask mFetchRestaurantsTask = new FetchRestaurantsTask(getApplicationContext(), textView);
                mFetchRestaurantsTask.execute();
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
