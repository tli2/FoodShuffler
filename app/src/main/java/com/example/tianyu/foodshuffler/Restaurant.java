package com.example.tianyu.foodshuffler;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tianyu on 5/27/2015.
 */
/* data structure to hold the chosen Restaurant. Supports possible future features like Show In Map, Call, etc. */
public class Restaurant implements Parcelable{
    //fields
    public String name;
    public String location;
    public String locationURI;
    public String mobileUrl;
    public String phone;
    public String[] category;
    public String description;
    public long distance;

    //constants
    private final String NAME = "name";
    private final String LOCATION = "location";
    private final String DISPLAY_ADDRESS = "display_address";
    private final String MOBILE_URL = "mobile_url";
    private final String PHONE = "phone";
    private final String CATEGORY = "categories";
    private final String DISTANCE = "distance";

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
            return (categories[0] + ", " + categories[1] + ", " + categories[2] + "\n");
        }

        else if(categories.length == 2){
            return (categories[0] + ", " + categories[1] + "\n");
        }

        else{
            return (categories[0] + "\n");
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
    //Ensures : constructs a Restaurant object with fields from the JSONArray and index
    public Restaurant(JSONArray businesses, int index) throws JSONException {
        JSONObject currentRestaurant = (JSONObject) businesses.get(index);
        name = currentRestaurant.getString(NAME);
        location = currentRestaurant.getJSONObject(LOCATION).getJSONArray(DISPLAY_ADDRESS).getString(0);
        locationURI = formatLocationURI(currentRestaurant.getJSONObject(LOCATION));
        mobileUrl = currentRestaurant.getString(MOBILE_URL);
        phone = currentRestaurant.getString(PHONE);
        //notice here that category could be null, which signals an error in parsing
        category = fromJSONArray(currentRestaurant.getJSONArray(CATEGORY));
        distance = currentRestaurant.getLong(DISTANCE);
        if (category != null) {
            description = name + "\n" + location + "\n" + formatCategories(category) + mobileUrl;
        }
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
        data.add(location);
        data.add(locationURI);
        data.add(mobileUrl);
        data.add(phone);
        data.add(description);
        out.writeStringList(data);
        out.writeLong(distance);
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
        location = data.get(1);
        locationURI = data.get(2);
        mobileUrl = data.get(3);
        phone = data.get(4);
        description = data.get(5);
        distance = in.readLong();
    }
}
