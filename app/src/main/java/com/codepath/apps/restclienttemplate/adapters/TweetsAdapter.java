package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.ProfileDetailActivity;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TweetDetailActivity;
import com.codepath.apps.restclienttemplate.other.TwitterApp;
import com.codepath.apps.restclienttemplate.other.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.List;

import okhttp3.Headers;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    Context context;
    List<Tweet> tweets;
    public static final int MAX_TWEET_LENGTH = 140;


    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    // For each row, inflate a layout
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    // Bind values based on the position of the element

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        // Get the data at position
        Tweet tweet = tweets.get(position);
        // Bind the tweet with view holder
        holder.bind(tweet);
    }



    @Override
    public int getItemCount() {
        return tweets.size();
    }


    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

    // Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvTime;
        ImageView ivEntity;
        EditText etReply;
        ImageButton btnReply;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfile);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvTime = itemView.findViewById(R.id.tvTime);
            ivEntity = itemView.findViewById(R.id.ivEntity);
            etReply = itemView.findViewById(R.id.etReply);
            btnReply = itemView.findViewById(R.id.btnReply);
            itemView.setOnClickListener(this);
        }


        public void bind(final Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screenName);
            tvTime.setText(tweet.time);
            etReply.setText("@" + tweet.user.screenName + " ");
            if (tweet.mediaUrl != "") {
                Log.d("TweetsAdapter", "loading media");
                Glide.with(context).
                        load(tweet.mediaUrl + ":thumb")
                        .into(ivEntity);
            } else {
                ivEntity.setVisibility(View.GONE);
            }


            btnReply.setColorFilter(Color.rgb(29,161,242), android.graphics.PorterDuff.Mode.SRC_IN);

            Glide.with(context)
                    .load(tweet.user.profileImageUrl)
                    .transform(new RoundedCornersTransformation(30, 0))
                    .into(ivProfileImage);
            btnReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tweetContent = etReply.getText().toString();
                    if (tweetContent.isEmpty()) {
                        Toast.makeText(itemView.getContext(), "Sorry, your tweet cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (tweetContent.length() > MAX_TWEET_LENGTH) {
                        Toast.makeText(itemView.getContext(), "Sorry, your tweet is too long", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    TwitterClient client = TwitterApp.getRestClient(itemView.getContext());

                    client.replyTweet(tweetContent, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i("TweetsAdapter", "OnSuccess to publish tweet");
                            try {
                                Tweet tweet = Tweet.fromJson(json.jsonObject);
                                Log.i("TweetsAdapter", "Published tweet says: " + tweet.body);
                                etReply.setText("");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e("TweetsAdapter", "onFailure to publish Tweet", throwable);
                        }
                    }, tweet);

                }
            });

            ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // intent to lead to a profile detail view that shows followers and following
                    Intent intent = new Intent(itemView.getContext(), ProfileDetailActivity.class);
                    intent.putExtra("User", Parcels.wrap(tweets.get(getAdapterPosition()).user));
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View view) {
            Log.d("TweetsAdapter", "in here");
            // create intent to pass tweet and go to detail view
            Tweet tweet = tweets.get(getAdapterPosition());
            Intent intent = new Intent(view.getContext(), TweetDetailActivity.class);
            intent.putExtra("tweet", Parcels.wrap(tweet));
            view.getContext().startActivity(intent);
        }
    }
}
