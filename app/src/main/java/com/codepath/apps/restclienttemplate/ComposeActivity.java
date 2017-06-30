package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.restclienttemplate.TwitterApp.getRestClient;

public class ComposeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        TwitterClient client = getRestClient();
    }

    public void createTweet(View view) {

        TwitterClient client = getRestClient();
        EditText et_simple = (EditText) findViewById(R.id.et_simple);
        Intent data = new Intent();
        data.putExtra("tweet", et_simple.getText().toString());
        client.sendTweet(et_simple.getText().toString(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                try {
                    Tweet tweet = Tweet.fromJSON(response);

                    Intent intent = new Intent();
                    intent.putExtra("tweet", Parcels.wrap(tweet));
                    setResult(RESULT_OK, intent);
                    finish();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent data = new Intent(ComposeActivity.this, TimelineActivity.class);

                Log.d("TwitterClient", response.toString());
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

    // REQUEST_CODE can be any value we like, used to determine the result type later
    private final int REQUEST_CODE = 20;
    // ComposeActivity, launching an activity for a result
    public void onClick(View view) {
        Intent i = new Intent(ComposeActivity.this, TimelineActivity.class);
        i.putExtra("mode", 2); // pass arbitrary data to launched activity
        startActivityForResult(i, REQUEST_CODE);
    }

    public void launchComposeView() {
        // first parameter is the context, second is the class of the activity to launch
        Intent i = new Intent(ComposeActivity.this, TimelineActivity.class);

        // put "extras" into the bundle for access in the second activity
        i.putExtra("username", "foobar");
        i.putExtra("in_reply_to", "george");
        i.putExtra("code", 400);

        startActivity(i); // brings up the second activity
    }

}

