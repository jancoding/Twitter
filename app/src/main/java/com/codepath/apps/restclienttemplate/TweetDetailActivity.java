package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.other.TwitterApp;
import com.codepath.apps.restclienttemplate.other.TwitterClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcels;

import okhttp3.Headers;

public class TweetDetailActivity extends AppCompatActivity {

    ImageView ivProfile;
    TextView tvScreenName;
    TextView tvBody;
    TextView tvTime;
    ImageView ivEntity;
    ImageButton btnRetweet;
    ImageButton btnLike;
    Tweet tweet;
    private int liked = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        // bind to view
        ivProfile = findViewById(R.id.ivProfile);
        tvScreenName = findViewById(R.id.tvScreenName);
        tvBody = findViewById(R.id.tvBody);
        tvTime = findViewById(R.id.tvTime);
        btnRetweet = findViewById(R.id.btnRetweet);
        ivEntity = findViewById(R.id.ivEntity);
        btnLike = findViewById(R.id.btnLike);

        btnRetweet.setImageResource(R.drawable.ic_vector_retweet);
        btnLike.setImageResource(R.drawable.ic_vector_heart_stroke);

        btnLike.setColorFilter(Color.rgb(29,161,242), android.graphics.PorterDuff.Mode.SRC_IN);
        btnRetweet.setColorFilter(Color.rgb(29,161,242), android.graphics.PorterDuff.Mode.SRC_IN);


        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwitterClient client = TwitterApp.getRestClient(TweetDetailActivity.this);

                if (liked == 0) {
                    btnLike.setImageResource(R.drawable.ic_vector_heart);
                    liked = 1;
                    client.favoriteTweet(tweet, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.d("TweetDetailActivity", "succesfully liked");
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.d("TweetDetailActivity", "failed to liked");
                        }
                    });

                } else {
                    btnLike.setImageResource(R.drawable.ic_vector_heart_stroke);
                    liked = 0;
                    client.unlikeTweet(tweet, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.d("TweetDetailActivity", "succesfully liked");
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.d("TweetDetailActivity", "failed to liked");
                        }
                    });
                }
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1DA1F2")));
        actionBar.setTitle("Twitter");


        btnRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwitterClient client = TwitterApp.getRestClient(TweetDetailActivity.this);
                client.reTweet(tweet, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.d("TweetDetailActivity", "succesfully retweeted");
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.d("TweetDetailActivity", "failed to retweet");
                    }
                });

            }
        });


        // get information from intent
        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        tvBody.setText(tweet.body);
        tvScreenName.setText(tweet.user.screenName);
        tvTime.setText(tweet.time);
        if (tweet.mediaUrl != "") {
            Log.d("TweetsAdapter", "loading media");
            Glide.with(this).load(tweet.mediaUrl + ":thumb").into(ivEntity);
        } else {
            ivEntity.setVisibility(View.GONE);
        }

        Glide.with(this).load(tweet.user.profileImageUrl).into(ivProfile);
    }
}