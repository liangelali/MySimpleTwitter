package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 20;
    TwitterClient client;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String username = getIntent().getStringExtra("username");
        String inReplyTo = getIntent().getStringExtra("in_reply_to");
        int code = getIntent().getIntExtra("code", 0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient();

        // find the RecyclerView
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);

        // init the arrayList (data source)
        tweets = new ArrayList<>();

        // construct the adapter from this data source
        tweetAdapter = new TweetAdapter(tweets);

        // RecyclerView setup (layout manager, use adapter)
        rvTweets.setLayoutManager(new LinearLayoutManager(this));

        // set the adapter
        rvTweets.setAdapter(tweetAdapter);

        populateTimeline();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == 1) {
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
            tweets.add(0, tweet);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);
        }

        // check request code and result code first

        // Use data parameter
        Tweet tweet = (Tweet) data.getSerializableExtra("tweet");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onComposeAction(View view) {
        Intent intent = new Intent(TimelineActivity.this, ComposeActivity.class);
        startActivityForResult(intent, 1);
    }

    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                Log.d("TwitterClient", response.toString());
                // iterate through the JSON Array
                // for each entry, deserialize the JSON object

                for (int i = 0; i < response.length(); i++) {
                    // convert each object to a Tweet model
                    // add that Tweet model to our data source
                    // notify the adapter that we've added an item
                    try {
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        tweets.add(tweet);
                        tweetAdapter.notifyItemInserted(tweets.size() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();            }
        });
    }

    public void onSubmit(View v) {
        EditText et_simple = (EditText) findViewById(R.id.et_simple);
        // Prepare data intent
        Intent data = new Intent();
        // Pass relevant data back as a result
        data.putExtra("name", et_simple.getText().toString());
        data.putExtra("code", 200); // ints work too
        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }
}
