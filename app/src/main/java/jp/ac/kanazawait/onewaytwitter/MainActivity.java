package jp.ac.kanazawait.onewaytwitter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.*;
import android.view.View.*;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

import java.util.List;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private Twitter twitter;
    private TweetItem tweetItem;
    ListView listView;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Twitterアカウントの認証
        if(!TwitterUtils.hasAccessToken(this)) {
            Intent intent = new Intent(this, OAuthActivity.class);
            startActivity(intent);
            finish();
        } else {
            twitter = TwitterUtils.getTwitterInstance(this);
            tweetItem = new TweetItem(this);
            listView = (ListView) findViewById(R.id.tweet_list);

            tweetItem.getTimeLine(twitter);

            getAccountInfo();

            findViewById(R.id.transition_tweet_activity).setOnClickListener(this);
        }
    }

    // アカウント情報の設定
    private void getAccountInfo() {
        new AsyncTask<Void, Void, User>() {
            @Override
            protected User doInBackground(Void... voids) {
                user = null;
                try {
                    user = twitter.verifyCredentials();
                    return user;
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }
            protected void onPostExecute(User user) {
                if (user != null) {
                    // name
                    TextView name = (TextView) findViewById(R.id.name);
                    name.setText(user.getName());
                    // screen Name
                    TextView screenName = (TextView) findViewById(R.id.screen_name);
                    screenName.setText("@" + user.getScreenName());
                    // description
                    TextView description = (TextView) findViewById(R.id.description);
                    description.setText(user.getDescription());
                    // friends
                    TextView friends = (TextView) findViewById(R.id.friends);
                    friends.setText("Friends : " + user.getFriendsCount());
                    // followers
                    TextView followers = (TextView) findViewById(R.id.followers);
                    followers.setText("Followers : " + user.getFollowersCount());
                    // icon
                    SmartImageView icon = (SmartImageView) findViewById(R.id.icon);
                    icon.setImageUrl(user.getProfileImageURL());
                }
            }
        }.execute();
    }

    private class TweetItem extends ArrayAdapter<Status> {
        private LayoutInflater inflater;

        public TweetItem(@NonNull Context context) {
            super(context, R.layout.tweet_item_view);
            inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.tweet_item_view, null);
            }
            Status item = getItem(position);
            // name
            TextView name = (TextView) convertView.findViewById(R.id.name);
            name.setText(item.getUser().getName());
            // screen name
            TextView screenName = (TextView) convertView.findViewById(R.id.screen_name);
            screenName.setText("@" + item.getUser().getScreenName());
            // tweet
            TextView text = (TextView) convertView.findViewById(R.id.text);
            text.setText(item.getText());
            // icon
            SmartImageView icon = (SmartImageView) convertView.findViewById(R.id.icon);
            icon.setImageUrl(item.getUser().getProfileImageURL());
            return convertView;
        }

        // タイムラインの取得
        @SuppressLint("StaticFieldLeak")
        private void getTimeLine(final Twitter twitter) {
            new AsyncTask<Void, Void, List<twitter4j.Status>>() {
                @Override
                protected List<twitter4j.Status> doInBackground(Void... params) {
                    // Pagingの設定(要はリクエスト時に含めるパラメータ)
                    Paging paging = new Paging();
                    paging.setCount(3);             // つぶやきを3件取得
                    try {
                        return twitter.getUserTimeline(paging);
                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
                // 取得したタイムラインのふるまい
                protected void onPostExecute(List<twitter4j.Status> statuses) {
                    if (statuses != null) {
                        for (twitter4j.Status status : statuses) {
                            tweetItem.add(status);
                            showToast(status.getText());
                        }
                        listView.setAdapter(tweetItem);
                    }
                }
            }.execute();
        }
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
