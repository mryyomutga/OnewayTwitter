package jp.ac.kanazawait.onewaytwitter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.*;
import android.widget.EditText;
import android.widget.Toast;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by mryyomutga on 2017/12/29.
 */

public class TweetActivity extends Activity implements OnClickListener {
    private Twitter twitter;
    private EditText tweets;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        twitter = TwitterUtils.getTwitterInstance(this);

        tweets = (EditText) findViewById(R.id.tweet_text);

        findViewById(R.id.tweet_button).setOnClickListener(this);
    }

    private void tweet(){
        new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    twitter.updateStatus(params[0]);
                    return true;
                } catch (TwitterException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result){
                if (result) {
                    showToast("ツイートが完了しました!! (・ω・)ノ");
                } else {
                    showToast("ツイートが失敗しました… (T_T)");
                }
            }
        }.execute(tweets.getText().toString());

    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v){
        if(v != null) {
            switch (v.getId()) {
                // Click Tweet Button
                case R.id.tweet_button:
                    tweet();
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
