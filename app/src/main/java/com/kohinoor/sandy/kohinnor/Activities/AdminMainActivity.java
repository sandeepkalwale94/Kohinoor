package com.kohinoor.sandy.kohinnor.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kohinoor.sandy.kohinnor.CustomClasses.ConnectivityReceiver;
import com.kohinoor.sandy.kohinnor.CustomClasses.MyApplication;
import com.kohinoor.sandy.kohinnor.R;

public class AdminMainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    private Button mUploadNews;
    private Button mUploadPdf;
    private Button mUploadQPaper;
    private Button mUploadArtical;
    Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.UploadDatabase);

        //toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUploadNews = (Button) findViewById(R.id.upload_news);
        mUploadPdf = (Button) findViewById(R.id.upload_pdf);
        mUploadQPaper = (Button) findViewById(R.id.upload_qpaper);
        //mUploadArtical = (Button) findViewById(R.id.upload_articals);



        mUploadNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start Uplaod News Fragment
                Intent intent = new Intent(AdminMainActivity.this, AdminActivity.class);
                startActivity(intent);

            }
        });

        mUploadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start Upload pdf fragment
                Intent intent = new Intent(AdminMainActivity.this, UploadPdfActivity.class);
                startActivity(intent);
            }
        });

        mUploadQPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start Upload QPaper
                Intent intent = new Intent(AdminMainActivity.this, UploadQpaperActivity.class);
                startActivity(intent);


            }
        });

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

        super.onBackPressed();
        finish();

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

}
