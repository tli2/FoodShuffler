package com.example.tianyu.foodshuffler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Tianyu on 5/27/2015.
 */
/* data structure to hold the chosen Restaurant. Supports possible future features like Show In Map, Call, etc. */
public class Restaurant implements Parcelable{

    private final String LOG_TAG = Restaurant.class.getSimpleName();

    //fields
    public String name;
    public String display_address;
    public String locationURI;
    public String mobileUrl;
    public String phone;
    public String display_phone;
    public String rating;
    public int review_count;
    public String[] category;
    public String description;
    public double distance;
    public double latitude;
    public double longitude;
    public Bitmap image;

    //constants
    private final String NAME = "name";
    private final String LOCATION = "location";
    private final String DISPLAY_ADDRESS = "display_address";
    private final String MOBILE_URL = "mobile_url";
    private final String PHONE = "phone";
    private final String DISPLAY_PHONE = "display_phone";
    private final String RATING = "rating";
    private final String REVIEW_COUNT = "review_count";
    private final String CATEGORY = "categories";
    private final String DISTANCE = "distance";
    private final String COORDINATE = "coordinate";
    private final String LATITUDE = "latitude";
    private final String LONGITUDE = "longitude";
    private final String IMAGE_URL = "image_url";

    public String getCategories() {
        return this.formatCategories(this.category);
    }

    public int getRatingImgId() {
        switch (rating) {
            case "1.0":
                return R.drawable.yelp_stars_1;
            case "1.5":
                return R.drawable.yelp_stars_1half;
            case "2.0":
                return R.drawable.yelp_stars_2;
            case "2.5":
                return R.drawable.yelp_stars_2half;
            case "3.0":
                return R.drawable.yelp_stars_3;
            case "3.5":
                return R.drawable.yelp_stars_3half;
            case "4.0":
                return R.drawable.yelp_stars_4;
            case "4.5":
                return R.drawable.yelp_stars_4half;
            case "5.0":
                return R.drawable.yelp_stars_5;
            default:
                return R.drawable.yelp_stars_1;
        }
    }

    public int getReviewCount() {
        return review_count;
    }

    public boolean hasPhone() {
        return display_phone != null;
    }

    public String getDisplayPhone() {
        if(hasPhone()) {
            return this.display_phone;
        }
        return "Unavailable";
    }

    public Address getCoordsFromGeocoder(Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocationName(this.locationURI, 1);
            return addressList.get(0);
        } catch (IOException e) {
            Log.d(LOG_TAG, "exception geocoding");
            return null;
        }
    }

    public String getPhone() {
        return this.phone;
    }

    //Helper method to convert a JSONArray into a regular String array
    //Requires : targetArray format complies with Yelp API specification
    //Ensures : returns an array with the category information identical with the JSONArray
    private String[] fromJSONArray(JSONArray targetArray){
        String[] resultArray = new String[targetArray.length()];
        for (int i = 0; i < targetArray.length(); i++){
            try {
                resultArray[i] = targetArray.getJSONArray(i).getString(0);
            } catch(Exception e){
                return null;
            }
        }
        return resultArray;
    }

    //Helper method to process category information. Picks at most three and format
    //Requires : categories.length != 0
    //Ensures : outputs the current output format based on categories
    private String formatCategories(String[] categories){
        if (categories.length >= 3){
            return (categories[0] + ", " + categories[1] + ", " + categories[2]);
        }

        else if(categories.length == 2){
            return (categories[0] + ", " + categories[1]);
        }

        else{
            return (categories[0]);
        }
    }

    private String formatLocationURI(JSONObject location) throws JSONException {
            String foo = location.getJSONArray("address").getString(0)
                    + ", " + location.getString("city")
                    + ", " + location.getString("state_code");
            char[] foobar = foo.toCharArray();
            for (int i =0; i < foobar.length; i++){
                if(foobar[i] == ' '){
                    foobar[i] = '+';
                }
            }
            return new String(foobar);
    }

    //takes in a JSONArray representing businesses around the area, and constructs a Restaurant object from the business representing at index
    //Requires : businesses is not null and a JSONArray as specified in Yelp API, index is non-negative and within limit
    //           Never call this constructor from the UI thread as it contains code that fetches image from web.
    //Ensures : constructs a Restaurant object with fields from the JSONArray and index
    public Restaurant(JSONArray businesses, int index) throws JSONException {
        JSONObject currentRestaurant = (JSONObject) businesses.get(index);
        name = currentRestaurant.getString(NAME);
        display_address = currentRestaurant.getJSONObject(LOCATION).getJSONArray(DISPLAY_ADDRESS).getString(0);
        locationURI = formatLocationURI(currentRestaurant.getJSONObject(LOCATION));
        mobileUrl = currentRestaurant.getString(MOBILE_URL);
        phone = currentRestaurant.getString(PHONE);
        display_phone = currentRestaurant.getString(DISPLAY_PHONE);
        rating = String.valueOf(currentRestaurant.getDouble(RATING));
        review_count = currentRestaurant.getInt(REVIEW_COUNT);
        //notice here that category could be null, which signals an error in parsing
        category = fromJSONArray(currentRestaurant.getJSONArray(CATEGORY));
        distance = currentRestaurant.getDouble(DISTANCE);
        JSONObject coordinate = currentRestaurant.getJSONObject(LOCATION).getJSONObject(COORDINATE);
        latitude = coordinate.getDouble(LATITUDE);
        longitude = coordinate.getDouble(LONGITUDE);
        if (category != null) {
            description = name + "\n" + display_address + "\n" + formatCategories(category) + mobileUrl;
        }
        image = fetchImagefromUrl(currentRestaurant.getString(IMAGE_URL));
    }

    private Bitmap fetchImagefromUrl(String imgUrl) {
        try {
            URL Url = new URL(imgUrl);
            InputStream in = Url.openConnection().getInputStream();
            return BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Implements Parcelable in order for a Restaurant object to be passed through an intent

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeStringArray(category);
        ArrayList<String> data = new ArrayList<>();
        data.add(name);
        data.add(display_address);
        data.add(locationURI);
        data.add(mobileUrl);
        data.add(phone);
        data.add(display_phone);
        data.add(rating);
        data.add(description);
        out.writeStringList(data);
        out.writeInt(review_count);
        double doubles[] = {latitude,longitude,distance};
        out.writeDoubleArray(doubles);
        out.writeParcelable(image,0);
    }

    public static final Parcelable.Creator<Restaurant> CREATOR
            = new Parcelable.Creator<Restaurant>() {
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    private Restaurant(Parcel in) {
        category = in.createStringArray();
        ArrayList<String> data = in.createStringArrayList();
        name = data.get(0);
        display_address = data.get(1);
        locationURI = data.get(2);
        mobileUrl = data.get(3);
        phone = data.get(4);
        display_phone = data.get(5);
        rating = data.get(6);
        description = data.get(7);
        review_count = in.readInt();
        double doubles[] = in.createDoubleArray();
        latitude = doubles[0];
        longitude = doubles[1];
        distance = doubles[2];
        image = in.readParcelable(null);
    }
}
