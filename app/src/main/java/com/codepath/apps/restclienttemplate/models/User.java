package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * <h1>User object</h1>
 * The User class outlines the User object which is used to store information for each user.
 * Each User object is comprised of their name, user ID, screeName (@name), and profile image url.
 */

@Parcel // annotation indicates class is Parcelable
public class User {

    // list all the attributes
    public String name;
    public long uid;
    public String screenName;
    public String profileImageUrl;

    public User() {}

    // deserialize the JSON
    public static User fromJSON(JSONObject jsonObject) throws JSONException {
        User user = new User();

        // extract and fill the values
        user.name = jsonObject.getString("name");
        user.uid = jsonObject.getLong("id");
        user.screenName = jsonObject.getString("screen_name");
        user.profileImageUrl = jsonObject.getString("profile_image_url");

        return user;
    }

}
