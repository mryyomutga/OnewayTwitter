package jp.ac.kanazawait.onewaytwitter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.*;
import android.view.View.*;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    List<Status> statuses = Collections.emptyList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Twitterアカウントの認証
        if(!TwitterUtils.hasAccessToken(this)) {
            Intent intent = new Intent(this, OAuthActivity.class);
            startActivity(intent);
            finish();
        }

        Twitter twitter = TwitterUtils.getTwitterInstance(this);

        getTimeLine(twitter);

        findViewById(R.id.transition_tweet_activity).setOnClickListener(this);
    }

    @SuppressLint("StaticFieldLeak")
    private void getTimeLine(final Twitter twitter) {
        new AsyncTask<Void, Void, List<Status>>() {
            @Override
            protected List<twitter4j.Status> doInBackground(Void... voids) {
                // Pagingの設定(要はリクエスト時に含めるパラメータ)
                Paging paging = new Paging();
                paging.setCount(3);             // つぶやきを3件取得
                try {
                    statuses = twitter.getUserTimeline(paging);
                    return statuses;
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }
            // 取得したタイムラインのふるまい
            protected void onPostExecute(List<twitter4j.Status> statuses) {
                for(twitter4j.Status status : statuses) {
                    showToast(status.getText());
                }
            }
        }.execute();
    }

    @Override
    public void onClick(View v) {
        if(v != null) {
            switch (v.getId()) {
                // Click Tweet
                case R.id.transition_tweet_activity:
                    Intent tweetIntent = new Intent(getApplication(), TweetActivity.class);
                    startActivity(tweetIntent);
                    break;
                default:
                    break;
            }
        }
    }

    private void showToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}
