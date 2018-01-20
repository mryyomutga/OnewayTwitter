package jp.ac.kanazawait.onewaytwitter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Created by mryyomutga on 2017/12/28.
 */

public class OAuthActivity extends Activity {
    private String callBackURL;
    private Twitter twitter;
    private RequestToken requestToken;

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        // CallBack用URL設定
        callBackURL = getString(R.string.twitter_callback_url);
        // Twitterインスタンス取得
        twitter = TwitterUtils.getTwitterInstance(this);

        // Authorize
        startAuthorize();
    }

    // OAuth認証開始メソッド
    private void startAuthorize(){
        // AsyncTaskでの非同期処理
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    // リクエストトークン取得
                    requestToken = twitter.getOAuthRequestToken(callBackURL);
                    return requestToken.getAuthorizationURL();
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String url) {
                if(url != null) {
                    // URLへアクティビティ遷移
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } else {
                    Toast.makeText(OAuthActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        };
        task.execute();
    }

    @Override
    public void onNewIntent(Intent intent){
        if(intent == null || intent.getData() == null || !intent.getData().toString().startsWith(callBackURL)) {
            return;
        }
        // Verifierを取得
        String verifier = intent.getData().getQueryParameter("oauth_verifier");
        @SuppressLint("StaticFieldLeak")
        AsyncTask<String, Void, AccessToken> task = new AsyncTask<String, Void, AccessToken>() {
            @Override
            protected AccessToken doInBackground(String... params) {
                try {
                    // アクセストークン取得
                    return twitter.getOAuthAccessToken(requestToken, params[0]);
                } catch(TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(AccessToken accessToken) {
                // トークン登録
                if(accessToken != null) {
                    // 認証成功
                    showToast("認証成功");
                    showToast("ログインしました(´ω｀)");
                    successOAuth(accessToken);
                } else {
                    // 認証失敗
                    showToast("認証失敗");
                }
            }
        };
        task.execute(verifier);
    }

    private void successOAuth(AccessToken accessToken) {
        // トークン登録メソッド呼び出し
        TwitterUtils.storeAccessToken(this, accessToken);

        // MainActivityへ遷移
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);

        // このアクティビティを終了
        finish();
    }

    // トースト表示メソッド
    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
