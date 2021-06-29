package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;
import org.w3c.dom.Text;

import okhttp3.Headers;

public class EditTweetDialogFragment extends DialogFragment{
    private EditText etTweet;
    private TextView tvRemain;
    private Button btnTweetNow;
    public static final int MAX_TWEET_LENGTH = 140;
    TwitterClient client;

    public interface EditTweetDialogListener {
        void onFinishEditDialog(Tweet tweet);
    }


    public EditTweetDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static EditTweetDialogFragment newInstance(String title) {
        EditTweetDialogFragment frag = new EditTweetDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_tweet, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvRemain = view.findViewById(R.id.tvRemain);
        btnTweetNow = view.findViewById(R.id.btnTweetNow);
        etTweet = view.findViewById(R.id.etTweet);
        client = TwitterApp.getRestClient(getActivity());
       // btnTweetNow.setOnEditorActionListener(this);

        etTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (140 - editable.toString().length() < 0) {
                    tvRemain.setTextColor(Color.RED);
                }
                tvRemain.setText(140 - editable.toString().length() + "/140");
            }
        });


        btnTweetNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make an API call to Twitter to tweet

                String tweetContent = etTweet.getText().toString();
                if (tweetContent.isEmpty()) {
                    Toast.makeText(getActivity(), "Sorry, your tweet cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tweetContent.length() > MAX_TWEET_LENGTH) {
                    Toast.makeText(getActivity(), "Sorry, your tweet is too long", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.i("EditTweetDialogFragment", "Published tweet going to say: " + tweetContent);

                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i("EditTweetDialogFragment", "OnSuccess to publish tweet");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i("EditTweetDialogFragment", "Published tweet says: " + tweet.body);
                            EditTweetDialogListener listener = (EditTweetDialogListener) getActivity();
                            listener.onFinishEditDialog(tweet);
                            dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e("EditTweetDialogFragment", "onFailure to publish Tweet", throwable);
                    }
                });


            }
        });

    }
}
