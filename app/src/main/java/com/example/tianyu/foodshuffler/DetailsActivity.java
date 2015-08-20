package com.example.tianyu.foodshuffler;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private double mLatitude;
    private double mLongitude;
    private Restaurant mResult;
    private final String LOG_TAG = DetailsActivity.class.getSimpleName();

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng targetCoords = new LatLng(mLatitude, mLongitude);
        googleMap.addMarker(new MarkerOptions().position(targetCoords).title(mResult.name));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(targetCoords, 14));
        googleMap.setMyLocationEnabled(true);
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setMapToolbarEnabled(false);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                openInMap();
            }
        });
    }

    private void openInMap() {
        Uri geoLocation = Uri.parse("geo:0,0?q=" + mResult.locationURI);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        mapIntent.setData(geoLocation);
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(getApplicationContext(), "Cannot detect map application", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.details_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent callingIntent = getIntent();
        final Restaurant result = callingIntent.getParcelableExtra("restaurant");
        mResult = result;

        final CollapsingToolbarLayout CTL = (CollapsingToolbarLayout) findViewById(R.id.details_collapsingToolbar);
        CTL.setTitle(result.name);

        Palette.generateAsync(result.image, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                CTL.setContentScrimColor(palette.getVibrantColor(R.attr.colorPrimary));
                CTL.setStatusBarScrimColor(palette.getDarkVibrantColor(R.attr.colorPrimaryDark));
            }
        });

        ImageView imageView = (ImageView) findViewById(R.id.details_backdropImg);
        imageView.setImageBitmap(result.image);

        /* CUISINE */
        View cuisineItem = findViewById(R.id.details_cuisine);
        cuisineItem.setClickable(true);
        ImageView cuisineIcon = (ImageView) cuisineItem.findViewById(R.id.details_item_icon);
        cuisineIcon.setImageResource(R.drawable.ic_label_black_36dp);
        TextView cuisinePrimary = (TextView) cuisineItem.findViewById(R.id.details_item_primaryText);
        cuisinePrimary.setText(result.getCategories());
        TextView cuisineSecondary = (TextView) cuisineItem.findViewById(R.id.details_item_secondaryText);
        cuisineSecondary.setText(R.string.details_item_cuisine_2nd);

        /* REVIEW */
        View reviewItem = findViewById(R.id.details_review);
        ImageView reviewStars = (ImageView) reviewItem.findViewById(R.id.details_item_review_stars);
        reviewStars.setImageResource(result.getRatingImgId());
        TextView reviewSecondary = (TextView) reviewItem.findViewById(R.id.details_item_secondaryText);
        String reviewSecText = getResources().getQuantityString(R.plurals.details_item_review_2nd,
                result.getReviewCount(), result.getReviewCount());
        reviewSecondary.setText(reviewSecText);

        reviewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webUri = Uri.parse(result.mobileUrl);
                Intent webIntent = new Intent(Intent.ACTION_VIEW);
                webIntent.setData(webUri);
                if (webIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(webIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot detect browser application", Toast.LENGTH_LONG).show();
                }
            }
        });

        /* PHONE */
        View phoneItem = findViewById(R.id.details_phone);
        ImageView phoneIcon = (ImageView) phoneItem.findViewById(R.id.details_item_icon);
        phoneIcon.setImageResource(R.drawable.ic_phone_black_36dp);
        TextView phonePrimary = (TextView) phoneItem.findViewById(R.id.details_item_primaryText);
        phonePrimary.setText(result.getDisplayPhone());
        TextView phoneSecondary = (TextView) phoneItem.findViewById(R.id.details_item_secondaryText);
        phoneSecondary.setText(R.string.details_item_phone_2nd);

        if(result.hasPhone()) {
            phoneItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri callNumber = Uri.parse("tel:" + result.getPhone());
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(callNumber);
                    if (callIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(callIntent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Cannot detect phone application", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        /* MAP */
        View mapItem = findViewById(R.id.details_map);
        TextView mapPrimary = (TextView) mapItem.findViewById(R.id.details_item_primaryText);
        mapPrimary.setText(result.display_address);
        TextView mapSecondary = (TextView) mapItem.findViewById(R.id.details_item_secondaryText);
        String mapSecText = String.format(getResources().getString(R.string.details_item_map_2nd), (int)result.distance);
        mapSecondary.setText(mapSecText);

        mapItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInMap();
            }
        });

        mLatitude = result.latitude;
        mLongitude = result.longitude;

        //setup Lite Map
        Log.d(LOG_TAG, "address ready, setting up map");
        // setup map fragment
        GoogleMapOptions mapOptions = new GoogleMapOptions().liteMode(true);
        SupportMapFragment mapFragment = SupportMapFragment.newInstance(mapOptions);

        // add the map fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.details_item_map_frame, mapFragment);
        fragmentTransaction.commit();

        mapFragment.getMapAsync(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);

        MenuItem shareMenuItem = menu.findItem(R.id.action_share);
        ShareActionProvider sap = (ShareActionProvider) MenuItemCompat.getActionProvider(shareMenuItem);

        if(mResult != null) {
            sap.setShareIntent(createShareRestaurantIntent());
        }
        return true;
    }

    private Intent createShareRestaurantIntent() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_text, mResult.name));
        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
