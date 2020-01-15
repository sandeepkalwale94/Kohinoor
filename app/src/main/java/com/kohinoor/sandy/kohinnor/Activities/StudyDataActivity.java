package com.kohinoor.sandy.kohinnor.Activities;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kohinoor.sandy.kohinnor.Adapter.StudyMFragAdapter;
import com.kohinoor.sandy.kohinnor.CustomClasses.ConnectivityReceiver;
import com.kohinoor.sandy.kohinnor.CustomClasses.MyApplication;
import com.kohinoor.sandy.kohinnor.Interface.ClickListener;
import com.kohinoor.sandy.kohinnor.Model.StudyMData;
import com.kohinoor.sandy.kohinnor.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.security.AccessController.getContext;

public class StudyDataActivity extends AppCompatActivity implements ClickListener, ConnectivityReceiver.ConnectivityReceiverListener {

    private int STORAGE_PERMISSION_CODE = 23;
    Context context;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    //RecyclerView.Adapter recyclerViewAdapter;
    private StudyMFragAdapter mAdapter;
    RecyclerView.LayoutManager recylerViewLayoutManager;
    FirebaseDatabase database;
    DatabaseReference mDatabaseReference;
    private ProgressDialog mProgressDialog;

    ClickListener clickListener;
    private List<StudyMData> MaterialType;

    private String StudyMaterialType;
    private String subItem;
    public static String notesType;

    private String pdffileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_data);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        // toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference();
        mProgressDialog = new ProgressDialog(this);
       // readPdfList();

        MaterialType = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();

        if(bundle!=null)
        {
            StudyMaterialType = bundle.getString("StudyMaterialType");
            subItem = bundle.getString("subItem");
            notesType = bundle.getString("notesType");
        }
        readPdfList();
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
        readPdfList();
    }

    public void readPdfList()
    {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/"+notesType+"/"+StudyMaterialType+"/"+subItem+"/");

        final Long timestamp = new Date().getTime();
        //retrieving upload data from firebase database
        mDatabaseReference.orderByChild("mTimeStamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MaterialType = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    StudyMData upload = postSnapshot.getValue(StudyMData.class);
                    MaterialType.add(upload);
                }


                context = getApplicationContext();

                relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout1);

                recyclerView = (RecyclerView) findViewById(R.id.recyclerview1);

                recylerViewLayoutManager = new LinearLayoutManager(context);

                recyclerView.setLayoutManager(recylerViewLayoutManager);
                if(MaterialType.size() == 0)
                {
                    MaterialType.add(new StudyMData("No Document", "",timestamp));
                }
                mAdapter = new StudyMFragAdapter(context, MaterialType);
                mAdapter.setClickListener(StudyDataActivity.this);
                recyclerView.addItemDecoration(new DividerItemDecoration(StudyDataActivity.this,
                        DividerItemDecoration.VERTICAL));


                recyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void ItemClicked(View view, int position) {
        //Toast.makeText(this, "#" + position + " - " + MaterialType.get(position).getmTitle(), Toast.LENGTH_SHORT).show();
        if (isReadStorageAllowed()) {
            pdffileName = MaterialType.get(position).getmTitle();
           openFile(position);
        }

        else
        {
            requestStoragePermission();
        }

    }
    public void openFile(final int position)
    {
         File pdfFile = new File(Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), pdffileName);

            try {
                if (pdfFile.exists()) {

                    Uri path = Uri.fromFile(pdfFile);

                    Intent objIntent = new Intent(Intent.ACTION_VIEW);

                    objIntent.setDataAndType(path, "application/pdf");

                    objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(objIntent);

                } else {

                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mProgressDialog.setMessage("Loading file...");
                    mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
                    mProgressDialog.setIndeterminate(false);
                    mProgressDialog.show();
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(MaterialType.get(position).getmUrl()));
                    // request.setDescription("Downloading file");
                    // request.setTitle("Yes it is downloading");

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    }

                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, MaterialType.get(position).getmTitle());

                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    manager.enqueue(request);

                    BroadcastReceiver receiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            String action = intent.getAction();
                            if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                                // Toast.makeText(StudyDataActivity.this, "File Downloaded", Toast.LENGTH_SHORT).show();

                                openFile(position);
                                mProgressDialog.dismiss();;
                            }
                        }
                    };
                    registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                 //   Toast.makeText(context, "File NotFound",

                  //          Toast.LENGTH_SHORT).show();

                }

            } catch (ActivityNotFoundException e) {

                Toast.makeText(context,

                        "No Viewer Application Found", Toast.LENGTH_SHORT)

                        .show();

            } catch (Exception e) {

                e.printStackTrace();

            }

    }
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == STORAGE_PERMISSION_CODE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){


                //Displaying a toast
                Toast.makeText(context,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(context,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
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
