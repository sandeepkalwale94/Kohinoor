package com.kohinoor.sandy.kohinnor.Activities;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.kohinoor.sandy.kohinnor.CustomClasses.ConnectivityReceiver;
import com.kohinoor.sandy.kohinnor.CustomClasses.Constants;
import com.kohinoor.sandy.kohinnor.CustomClasses.MyApplication;
import com.kohinoor.sandy.kohinnor.CustomClasses.MyNotificationManager;
import com.kohinoor.sandy.kohinnor.Model.DataModel;
import com.kohinoor.sandy.kohinnor.Model.PushNotification;
import com.kohinoor.sandy.kohinnor.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AdminActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private int STORAGE_PERMISSION_CODE = 23;
    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "MainActivity";

    private EditText mAdminHeading;
    private EditText mAdminNewsData;
    private TextView mImageName;
    private Button mImageSelect;
    private Button mUpload;
    private StorageReference mStorageRef;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseRef;

    private ProgressDialog mProgressDialog;

    private boolean isConnected;

    Uri downloadUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(R.string.UploadCurrentAffairs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAdminHeading = (EditText) findViewById(R.id.admin_heading);
        mAdminNewsData = (EditText) findViewById(R.id.admin_newsdata);
        mImageName = (TextView) findViewById(R.id.image_name);
        mImageSelect = (Button) findViewById(R.id.imageselect);
        mUpload = (Button) findViewById(R.id.upload);
        mProgressDialog = new ProgressDialog(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mFirebaseDatabase.getReference();

        mImageSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isReadStorageAllowed()){


                    //If permission is already having then showing the toast
                   // Toast.makeText(AdminActivity.this,"You already have the permission",Toast.LENGTH_LONG).show();
                    openImageChooser();
                    //Existing the method with return
                    return;
                }

                requestStoragePermission();

            }
        });


        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //upload
                upload();

            }
        });
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

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
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

                openImageChooser();
                //Displaying a toast
                Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void upload()
    {
        String Heading = mAdminHeading.getText().toString().trim();
        String NewsData = mAdminNewsData.getText().toString().trim();
        String ImageUrl = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String Date = sdf.format(new Date());
        Long timestamp = new Date().getTime();
       // String mDatabaseChild = UUID.randomUUID().toString();
        if(downloadUrl != null)
        {
            ImageUrl = downloadUrl.toString();
        }


        if((Heading.equals("") || NewsData.equals("")) || ImageUrl.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please fill all data",Toast.LENGTH_SHORT).show();
        }
        else {
            DataModel dataModel = new DataModel(Heading,Date,ImageUrl,NewsData,timestamp);


            if(ConnectivityReceiver.isConnected()) {
                mDatabaseRef = mFirebaseDatabase.getReference("NewsData");

                String mDatabaseChild = mDatabaseRef.push().getKey();

                mDatabaseRef.child(mDatabaseChild).setValue(dataModel);

                Toast.makeText(this, "Data uploaded successfully", Toast.LENGTH_LONG).show();


                //To send Push Notification
                mDatabaseRef = FirebaseDatabase.getInstance().getReference("PushNotification");
                PushNotification pushNotification = new PushNotification("चालू घडामोडी",dataModel.getmHeading(),"","","",mDatabaseChild);
                mDatabaseRef.child(mDatabaseRef.push().getKey()).setValue(pushNotification);


            }
        }



    }
    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);


    }
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setMessage("Uploading Image...");
                mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
            mProgressDialog.setIndeterminate(false);
                mProgressDialog.show();
                // Get the url from data
                Uri file = data.getData();
               // String filepath;
			   //String
                String fileNameString;



                if (null != file) {
                    try {
                        fileNameString =  getFileName(file);
                        StorageReference riversRef = mStorageRef.child("images/" + fileNameString);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), file);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                        byte[] Bytedata = baos.toByteArray();
                        mImageName.setText(fileNameString);
                        UploadTask uploadTask2 = riversRef.putBytes(Bytedata);

                      //  riversRef.putFile(file)
                        uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // Get a URL to the uploaded content
                                        downloadUrl = taskSnapshot.getDownloadUrl();
                                        mProgressDialog.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle unsuccessful uploads
                                        Toast.makeText(getApplicationContext(),"Image upload failder",Toast.LENGTH_LONG).show();
                                        mProgressDialog.dismiss();
                                        // ...
                                    }
                                });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Get the path from the Uri
                   // filepath = getPathFromURI(file);
                   // File fileName = new File(filepath);



                    // Set the image in ImageView
                   // imgView.setImageURI(selectedImageUri);

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
