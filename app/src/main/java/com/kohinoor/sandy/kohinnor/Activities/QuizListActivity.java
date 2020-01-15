package com.kohinoor.sandy.kohinnor.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kohinoor.sandy.kohinnor.Adapter.QuizListAdapter;
import com.kohinoor.sandy.kohinnor.CustomClasses.ConnectivityReceiver;
import com.kohinoor.sandy.kohinnor.CustomClasses.MyApplication;
import com.kohinoor.sandy.kohinnor.Interface.ClickListener;
import com.kohinoor.sandy.kohinnor.Model.QuizData;
import com.kohinoor.sandy.kohinnor.R;

import java.util.ArrayList;
import java.util.List;

public class QuizListActivity extends AppCompatActivity implements ClickListener,ConnectivityReceiver.ConnectivityReceiverListener{

    Context context;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    //RecyclerView.Adapter recyclerViewAdapter;
    private QuizListAdapter mAdapter;
    RecyclerView.LayoutManager recylerViewLayoutManager;
    FirebaseDatabase database;
    DatabaseReference mDatabaseReference;
    ProgressDialog mProgressDialog;

    private List<QuizData> mQuizData;
    private List<String> mQuizList;

    private InterstitialAd mInterstitialAd;
    private boolean AdClosed;

    private String StudyMaterialType;
    private String subItem;
    public  String notesType;
    private String mQuizNumber;
    private int QuetionCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.PracticeQuiz);
        //toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();

        AdClosed = false;
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();

        mQuizData = new ArrayList<>();
        mQuizList = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();


        if(bundle!=null)
        {
            StudyMaterialType = bundle.getString("StudyMaterialType");
            subItem = bundle.getString("subItem");
            notesType = bundle.getString("notesType");
            QuetionCount = bundle.getInt("QuetionCount");
        }
        readQuizList();
    }

    @Override
    public void ItemClicked(View view, int position) {

        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra("QuetionCount",QuetionCount);
        intent.putExtra("StudyMaterialType", StudyMaterialType);
        intent.putExtra("subItem", subItem);
        intent.putExtra("notesType", notesType);
        intent.putExtra("QuizNumber", position);
        startActivity(intent);
    }
    private void readQuizList()
    {

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("StudyMaterial/"+notesType+"/"+StudyMaterialType+"/"+subItem+"/");

        //retrieving upload data from firebase database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    /*for(DataSnapshot postSnapshot2 : postSnapshot.getChildren())
                    {*/
                        QuizData upload = postSnapshot.getValue(QuizData.class);
                        mQuizData.add(upload);
                   /* }

                    mQuizList.add(postSnapshot.getKey());*/
                }

                context = getApplicationContext();

                recyclerView = (RecyclerView) findViewById(R.id.recyclerview_quizlist);

                mAdapter = new QuizListAdapter(context,mQuizData, QuetionCount);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                mAdapter.setClickListener(QuizListActivity.this);
                recyclerView.addItemDecoration(new DividerItemDecoration(QuizListActivity.this,
                        DividerItemDecoration.VERTICAL));
                recyclerView.setAdapter(mAdapter);
                mProgressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mProgressDialog.dismiss();
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

        mProgressDialog.dismiss();
        if(AdClosed)
        {
            AdClosed = false;
            super.onBackPressed();
            finish();
        }
        else
        {
            showInterstitial();
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
