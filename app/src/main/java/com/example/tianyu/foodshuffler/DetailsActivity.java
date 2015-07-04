package com.example.tianyu.foodshuffler;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DetailsActivity extends ActionBarActivity {

    private TextView detailView;
    private final String LOG_TAG = DetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        detailView = (TextView) findViewById(R.id.DetailView);

        Intent callingIntent = getIntent();
        final Restaurant result = callingIntent.getParcelableExtra("restaurant");

        if(result == null){
            detailView.setText("Error receiving data, please check Internet Connectivity");
            return;
        }
        detailView.setText(result.description);

        Button mapButton = (Button) findViewById(R.id.mapAction);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri geoLocation = Uri.parse("geo:0,0?q=" + result.locationURI);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW);
                mapIntent.setData(geoLocation);
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    detailView.setText("Cannot detect map application");
                }
            }
        });

        Button callButton = (Button) findViewById(R.id.callAction);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri callNumber = Uri.parse("tel:" + result.phone);
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(callNumber);
                if (callIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(callIntent);
                } else {
                    detailView.setText("Cannot detect phone application");
                }
            }
        });

        Button webButton = (Button) findViewById(R.id.webAction);
        webButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webUri = Uri.parse(result.mobileUrl);
                Intent webIntent = new Intent(Intent.ACTION_VIEW);
                webIntent.setData(webUri);
                if (webIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(webIntent);
                } else {
                    detailView.setText("Cannot detect browser app");
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
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
}
