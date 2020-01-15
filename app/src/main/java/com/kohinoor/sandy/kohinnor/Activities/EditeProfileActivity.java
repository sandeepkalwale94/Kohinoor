package com.kohinoor.sandy.kohinnor.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kohinoor.sandy.kohinnor.Model.ProfileData;
import com.kohinoor.sandy.kohinnor.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.content.ContentValues.TAG;

public class EditeProfileActivity extends AppCompatActivity {

    private int STORAGE_PERMISSION_CODE = 23;
    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "MainActivity";


    private EditText mEditProfName;
    private EditText mEditProfMail;
    private Button mAddProfPic;
    private ImageView mEditProfPic;
    private EditText mEditAboutMeText;
    private EditText mEditDOBText;
    private EditText mEditAdress;
    private Button mSaveProfile;

    private String mImageUrl;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ProfileData profileData;
    private StorageReference mStorageRef;

    private ProgressDialog mProgressDialog;

    Uri downloadUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edite_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Hello");

        setSupportActionBar(toolbar);
      //  toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mEditProfName = (EditText) findViewById(R.id.editProfname);
        mEditProfMail = (EditText) findViewById(R.id.editProfemail);
        mAddProfPic = (Button) findViewById(R.id.editProfaddimage);
        mEditProfPic= (ImageView) findViewById(R.id.user_profile_photo);
        mEditAboutMeText= (EditText) findViewById(R.id.editProfaboutmetext);
        mEditDOBText= (EditText) findViewById(R.id.editProfDOB);
        mEditAdress= (EditText) findViewById(R.id.editProfaddress);
        mSaveProfile = (Button) findViewById(R.id.savebutton);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mProgressDialog = new ProgressDialog(this);

        loadProfileData();

        mSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveProfile();

            }
        });

        mAddProfPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadImage();
            }
        });


        toolbar.setTitle("Kohinoor");



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

    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage("Uploading Image...");
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#DC143C")));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
            // Get the url from data
            Uri file = data.getData();
            // String filepath;
            String fileNameString;


            if (null != file) {
                try {
                    fileNameString = getFileName(file);
                    StorageReference riversRef = mStorageRef.child("images/" + fileNameString);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), file);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                    byte[] Bytedata = baos.toByteArray();

                    UploadTask uploadTask2 = riversRef.putBytes(Bytedata);

                    //  riversRef.putFile(file)
                    uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            downloadUrl = taskSnapshot.getDownloadUrl();

                            Picasso.with(getApplicationContext())
                                    .load(downloadUrl.toString())
                                    .resize(250,250)
                                    .into(mEditProfPic, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            Bitmap imageBitmap = ((BitmapDrawable) mEditProfPic.getDrawable()).getBitmap();
                                            RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                                            imageDrawable.setCircular(true);
                                            imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                                            mEditProfPic.setImageDrawable(imageDrawable);
                                        }

                                        @Override
                                        public void onError() {
                                            mEditProfPic.setImageResource(R.drawable.ic_account_circle_black_24dp);
                                        }
                                    });

                            mProgressDialog.dismiss();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    Toast.makeText(getApplicationContext(), "Image upload failder", Toast.LENGTH_LONG).show();
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

    public void LoadImage()
    {
        if(isReadStorageAllowed()){


            //If permission is already having then showing the toast
            // Toast.makeText(AdminActivity.this,"You already have the permission",Toast.LENGTH_LONG).show();
            openImageChooser();
            //Existing the method with return
            return;
        }

        requestStoragePermission();

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
    public void SaveProfile()
    {
        String editProfName = mEditProfName.getText().toString().trim();
        String editProfEmail = mEditProfMail.getText().toString().trim();

        String editImageUrl = downloadUrl.toString();;

        String editProfAboutText = mEditAboutMeText.getText().toString().trim();
        String editProfDOB = mEditDOBText.getText().toString().trim();
        String editProfAddress = mEditAdress.getText().toString().trim();

        ProfileData profileData = new ProfileData(editProfName,editProfEmail,editImageUrl,editProfAboutText,editProfDOB,editProfAddress);
        myRef = database.getReference("Users");
        FirebaseUser user = mAuth.getCurrentUser();
        String usersId = user.getUid();

        myRef.push().child(usersId);

        myRef.child(usersId).setValue(profileData);

        Toast.makeText(this,"Data Saved Successfully",Toast.LENGTH_LONG).show();

    }
    public void loadProfileData()
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

                if(profileData != null)
                {

                    mEditProfName.setText(profileData.getName());
                    mEditProfMail.setText(profileData.getEmail());
                    mEditAboutMeText.setText(profileData.getAboutMeText());
                    mEditDOBText.setText(profileData.getDOB());
                    mEditAdress.setText(profileData.getAddress());
                    Picasso.with(getApplicationContext())
                            .load(profileData.getPhotoUrl())
                            .resize(250,250)
                            .into(mEditProfPic, new Callback() {
                                @Override
                                public void onSuccess() {
                                    Bitmap imageBitmap = ((BitmapDrawable) mEditProfPic.getDrawable()).getBitmap();
                                    RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                                    imageDrawable.setCircular(true);
                                    imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                                    mEditProfPic.setImageDrawable(imageDrawable);
                                }

                                @Override
                                public void onError() {
                                    mEditProfPic.setImageResource(R.drawable.ic_account_circle_black_24dp);
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
}
