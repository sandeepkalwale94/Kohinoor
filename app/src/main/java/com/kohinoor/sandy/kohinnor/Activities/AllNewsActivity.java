package com.kohinoor.sandy.kohinnor.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.kohinoor.sandy.kohinnor.CustomClasses.ConnectivityReceiver;
import com.kohinoor.sandy.kohinnor.CustomClasses.MyApplication;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kohinoor.sandy.kohinnor.Adapter.AllNewsAdapter;
import com.kohinoor.sandy.kohinnor.Adapter.MyHomeRecyclerViewAdapter;
import com.kohinoor.sandy.kohinnor.Model.DataModel;
import com.kohinoor.sandy.kohinnor.Model.Database;
import com.kohinoor.sandy.kohinnor.Model.ExpandableData;
import com.kohinoor.sandy.kohinnor.Model.Repo;
import com.kohinoor.sandy.kohinnor.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;

public class AllNewsActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    private RecyclerView recyclerView;
    private ExpandableListView mExpandableListView;
    private Spinner spinner1,spinner2;

    private List<DataModel> mDatabase;
    private ArrayList<ExpandableData> CurrentMonthData;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private String mMonth;
    private String mYear;

    private Database data;

    private InterstitialAd mInterstitialAd;
    private boolean AdClosed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("All");
        mYear = getString(R.string.Y2018);
        mMonth = getString(R.string.Jan);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("NewsData");

        AdClosed = false;
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();


        mExpandableListView=(ExpandableListView) findViewById(R.id.expandableListView1);


        //SET ONCLICK LISTENER
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPos,
                                        int childPos, long id) {

               // Toast.makeText(getApplicationContext(), CurrentMonthData.get(groupPos).mExpnSubData.get(childPos).getmHeading(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),NewsArticalActivity.class);
                intent.putExtra("Heading", CurrentMonthData.get(groupPos).mExpnSubData.get(childPos).getmHeading());
                intent.putExtra("Date", CurrentMonthData.get(groupPos).mExpnSubData.get(childPos).getmDate());
                intent.putExtra("NewsData", CurrentMonthData.get(groupPos).mExpnSubData.get(childPos).getmNewsData());
                intent.putExtra("ImageUrl", CurrentMonthData.get(groupPos).mExpnSubData.get(childPos).getmImageUrl());
                startActivity(intent);
                return false;
            }
        });


        //Read All News Data
        myRef.orderByChild("TimeStamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                mDatabase = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    DataModel post = dataSnapshot1.getValue(DataModel.class);
                    mDatabase.add(post);
                    //String temp = "";
                }
                data = new Database();
                data.Database(mDatabase);

                CurrentMonthData=getData();

                //CREATE AND BIND TO ADAPTER
                AllNewsAdapter adapter=new AllNewsAdapter(getApplicationContext(), CurrentMonthData);
                mExpandableListView.setAdapter(adapter);

                /*mDatabase = data.getmDatabase();


                if(mDatabase.size() == 0)
                {
                    mDatabase.add(new DataModel("No Data","","","",(long)0));
                }*/

            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
               // mProgressDialog.dismiss();
            }


        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exp_dropdown_menu, menu);
        MenuItem year = menu.findItem(R.id.spinner1);
        MenuItem month = menu.findItem(R.id.spinner2);
        spinner1 = (Spinner) MenuItemCompat.getActionView(year);
        spinner2 = (Spinner) MenuItemCompat.getActionView(month);

        List<String> Year = new ArrayList<String>();
        Year.add(getString(R.string.Y2018));
        Year.add(getString(R.string.Y2019));


        List<String> Month = new ArrayList<String>();
        Month.add(getString(R.string.Jan));
        Month.add(getString(R.string.Feb));
        Month.add(getString(R.string.March));
        Month.add(getString(R.string.April));
        Month.add(getString(R.string.May));
        Month.add(getString(R.string.June));
        Month.add(getString(R.string.Jully));
        Month.add(getString(R.string.Aug));
        Month.add(getString(R.string.Sept));
        Month.add(getString(R.string.Oct));
        Month.add(getString(R.string.Nov));
        Month.add(getString(R.string.Dec));
        mMonth = getString(R.string.Jan);

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,
                R.layout.spinner_item_layout, Year);

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                R.layout.spinner_item_layout, Month);

        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(dataAdapter1); // set the adapter to provide layout of rows and content
        spinner2.setAdapter(dataAdapter2);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH); // prints 10 (October)
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        spinner1.setSelection(getIndexOfYear(currentYear));

        spinner2.setSelection(currentMonth);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(AllNewsActivity.this, adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
                mYear = adapterView.getItemAtPosition(i).toString();
                CurrentMonthData=getData();

                //CREATE AND BIND TO ADAPTER
                AllNewsAdapter adapter=new AllNewsAdapter(getApplicationContext(), CurrentMonthData);
                mExpandableListView.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }); // set the listener, to perform actions based on item selection
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               // Toast.makeText(AllNewsActivity.this, adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
                mMonth = adapterView.getItemAtPosition(i).toString();
                CurrentMonthData=getData();

                //CREATE AND BIND TO ADAPTER
                AllNewsAdapter adapter=new AllNewsAdapter(getApplicationContext(), CurrentMonthData);
                mExpandableListView.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return true;
    }
    public int getIndexOfYear(int year)
    {
        int lIndex = 0;
        if(String.valueOf(year).equals(getString(R.string.Y2018)))
        {
            lIndex = 0;
        }
        else if(String.valueOf(year).equals(getString(R.string.Y2019)))
        {
            lIndex = 1;
        }
        return lIndex;
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mExpandableListView.setIndicatorBounds(mExpandableListView.getRight()- 140, mExpandableListView.getWidth()-50);
        } else {
            mExpandableListView.setIndicatorBoundsRelative(mExpandableListView.getRight()- 140, mExpandableListView.getWidth()-50);

        }
    }
    //ADD AND GET DATA

    private ArrayList<ExpandableData> getData()
    {
        mDatabase = data.getmDatabase();
        DataModel mData = null;

        ArrayList<ExpandableData> CurrentMonth=new ArrayList<ExpandableData>();
        String lMonth = "";
        String lDay = "";

        String lYear = "";
        if(mDatabase.size() > 0)
        {

            for(int i = 0; i < mDatabase.size(); i++) {
                mData = mDatabase.get(i);
                lMonth = mMonth;
                lYear = mYear;
                if(lYear.equals(getYear(mData.getmTimeStamp()))) {
                    if (lMonth.equals(getMonth(mData.getmTimeStamp()))) {

                        if (!(lDay.equals(getDay(mData.getmTimeStamp())))) {
                            lDay = getDay(mData.getmTimeStamp());
                            ExpandableData t1 = new ExpandableData(lMonth, lDay);
                            for (int j = 0; j < mDatabase.size(); j++) {
                                mData = mDatabase.get(j);
                                if (lMonth.equals(getMonth(mData.getmTimeStamp())) && lDay.equals(getDay(mData.getmTimeStamp()))) {
                                    DataModel mDataModel = new DataModel(mData.getmHeading(), getDate(mData.getmTimeStamp()),mData.getmImageUrl(),mData.getmNewsData(),mData.getmTimeStamp());
                                    t1.mExpnSubData.add(mDataModel);
                                }
                            }
                            CurrentMonth.add(t1);
                        }
                    }
                }
            }
        }

        return CurrentMonth;
    }

    public String getMonthString(int month)
    {
        String lmonth = "";
        switch(month)
        {
            case 0:
                lmonth = getString(R.string.Jan);
                break;
            case 1:
                lmonth = getString(R.string.Feb);
                break;
            case 2:
                lmonth = getString(R.string.March);
                break;
            case 3:
                lmonth = getString(R.string.April);
                break;
            case 4:
                lmonth = getString(R.string.May);
                break;
            case 5:
                lmonth = getString(R.string.June);
                break;
            case 6:
                lmonth = getString(R.string.Jully);
                break;
            case 7:
                lmonth = getString(R.string.Aug);
                break;
            case 8:
                lmonth = getString(R.string.Sept);
                break;
            case 9:
                lmonth = getString(R.string.Oct);
                break;
            case 10:
                lmonth = getString(R.string.Nov);
                break;
            case 11:
                lmonth = getString(R.string.Dec);
                break;

        }
        return lmonth;
    }
    public  String getMonth(long timestamp) {
        try{
            String monthString = "";
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            java.util.Date currenTimeZone=new java.util.Date(timestamp);
            monthString = getMonthString(currenTimeZone.getMonth());
            return monthString;
        }catch (Exception e) {
        }
        return "";
    }
    public  String getYear(long timestamp) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return String.valueOf(cal.get(Calendar.YEAR));

    }
    public  String getDate(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            java.util.Date currenTimeZone=new java.util.Date(timestamp);
            return sdf.format(currenTimeZone);
        }catch (Exception e) {
        }
        return "";
    }
    public  String getDay(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            java.util.Date currenTimeZone=new java.util.Date(timestamp);

            return String.valueOf(currenTimeZone.getDate());
        }catch (Exception e) {
        }
        return "";
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
