package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    public static final String RESULT_TWEET_KEY = "result_tweet";
    TextInputLayout tilTweetInput;
    EditText etTweetInput;
    Button btnSend;
    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        tilTweetInput = (TextInputLayout) findViewById(R.id.tilTweetInput);
        etTweetInput = tilTweetInput.getEditText();
        btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTweet();
            }
        });
    }

    private void sendTweet() {
        client = TwitterApp.getRestClient(this);
        client.sendTweet(etTweetInput.getText().toString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    try {
                        Log.i("ComposeActivity", "Tweet sent!");
                        // parsing response
                        JSONObject responseJson = new JSONObject(new String(responseBody));
                        Tweet resultTweet = Tweet.fromJSON(responseJson);

                        // return result to calling activity
                        Intent resultData = new Intent();
                        // THIS IS WRONG! TODO Use parceler
//                        resultData.putExtra(RESULT_TWEET_KEY, resultTweet.toString());
                        resultData.putExtra(RESULT_TWEET_KEY, Parcels.wrap(resultTweet));

                        setResult(RESULT_OK, resultData);
                        finish();
                    } catch (JSONException e)
                    {
                        Log.e("ComposeActivity", "Error parsing response", e);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("ComposeActivity", "Error sending tweet", error);
            }
        });
    }

}