package jp.ac.kanazawait.onewaytwitter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.*;

import twitter4j.Twitter;

/**
 * Created by mryyomutga on 2017/12/29.
 */

public class TweetActivity extends Activity implements OnClickListener {
    private Twitter twitter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        twitter = TwitterUtils.getTwitterInstance(this);

        findViewById(R.id.tweet_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if(v != null) {
            switch (v.getId()) {
                // Click Tweet Button
                case R.id.tweet_button:
                    Intent mainIntent = new Intent(getApplication(), MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    }

}
