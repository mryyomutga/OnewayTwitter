package jp.ac.kanazawait.onewaytwitter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.*;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by mryyomutga on 2017/12/29.
 */

public class TweetActivity extends Activity implements OnClickListener {
    private Twitter twitter;
    private EditText tweets;
    private final static int RESULT_CAMERA = 1;
    private Uri imageUri;
    private String tweetImagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        twitter = TwitterUtils.getTwitterInstance(this);

        tweets = (EditText) findViewById(R.id.tweet_text);

        findViewById(R.id.tweet_button).setOnClickListener(this);
        findViewById(R.id.camera_button).setOnClickListener(this);
    }

    private void tweet(){
        new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    if (imageUri != null) {
                        twitter.updateStatus(new StatusUpdate(params[0]).media(new File(imageUri.getPath())));
                        imageUri = null;
                    } else {
                        twitter.updateStatus(params[0]);
                    }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CAMERA) {
            switch (resultCode) {
                case RESULT_OK:
                    tweetImagePath = imageUri.getPath();
                    showToast("Success " + tweetImagePath);
                    break;
                case RESULT_CANCELED:
                    showToast("Canceled");
                    break;
                default:
                    break;
            }
        }
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
                case R.id.camera_button:
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // 画像の保存先のディレクトリ
                    File mediaStorageDir = new File(
                            Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES), "MyApp");
                    if (!mediaStorageDir.exists()) {
                        if (!mediaStorageDir.mkdirs()) {
                            break;
                        }
                    }
                    // 撮影した写真のタイムスタンプ
                    String timeStamp = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss").format(new Date());
                    // 撮影した画像のファイル名
                    File mediaFile = new File(mediaStorageDir.getPath() + File.separator + timeStamp + ".png");
                    imageUri = Uri.fromFile(mediaFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaFile));
                    startActivityForResult(intent, RESULT_CAMERA);
                    break;
                default:
                    break;
            }
        }
    }
}
