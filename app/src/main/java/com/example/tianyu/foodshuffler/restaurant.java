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

    //takes in a JSONArray representing businesses around the area, and constructs a restaurant object from the business representing at index
    //Requires : businesses is not null and a JSONArray as specified in Yelp API, index is non-negative and within limit
    //Ensures : constructs a restaurant object with fields from the JSONArray and index
    public restaurant(JSONArray businesses, int index) throws JSONException {
        JSONObject currentRestaurant = (JSONObject) businesses.get(index);
        name = currentRestaurant.getString(NAME);
        location = currentRestaurant.getJSONObject(LOCATION).getJSONArray(DISPLAY_ADDRESS).getString(0);
        mobileUrl = currentRestaurant.getString(MOBILE_URL);
        phone = currentRestaurant.getString(PHONE);
        //notice here that category could be null, which signals an error in parsing
        category = fromJSONArray(currentRestaurant.getJSONArray(CATEGORY));
        distance = currentRestaurant.getLong(DISTANCE);
        if (category != null) {
            description = name + "\n" + location + "\n" + formatCategories(category) + mobileUrl;
        }
    }
}
