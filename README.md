# OnewayTwitter

最低限のTwitterの機能を備えたAndroidアプリ

## 機能

- ✔アプリ起動時にOAuth認証をして自分のTwitterアカウントと連携する
- ✔Tweet機能
- ✔自分のタイムラインを3件取得して表示
- ✔写真付きTweet

## アプリケーションの設計

- 事前にTwitterにアプリケーション登録しておいたConsumer KeyとConsumer Secretを用いてOAuth認証を行い,
Access TokenとAccess Token Secretを取得する
- 取得したアクセストークンSharedPreferenceに書き込むことで次回起動時に認証なしで使用できるようにする
- 取得したアクセストークンを用いてタイムラインの取得やツイートを行う
- SharedPreferenceに書き込まれたアクセストークンを消去することでログアウト処理を実装する

## 画面構成

![view.img](https://github.com/mryyomutga/OnewayTwitter/blob/master/image/view.png)

## Libraries

- [Twitter4j](http://twitter4j.org/ja/index.html)
- [SmartImageView](http://loopj.com/android-smart-image-view/)


## 更新情報
- 2018/01/15

	MainActivityが裏で動いてるせいか、OAuthActivityに遷移しても認証前の段階でタイムラインを取得しようとして発生する例外の修正

- 2018/01/18

    ButtonをImageButtonに変更

## 参考

- http://twitter4j.org/ja/index.html
- https://akira-watson.com/android/activity-1.html
- https://qiita.com/HideMatsu/items/2e6caec8265bcf2a2dcb
- https://qiita.com/icchi_h/items/8ce738ce8511ef69c799
- http://ojed.hatenablog.com/entry/2015/12/05/161013
- https://qiita.com/Yuki_Yamada/items/f8ea90a7538234add288
- https://qiita.com/kskso9/items/1ee5b1dbe996402ae94b
- https://qiita.com/Tsumugi/items/47f31bb7351979a45653
- http://mltmdkana.hatenablog.com/entry/2016/11/04/234003
- http://mltmdkana.hatenablog.com/entry/2016/11/06/143739
- https://qiita.com/gabu/items/673288c3a5b39f89aa92
- https://qiita.com/gabu/items/53857fcfa871b921af45
- https://qiita.com/kskso9/items/1ee5b1dbe996402ae94b
- https://akira-watson.com/android/asynctask.html
- https://blanktar.jp/blog/2017/06/android-wear-dont-show-imageview.html

## Memo

- TweetActivityでImageButtonを、`app:srcCompat="@drawable/image"`で表示できなかったため、
`android:src="@drawable/image"`を追加した

- カメラなどの機能を使う場合は、<u>AndroidManifest.xml</u>にパーミッションの設定を記述するだけではなく、端末側でもアプリの設定で権限を与える必要がある
