package com.example.tianyu.foodshuffler;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Created by Tianyu on 5/27/2015.
 */
/* data structure to hold the chosen restaurant. Supports possible future features like Show In Map, Call, etc. */
public class restaurant{
    //fields
    public String name;
    public String location;
    public String mobileUrl;
    public String phone;
    public String category;
    public String description;
    public long distance;

    //constants
    private final String NAME = "name";
    private final String LOCATION = "location.display_address";
    private final String MOBILE_URL = "mobile_url";
    private final String PHONE = "phone";
    private final String CATEGORY = "categories";
    private final String DISTANCE = "distance";

    //takes in a JSONArray representing businesses around the area, and constructs a restaurant object from the business representing at index
    //Requires : businesses is not null and a JSONArray as specified in Yelp API, index is non-negative and within limit
    //Ensures : constructs a restaurant object with fields from the JSONArray and index
    public restaurant(JSONArray businesses, int index) throws JSONException{
        JSONObject currentRestaurant = (JSONObject) businesses.get(index);
        name = currentRestaurant.getString(NAME);
        location = currentRestaurant.getJSONArray(LOCATION).getString(0);
        mobileUrl = currentRestaurant.getString(MOBILE_URL);
        phone = currentRestaurant.getString(PHONE);
        category = currentRestaurant.getString(CATEGORY);
        distance = currentRestaurant.getLong(DISTANCE);
        description = name + "\n" + location + "\n" + mobileUrl;
    }



}
