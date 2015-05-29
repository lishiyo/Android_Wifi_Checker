package com.cziyeli.project_one;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by connieli on 5/29/15.
 */
public class WebviewSubreddit extends Activity {
    private WebView mWebView;
    private String url;
    private static final String DEBUG_TAG = "myapp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subreddit);

        mWebView = (WebView) findViewById(R.id.activity_main_webview);
        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new WebViewClient());

        Bundle data = getIntent().getExtras();
        url = data.getString("url");

        mWebView.loadUrl(url);
    }
}
