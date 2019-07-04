package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;
    Context context;

    private static TwitterClient client;

    // pass in the Tweets array in the constructor
    public TweetAdapter(List<Tweet> tweets) {
        mTweets = tweets;
    }

    // for each row, inflate the layout and cache references into ViewHolder

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        context = parent.getContext();

        client = TwitterApp.getRestClient(context);

        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get the data according to position
        final Tweet tweet = mTweets.get(position);
        final ViewHolder holder2 = holder;
        // populate the views according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);

        holder.tvCreatedAt.setText(getRelativeTimeAgo(tweet.createdAt));

        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);

        holder.tvLikeCount.setText(String.valueOf(tweet.likeCount));
        if (tweet.likeStatus)
            holder.ivLikeBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_vector_heart));
        else
            holder.ivLikeBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_vector_heart_stroke));

        final long uid = tweet.uid;


        holder.ivLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TweetAdapter", "Like Button was CLICKED");
                if (tweet.likeStatus) {
                    client.unlikeTweet(uid, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            tweet.likeStatus = false;
                            tweet.likeCount -= 1;
                            holder2.ivLikeBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_vector_heart_stroke));
                            holder2.tvLikeCount.setText(String.valueOf(tweet.likeCount));
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Log.e("TweetAdapter", "Failed to unlike tweet", error);
                        }
                    });
                }
                else {
                    client.likeTweet(uid, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            tweet.likeStatus = true;
                            tweet.likeCount += 1;
                            holder2.ivLikeBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_vector_heart));
                            holder2.tvLikeCount.setText(String.valueOf(tweet.likeCount));
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Log.e("TweetAdapter", "Failed to like tweet", error);
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // create ViewHolder class

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvCreatedAt;
        public ImageView ivLikeBtn;
        public TextView tvLikeCount;


        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvCreatedAt = (TextView) itemView.findViewById(R.id.tvCreatedAt);
            ivLikeBtn = (ImageView) itemView.findViewById(R.id.ivLikeBtn);
            tvLikeCount = (TextView) itemView.findViewById(R.id.tvLikeCount);
        }
    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }
}
