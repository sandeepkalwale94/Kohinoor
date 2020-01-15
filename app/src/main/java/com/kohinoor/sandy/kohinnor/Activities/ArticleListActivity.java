package com.kohinoor.sandy.kohinnor.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kohinoor.sandy.kohinnor.Adapter.ArticleListAdapter;
import com.kohinoor.sandy.kohinnor.Adapter.StudyMFragAdapter;
import com.kohinoor.sandy.kohinnor.CustomClasses.ConnectivityReceiver;
import com.kohinoor.sandy.kohinnor.CustomClasses.MyApplication;
import com.kohinoor.sandy.kohinnor.Interface.ClickListener;
import com.kohinoor.sandy.kohinnor.Model.ArticleData;
import com.kohinoor.sandy.kohinnor.Model.ArticleDataBase;
import com.kohinoor.sandy.kohinnor.Model.StudyMData;
import com.kohinoor.sandy.kohinnor.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class ArticleListActivity extends AppCompatActivity implements ClickListener, ConnectivityReceiver.ConnectivityReceiverListener{

    Context context;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    //RecyclerView.Adapter recyclerViewAdapter;
    private ArticleListAdapter mAdapter;
    RecyclerView.LayoutManager recylerViewLayoutManager;
    FirebaseDatabase database;
    DatabaseReference mDatabaseReference;

    private List<ArticleData> mArticleData;

    private String StudyMaterialType;
    private String subItem;
    public String notesType;

    private boolean isConnected;

    private InterstitialAd mInterstitialAd;
    private boolean AdClosed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AdClosed = false;
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();

        database = FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference();
        mArticleData = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();

        if(bundle!=null)
        {
            StudyMaterialType = bundle.getString("StudyMaterialType");
            subItem = bundle.getString("subItem");
            notesType = bundle.getString("notesType");
        }
        readArticleData();
        getSupportActionBar().setTitle(subItem);
    }

    @Override
    public void onNewIntent(Intent intent){
        Bundle extras = intent.getExtras();
        String tabNumber;

        if(extras != null) {
            StudyMaterialType = extras.getString("StudyMaterialType");
            subItem = extras.getString("subItem");
            notesType = extras.getString("notesType");

        } else {

        }
        readArticleData();
        getSupportActionBar().setTitle(subItem);
    }
    @Override
    public void ItemClicked(View view, int position) {


        String mArticleHeading = mArticleData.get(position).getmArticleTitle();
        String mArticleDesc = mArticleData.get(position).getmArticleData();

        Intent intent = new Intent(this, ImpArticleActivity.class);
        intent.putExtra("Heading",mArticleHeading);
        intent.putExtra("Desc",mArticleDesc);
        startActivity(intent);
    }
    private void readArticleData()
    {

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("StudyMaterial/"+notesType+"/"+StudyMaterialType+"/"+subItem + "/");

        //retrieving upload data from firebase database
        final Long timeStamp = new Date().getTime();
        mDatabaseReference.orderByChild("mTimeStamp").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mArticleData = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ArticleData upload = postSnapshot.getValue(ArticleData.class);
                    mArticleData.add(upload);
                }



                //Storing data in ArticleDatabase Class
               // ArticleDataBase articleDataBase = new ArticleDataBase();
              //  articleDataBase.ArticleDataBase(mArticleData);
                context = getApplicationContext();

                recyclerView = (RecyclerView) findViewById(R.id.recyclerview_2);

                if(mArticleData.size() == 0)
                {
                    mArticleData.add(new ArticleData("No Data","",timeStamp));
                }
                mAdapter = new ArticleListAdapter(context,mArticleData);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                //recyclerView.setItemAnimator(new DefaultItemAnimator());
                mAdapter.setClickListener(ArticleListActivity.this);
                /*recyclerView.addItemDecoration(new DividerItemDecoration(ArticleListActivity.this,
                        R.drawable.divider));*/
                /*recyclerView.addItemDecoration(new DividerItemDecoration(ArticleListActivity.this,
                        DividerItemDecoration.VERTICAL));
                DividerItemDecoration itemDecorator= new DividerItemDecoration(ArticleListActivity.this,
                        DividerItemDecoration.VERTICAL);
                itemDecorator.setDrawable(ContextCompat.getDrawable(ArticleListActivity.this, R.drawable.divider));
*/
                recyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*if(!checkConnection())
        {
            mArticleData.add(new ArticleData("No Data",""));
            mAdapter = new ArticleListAdapter(context,mArticleData);
        }*/

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
