package com.kohinoor.sandy.kohinnor.Activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.kohinoor.sandy.kohinnor.CustomClasses.ConnectivityReceiver;
import com.kohinoor.sandy.kohinnor.CustomClasses.MyApplication;
import com.kohinoor.sandy.kohinnor.R;
import com.squareup.picasso.Picasso;

public class NewsArticalActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    private TextView mArtical;
    private TextView mArtcalDate;
    private ImageView mArtcalImage;
    private TextView mArticalHeading;
    private ProgressDialog mProgressDialog;

    private InterstitialAd mInterstitialAd;
    private boolean AdClosed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_artical);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // toolbar.setTitle(getString(R.string.CurrentAffairs));
        setSupportActionBar(toolbar);

        // toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AdClosed = false;
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();

        mArtical = (TextView) findViewById(R.id.artical);
        mArtcalDate = (TextView) findViewById(R.id.articalDate);
        mArtcalImage = (ImageView) findViewById(R.id.imageName);
        mArticalHeading = (TextView) findViewById(R.id.articalHeading);

        Bundle bundle = getIntent().getExtras();

        if(bundle!=null)
        {
            mArtical.setText(bundle.getString("NewsData"));
            mArtcalDate.setText(bundle.getString("Date"));
            mArticalHeading.setText(bundle.getString("Heading"));
            String imageUrl = bundle.getString("ImageUrl");
            if(!imageUrl.equals("")) {
                Picasso.with(this)
                        .load(imageUrl)
                        .resize(800, 320)
                        .placeholder(R.mipmap.ic_default_image)
                        .error(R.mipmap.ic_default_image)
                        .into(mArtcalImage);
            }
           // mProgressDialog.dismiss();
        }
        getSupportActionBar().setTitle(R.string.CurrentAffairs);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {

        if (AdClosed) {
            AdClosed = false;
            super.onBackPressed();
            finish();
        } else {
            showInterstitial();
        }
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }
    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
        return isConnected;
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected){
        showSnack(isConnected);

    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message = "Sorry! Not connected to internet";
        int color = Color.WHITE;


        if (isConnected) {

            message = "Good! Connected to Internet";
            color = Color.WHITE;
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.fab), message, Snackbar.LENGTH_SHORT);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            sbView.setBackgroundColor(getResources().getColor(R.color.Green));
            snackbar.show();

        } else {
            message = "Sorry! Could not connect to internet";
            color = Color.WHITE;
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.fab), message, Snackbar.LENGTH_INDEFINITE);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            sbView.setBackgroundColor(getResources().getColor(R.color.Red));
            snackbar.show();

        }


    }
    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

            }

            @Override
            public void onAdClosed() {
                // Proceed to the next level.
                AdClosed = true;
            }
        });
        return interstitialAd;
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            AdClosed = true;
            onBackPressed();
        }
    }

    private void loadInterstitial() {
        // Disable the next level button and load the ad.
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest);
    }
}
