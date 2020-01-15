package com.kohinoor.sandy.kohinnor.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kohinoor.sandy.kohinnor.R;

public class StudyDataSelectorActivity extends AppCompatActivity {

    private ImageView mItem_1_Image;
    private ImageView mItem_2_Image;
    private ImageView mItem_3_Image;

    private TextView mItem_1_Text;
    private TextView mItem_2_Text;
    private TextView mItem_3_Text;

    private String MaterialType;
    private String subItem;
    private String notesType;
    private String mFlowFrom;

    private InterstitialAd mInterstitialAd;
    private boolean AdClosed;


    private String articleHeading;
    private String articleDate;
    private String articleUrl;
    private String articleData;

    private int QuetionCount;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_data_selector);

        mItem_1_Image = (ImageView) findViewById(R.id.item_1_image);
        mItem_2_Image = (ImageView) findViewById(R.id.item_2_image);
        mItem_1_Text = (TextView) findViewById(R.id.item_1_text);
        mItem_2_Text = (TextView) findViewById(R.id.item_2_text);
        mItem_3_Image = (ImageView) findViewById(R.id.item_3_image);
        mItem_3_Text = (TextView) findViewById(R.id.item_3_text);


        database = FirebaseDatabase.getInstance();

        AdClosed = false;
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        // toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            MaterialType = bundle.getString("MaterialType");
            subItem = bundle.getString("SubItem");
            mFlowFrom = bundle.getString("FlowFrom");
        }


        if (mFlowFrom.equals(getString(R.string.PracticeQuiz))) {

            getSupportActionBar().setTitle(subItem);
            //TODO SHOW PRACTICE QUETION COUNT IMAGES 5 10 20

            mItem_1_Image.setImageDrawable(getResources().getDrawable(R.mipmap.ic_five));//
            mItem_1_Text.setText(getString(R.string.Quetiones));
            mItem_2_Image.setImageDrawable(getResources().getDrawable(R.mipmap.ic_ten));
            mItem_2_Text.setText(getString(R.string.Quetiones));
            mItem_3_Image.setImageDrawable(getResources().getDrawable(R.mipmap.ic_twenty));
            mItem_3_Text.setText(getString(R.string.Quetiones));

            mItem_1_Image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Start PDF ACtivity
                    notesType = getString(R.string.QuetionSet);
                    QuetionCount = 5;
                    displayQuizListActivity(MaterialType, subItem, notesType, QuetionCount);
                }
            });
            mItem_2_Image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Start PDF ACtivity
                    notesType = getString(R.string.QuetionSet);
                    QuetionCount = 10;
                    displayQuizListActivity(MaterialType, subItem, notesType, QuetionCount);
                }
            });


            mItem_3_Image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Start PDF ACtivity
                    notesType = getString(R.string.QuetionSet);
                    QuetionCount = 20;
                    displayQuizListActivity(MaterialType, subItem, notesType, QuetionCount);
                }
            });


        } else {
            //setHeader(subItem);
            getSupportActionBar().setTitle(subItem);
            mItem_1_Image.setImageDrawable(getResources().getDrawable(R.mipmap.ic_pdf));
            mItem_1_Text.setText(getString(R.string.PDFNotes));
            mItem_2_Image.setImageDrawable(getResources().getDrawable(R.mipmap.ic_quetion_paper));
            mItem_2_Text.setText(getString(R.string.QuetionSet));
            mItem_3_Image.setImageDrawable(getResources().getDrawable(R.mipmap.ic_books));
            mItem_3_Text.setText(getString(R.string.Books));


            mItem_1_Image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Start PDF ACtivity
                    notesType = getString(R.string.PDFNotes);
                    displayStudyDataActivity(MaterialType, subItem, notesType);
                }
            });

            mItem_2_Image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Start QuetionPaper ACtivity
                    notesType = getString(R.string.QuetionSet);
                    displayStudyDataActivity(MaterialType, subItem, notesType);

                }
            });
            mItem_3_Image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Start QuetionPaper ACtivity
                    notesType = getString(R.string.Books);
                    displayStudyDataActivity(MaterialType, subItem, notesType);

                }
            });

        }

    }

    public void displayQuizListActivity(String materialType, String subItem, String notesType, int quetionCount) {
        Intent intent = new Intent(this, QuizListActivity.class);
        intent.putExtra("StudyMaterialType", materialType);
        intent.putExtra("subItem", subItem);
        intent.putExtra("notesType", notesType);
        intent.putExtra("QuetionCount", quetionCount);
        startActivity(intent);
    }

    public void displayStudyDataActivity(String materialType, String subItem, String notesType) {
        Intent intent = new Intent(this, StudyDataActivity.class);
        intent.putExtra("StudyMaterialType", materialType);
        intent.putExtra("subItem", subItem);
        intent.putExtra("notesType", notesType);
        startActivity(intent);
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