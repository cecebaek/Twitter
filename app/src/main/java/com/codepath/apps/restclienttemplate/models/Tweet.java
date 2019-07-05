package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * <h1>Tweet object</h1>
 * The Tweet class outlines the Tweet object which is used to store information for each tweet.
 * Each Tweet will be composed of a body, uid (database ID for the tweet), a User object which
 * represents the creator of the tweet, the createdAt string which shows the relative time the
 * tweet was sent, and the number/status of "likes" and "retweets".
 */

@Parcel // annotation indicates class is Parcelable
public class Tweet {

    // list out all attributes
    public String body; // message of the tweet
    public long uid; // database ID for the tweet
    public User user;   // user who created the tweet
    public String createdAt;    // used to calculate relative time of tweet
    public int likeCount;   // number of likes the tweet has gained
    public boolean likeStatus; // true if tweet was liked by user, false otherwise
    public int retweetCount;    // number of retweets the tweet has gained
    public boolean retweetStatus;   // true if tweet was retweeted by the user, false otherwise

    public Tweet() {}

    // deserialize the data
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        // extract all the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.likeCount = jsonObject.getInt("favorite_count");
        tweet.likeStatus = jsonObject.getBoolean("favorited");
        tweet.retweetCount = jsonObject.getInt("retweet_count");
        tweet.retweetStatus = jsonObject.getBoolean("retweeted");

        return tweet;
    }


}
