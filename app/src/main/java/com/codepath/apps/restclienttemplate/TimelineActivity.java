package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;

public class TimelineActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 20;

    TweetsListFragment fragmentTweetsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // String username = getIntent().getStringExtra("username");
        // String inReplyTo = getIntent().getStringExtra("in_reply_to");
        // int code = getIntent().getIntExtra("code", 0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        fragmentTweetsList = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // REQUEST_CODE is defined above
//        if (resultCode == RESULT_OK && requestCode == 1) {
//            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
//            tweets.add(0, tweet);
//            tweetAdapter.notifyItemInserted(0);
//            rvTweets.scrollToPosition(0);
//        }

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
