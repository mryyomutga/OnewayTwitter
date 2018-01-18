package jp.ac.kanazawait.onewaytwitter;

import android.content.Context;
import android.content.SharedPreferences;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * Created by mryyomutga on 2017/12/28.
 */

public class TwitterUtils {
    // SharedPrerence用キー
    private static final String TOKEN = "token";
    private static final String TOKEN_SECRET = "token_secret";
    private static final String PREF_NAME = "twitter_access_token";

    // Twitterオブジェクトのインスタンス生成
    public static Twitter getTwitterInstance(Context context) {
        // XMLに記述したキーの取得
        String consumerKey = context.getString(R.string.twitter_consumer_key);
        String consumerSecret = context.getString(R.string.twitter_consumer_secret);

        TwitterFactory factory = new TwitterFactory();
        Twitter twitter = factory.getInstance();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);

        // トークンの設定
        if(hasAccessToken(context)) {
            twitter.setOAuthAccessToken(loadAccessToken(context));
        }
        return twitter;
    }

    // トークンの保持
    public static void storeAccessToken(Context context, AccessToken accessToken) {
        // トークンの設定
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TOKEN, accessToken.getToken());
        editor.putString(TOKEN_SECRET, accessToken.getTokenSecret());

        // 取得したトークンの保存
        editor.apply();
    }

    // トークンの破棄
    public static void removeAccessToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    // トークンの読み出し
    public static AccessToken loadAccessToken(Context context) {
        // preferenceからトークンの読み出し
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String token = preferences.getString(TOKEN, null);
        String tokenSecret = preferences.getString(TOKEN_SECRET, null);
        if(token != null && tokenSecret != null) {
            return new AccessToken(token, tokenSecret);
        } else {
            return null;
        }
    }

    // トークンの有無
    public static boolean hasAccessToken(Context context) {
        return loadAccessToken(context) != null;
    }
}
