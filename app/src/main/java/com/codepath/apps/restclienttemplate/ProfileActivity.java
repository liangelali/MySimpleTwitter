package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {

    TwitterClient client;
    User myself;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        client = TwitterApp.getRestClient();
        client.getUserInfo(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    myself = User.fromJSON(response);
                    getSupportActionBar().setTitle(myself.screenName);

                    // create the user fragment
                    UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(myself.screenName);
                    // display the user timeline fragment dynamically

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                    //make change
                    ft.replace(R.id.flContainer, userTimelineFragment);

                    // commit
                    ft.commit();

                    //populate the user headline
                    populateUserHeadline(user);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public void populateUserHeadline (User user) {
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagline = (TextView) findViewById(R.id.tvName);
        TextView tvFollowers = (TextView) findViewById(R.id.tvName);
        TextView tvFollowing = (TextView) findViewById(R.id.tvName);
    }
}
