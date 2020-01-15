package com.kohinoor.sandy.kohinnor.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.kohinoor.sandy.kohinnor.R;

public class TODODeleteActivity extends AppCompatActivity {

    WebView webView;

    public String fileName = "psi_sti.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tododelete);

        webView = (WebView) findViewById(R.id.simpleWebView);
        // displaying content in WebView from html file that stored in assets folder
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/" + fileName);

    }
}
