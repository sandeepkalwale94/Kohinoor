package com.kohinoor.sandy.kohinnor.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kobakei.ratethisapp.RateThisApp;
import com.kohinoor.sandy.kohinnor.Adapter.MyHomeRecyclerViewAdapter;
import com.kohinoor.sandy.kohinnor.CustomClasses.ConnectivityReceiver;
import com.kohinoor.sandy.kohinnor.CustomClasses.Constants;
import com.kohinoor.sandy.kohinnor.CustomClasses.MyApplication;
import com.kohinoor.sandy.kohinnor.CustomClasses.MyNotificationManager;
import com.kohinoor.sandy.kohinnor.Fragments.AboutusFragment;
import com.kohinoor.sandy.kohinnor.Fragments.ContactFragment;
import com.kohinoor.sandy.kohinnor.Fragments.CourseListFragment;
import com.kohinoor.sandy.kohinnor.Fragments.HomeFragment;
import com.kohinoor.sandy.kohinnor.Fragments.ProfileFragment;
import com.kohinoor.sandy.kohinnor.Fragments.StudyMFragment;
import com.kohinoor.sandy.kohinnor.Model.CourseList;
import com.kohinoor.sandy.kohinnor.Model.DataModel;

import com.kohinoor.sandy.kohinnor.Model.Database;
import com.kohinoor.sandy.kohinnor.Model.ProfileData;
import com.kohinoor.sandy.kohinnor.Model.StudyDataModel;
import com.kohinoor.sandy.kohinnor.R;
import com.kohinoor.sandy.kohinnor.Services.FireBaseDataChangeNotificationService;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnListFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener,
StudyMFragment.OnListFragmentInteractionListener, CourseListFragment.OnListFragmentInteractionListener, ConnectivityReceiver.ConnectivityReceiverListener{

    private static final String TAG = "HomeActivity";
    private TextView mProfName;
    private TextView mProfEmail;
    private ImageView mProfPic;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ProfileData profileData;

    private String emailString;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    private boolean mToolBarNavigationListenerIsRegistered = false;
    private ShareActionProvider mShareActionProvider;;
    public List<DataModel> mDatabase;
    FloatingActionButton fab;

    private boolean isConnected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.CurrentAffairs);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartAdminActivity();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Internet Connection Check
       // checkConnection();

        // Monitor launch times and interval from installation
        RateThisApp.onCreate(this);

        //Send Push Notification on Database update

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
      //  myRef = database.getReference("NewsData");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View hView = navigationView.getHeaderView(0);

        mProfName = (TextView)hView.findViewById(R.id.prof_name);
        mProfEmail = (TextView)hView.findViewById(R.id.prof_email);
        mProfPic = (ImageView)hView.findViewById(R.id.prof_pic);

        navigationView.setNavigationItemSelectedListener(this);

        displaySelectedScreen(R.id.nav_home) ;

//Push Notification

        //sendNotification();
        startService(new Intent(this, FireBaseDataChangeNotificationService.class));

    }


    private void enableViews(boolean enable) {

        // To keep states of ActionBar and ActionBarDrawerToggle synchronized,
        // when you enable on one, you disable on the other.
        // And as you may notice, the order for this operation is disable first, then enable - VERY VERY IMPORTANT.
        if(enable) {
            // Remove hamburger
            toggle.setDrawerIndicatorEnabled(false);

            // Show back button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
            // clicks are disabled i.e. the UP button will not work.
            // We need to add a listener, as in below, so DrawerToggle will forward
            // click events to this listener.
            if(!mToolBarNavigationListenerIsRegistered) {
                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Doesn't have to be onBackPressed
                        onBackPressed();
                    }
                });

                mToolBarNavigationListenerIsRegistered = true;
            }

        } else {
            toolbar.setTitle(R.string.CurrentAffairs);
            // Remove back button
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            // Show hamburger
            toggle.setDrawerIndicatorEnabled(true);

            // Remove the/any drawer toggle listener
            toggle.setToolbarNavigationClickListener(null);
            mToolBarNavigationListenerIsRegistered = false;
        }



    }

    public void loadNavigationHeaderData()
    {
        FirebaseUser user = mAuth.getCurrentUser();
        final String usersId = user.getUid();
        myRef = database.getReference("Users/"+usersId);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                profileData = dataSnapshot.getValue(ProfileData.class);

                if (profileData != null) {
                    mProfName.setText(profileData.getName());
                    mProfEmail.setText(profileData.getEmail());

                    Picasso.with(getApplicationContext())
                            .load(profileData.getPhotoUrl())
                            .resize(100, 100)
                            .into(mProfPic, new Callback() {
                                @Override
                                public void onSuccess() {
                                    Bitmap imageBitmap = ((BitmapDrawable) mProfPic.getDrawable()).getBitmap();
                                    RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                                    imageDrawable.setCircular(true);
                                    imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                                    mProfPic.setImageDrawable(imageDrawable);
                                }

                                @Override
                                public void onError() {
                                    mProfPic.setImageResource(R.drawable.ic_account_circle_black_24dp);
                                }
                            });
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());

            }


        });

    }
    public void StartAdminActivity()
    {
        //Intent intent = new Intent(this,GoogleAdds.class);
        Intent intent = new Intent(this,AdminMainActivity.class);
        startActivity(intent);
    }
    //Interface of Home fragment
    @Override
    public void onListFragmentInteraction(DataModel item){
        //you can leave it empty
        Intent intent = new Intent(this,NewsArticalActivity.class);
        intent.putExtra("Heading", item.getmHeading());
        intent.putExtra("Date", item.getmDate());
        intent.putExtra("NewsData", item.getmNewsData());
        intent.putExtra("ImageUrl", item.getmImageUrl());
        startActivity(intent);
    }

    //Interface of Course fragment
    @Override
    public void onListFragmentInteraction(CourseList item){
        //you can leave it empty

      //  Toast.makeText(this,"Hello",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,CourseDetailsActivity.class);
      //  Intent intent = new Intent(this,TODODeleteActivity.class);

        startActivity(intent);
    }

    //Interface of ProfileFragmengt
    @Override
    public void onFragmentInteraction(Uri item){
        //you can leave it empty
        /*Intent intent = new Intent(this,StudyDataActivity.class);
        intent.putExtra("Heading", item.getmHeading());intent.putExtra("Date", item.getmDate());
        intent.putExtra("NewsData", item.getmNewsData());
        intent.putExtra("ImageUrl", item.getmImageUrl());
        startActivity(intent);*/
    }

    //Interface of StudyMaterial Fragment
    @Override
    public void onListFragmentInteraction(StudyDataModel item, String l_strFlowFrom){
        String MaterialType;
        String subItem = item.text;

        // Intent intent = new Intent(getContext(),StudyDataSelectorActivity.class);
        if((item.text.equals(getString(R.string.PSI_H))) ||
                (item.text.equals(getString(R.string.Police_H))) ||
                (item.text.equals(getString(R.string.STI_H))) ||
                (item.text.equals(getString(R.string.Agri_H))) ||
                (item.text.equals(getString(R.string.MPSC_H))))
        {
            MaterialType = getString(R.string.Course_wise_material);

        }
        else
        {
            MaterialType = getString(R.string.Subject_wise_material);
        }

        if(l_strFlowFrom.equals(getString(R.string.ImportantArticles)))
        {
            displayArticleListActivity(getString(R.string.ImportantArticles),MaterialType,subItem);
        }
        else
        {
            Intent intent = new Intent(this,StudyDataSelectorActivity.class);
            intent.putExtra("MaterialType", MaterialType);
            intent.putExtra("SubItem", subItem);
            intent.putExtra("FlowFrom",l_strFlowFrom);
            startActivity(intent);
        }


    }

    public void displayArticleListActivity(String notesType, String materialType,String subItem)
    {
        Intent intent = new Intent(this,ArticleListActivity.class);
        intent.putExtra("StudyMaterialType", materialType);
        intent.putExtra("subItem", subItem);
        intent.putExtra("notesType", notesType);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(getFragmentManager().getBackStackEntryCount()>0) {
            getFragmentManager().popBackStack();
            enableViews(false);
            fab.setVisibility(View.VISIBLE);
        }
        else{
                super.onBackPressed();
            enableViews(false);
            fab.setVisibility(View.VISIBLE);
            }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        //MenuItem item = menu.findItem(R.id.nav_share);

        //mShareActionProvider = (ShareActionProvider) item.getActionProvider();

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // If the condition is satisfied, "Rate this app" dialog will be shown
            RateThisApp.showRateDialog(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        if(!(item.getItemId() == R.id.nav_home))
        {
            enableViews(true);
        }

        displaySelectedScreen(item.getItemId()) ;



        return true;
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;
        Bundle data = new Bundle();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                toolbar.setTitle(R.string.CurrentAffairs);
                break;
           /* case R.id.nav_profile:
                toolbar.setTitle(R.string.Profile);
                fragment = new ProfileFragment();

                data.putString("email", emailString);
                fragment.setArguments(data);
                ft.addToBackStack(null);
                break;*/
            /*case R.id.nav_downloads:
                toolbar.setTitle(R.string.Downloads);
                fragment = new StudyMFragment();
                data.putString(getString(R.string.FromWhere), getString(R.string.Downloads));
                fragment.setArguments(data);
                ft.addToBackStack(null);
                break;*/

            case R.id.nav_imp_articles:
                toolbar.setTitle(R.string.ImportantArticles);
                fragment = new StudyMFragment();
                data.putString(getString(R.string.FromWhere), getString(R.string.ImportantArticles));
                fragment.setArguments(data);
                ft.addToBackStack(null);
                break;
            case R.id.nav_test:
                toolbar.setTitle(R.string.PracticeQuiz);
                fragment = new StudyMFragment();
                data.putString(getString(R.string.FromWhere), getString(R.string.PracticeQuiz));
                fragment.setArguments(data);
                ft.addToBackStack(null);
                break;


            case R.id.nav_study:
                toolbar.setTitle(R.string.StudyMaterial);
                fragment = new StudyMFragment();
                data.putString("FromWhere", "StudyMaterial");
                fragment.setArguments(data);
                ft.addToBackStack(null);
                break;

            case R.id.nav_course:
                toolbar.setTitle(R.string.Courses);
                fragment = new CourseListFragment();
                ft.addToBackStack(null);
                break;

            case R.id.nav_share:
               /* setShareIntent(createShareIntent());*/

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                        "Kohinoor Academy");
                startActivity(Intent.createChooser(shareIntent, "Share via"));
                ft.addToBackStack(null);
                fab.setVisibility(View.GONE);
                break;
            case R.id.nav_contact:
                toolbar.setTitle(R.string.Contact);
                fragment = new ContactFragment();
                ft.addToBackStack(null);
                fab.setVisibility(View.GONE);
                break;
            case R.id.nav_info:
                toolbar.setTitle(R.string.AboutUs);
                fragment = new AboutusFragment();
                ft.addToBackStack(null);
                fab.setVisibility(View.GONE);
                break;
/*
            case R.id.nav_test:
                fragment = new StudyMFragment();
                data.putString("FromWhere", "Tests");
                fragment.setArguments(data);


                break;*/
            case R.id.nav_logout:
                finish();
                break;

            }


        //replacing the fragment
        if (fragment != null) {


            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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
