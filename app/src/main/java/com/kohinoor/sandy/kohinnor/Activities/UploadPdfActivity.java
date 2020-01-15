package com.kohinoor.sandy.kohinnor.Activities;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kohinoor.sandy.kohinnor.CustomClasses.ConnectivityReceiver;
import com.kohinoor.sandy.kohinnor.CustomClasses.Constants;
import com.kohinoor.sandy.kohinnor.CustomClasses.MyApplication;
import com.kohinoor.sandy.kohinnor.CustomClasses.MyNotificationManager;
import com.kohinoor.sandy.kohinnor.Model.ArticleData;
import com.kohinoor.sandy.kohinnor.Model.PushNotification;
import com.kohinoor.sandy.kohinnor.Model.StudyMData;
import com.kohinoor.sandy.kohinnor.R;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UploadPdfActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    final static int PICK_PDF_CODE = 2342;
    private Spinner spinner1, spinner2, spinner3;
    private Button btnSubmit, btnSelectFile;
    private TextView filename;

    private LinearLayout mArticleLayout;
    private LinearLayout mPdfLayout;

    private String materialType;
    private String subMaterial;

    private EditText mArticleTitle;
    private EditText mArticleBody;
    private String notesType;

    private String fileNameString;

    private Uri filedata;
    StorageReference mStorageReference;
    DatabaseReference mDatabaseReference;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
       // toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.UploadDatabase);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mProgressDialog = new ProgressDialog(this);

        mArticleLayout = (LinearLayout) findViewById(R.id.article_layout);
        mArticleLayout.setVisibility(LinearLayout.GONE);
        mPdfLayout = (LinearLayout) findViewById(R.id.pdf_layout);

        mArticleTitle = (EditText) findViewById(R.id.article_title);
        mArticleBody = (EditText) findViewById(R.id.artical_body);

        filename = (TextView) findViewById(R.id.filename);
        List<String> list = new ArrayList<String>();
        list.add(getString(R.string.PSI_H));
        list.add(getString(R.string.Police_H));
        list.add(getString(R.string.STI_H));
        list.add(getString(R.string.Assistant_H));
        list.add(getString(R.string.Agri_H));
        list.add(getString(R.string.MPSC_H));
        addItemsOnSpinner2(list);
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();

        materialType = getString(R.string.Course_wise_material);
        subMaterial = getString(R.string.PSI_H);
        notesType = getString(R.string.PDFNotes);

        //Push Notification
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance);
            mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    public void addItemsOnSpinner2(List<String> list) {

        spinner2 = (Spinner) findViewById(R.id.spinner2);
       // List<String> list = new ArrayList<String>();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
    }

    public void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
      //  spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                if(selectedItem.equals(getString(R.string.PDFNotes)))
                {
                    notesType = getString(R.string.PDFNotes);
                    mArticleLayout.setVisibility(LinearLayout.GONE);
                    mPdfLayout.setVisibility(View.VISIBLE);
                }
                else if(selectedItem.equals(getString(R.string.QuetionSet)))
                {
                    notesType = getString(R.string.QuetionSet);
                    mArticleLayout.setVisibility(LinearLayout.GONE);
                    mPdfLayout.setVisibility(View.VISIBLE);
                }
                else if(selectedItem.equals(getString(R.string.ImportantArticles)))
                {
                    notesType = getString(R.string.ImportantArticles);

                    mPdfLayout.setVisibility(LinearLayout.GONE);
                    mArticleLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                subMaterial = selectedItem;


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                materialType = selectedItem;
                if(selectedItem.equals(getString(R.string.Course_wise_material)))
                {

                    subMaterial = getString(R.string.PSI_H);
                    spinner2 = (Spinner) findViewById(R.id.spinner2);
                    List<String> list = new ArrayList<String>();
                    list.add(getString(R.string.PSI_H));
                    list.add(getString(R.string.Police_H));
                    list.add(getString(R.string.STI_H));
                    list.add(getString(R.string.Assistant_H));
                    list.add(getString(R.string.Agri_H));
                    list.add(getString(R.string.MPSC_H));

                    addItemsOnSpinner2(list);

                }
                else if(selectedItem.equals(getString(R.string.Subject_wise_material)))
                {
                    subMaterial = getString(R.string.Marathi);
                    spinner2 = (Spinner) findViewById(R.id.spinner2);
                    List<String> list = new ArrayList<String>();
                    list.add(getString(R.string.Marathi));
                    list.add(getString(R.string.English));
                    list.add(getString(R.string.History));
                    list.add(getString(R.string.Geography));
                    list.add(getString(R.string.Politics));
                    list.add(getString(R.string.Science));
                    list.add(getString(R.string.Math));
                    list.add(getString(R.string.Apti));
                    list.add(getString(R.string.Computer));
                    addItemsOnSpinner2(list);

                }
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    public void addListenerOnButton() {

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSelectFile = (Button) findViewById(R.id.selectfile);


        btnSelectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getPDF();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


               if(notesType.equals(getString(R.string.ImportantArticles)))
               {
                   uploadArticle();
               }
               else {
                   uploadFile(filedata);
               }
            }

        });
    }

    private void getPDF() {
        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return;
        }

        //creating an intent for file chooser
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
               // uploadFile(data.getData());
                filedata = data.getData();
                filename.setText(getFileName(filedata));
            }else{
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
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

    private void uploadArticle()
    {
        final String mTitle = mArticleTitle.getText().toString().trim();
        final String mBody = mArticleBody.getText().toString().trim();
        Long timeStamp = new Date().getTime();

        if(mTitle.equals("") || mBody.equals(""))
        {
            Toast.makeText(this, "Please fill all Data", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mDatabaseReference = FirebaseDatabase.getInstance().getReference("StudyMaterial/"+notesType+"/"+materialType+"/"+subMaterial);
            String key = mDatabaseReference.push().getKey();
            ArticleData upload = new ArticleData(mTitle, mBody,timeStamp);
            mDatabaseReference.child(key).setValue(upload);

            Toast.makeText(this,"Data uploaded successfully",Toast.LENGTH_LONG).show();

            //To send Push Notification
            mDatabaseReference = FirebaseDatabase.getInstance().getReference("PushNotification");
            PushNotification pushNotification = new PushNotification("महत्वाचे लेख",mTitle,notesType,materialType,subMaterial,key);
            mDatabaseReference.child(mDatabaseReference.push().getKey()).setValue(pushNotification);
        }


       // MyNotificationManager.getInstance(this).displayNotification("महत्वाचे लेख", mTitle);


    }
    private void uploadFile(Uri data) {


        final Long timeStamp = new Date().getTime();
        final String key = mDatabaseReference.push().getKey();
        StorageReference sRef = mStorageReference.child("pdfs/"+fileNameString);

        if(data != null) {
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage("Uploading Database...");
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
            fileNameString = getFileName(data);
            mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/"+notesType+"/"+materialType+"/"+subMaterial);
            sRef.putFile(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mProgressDialog.dismiss();
                            StudyMData upload = new StudyMData(fileNameString, taskSnapshot.getDownloadUrl().toString(),timeStamp);
                            mDatabaseReference.child(key).setValue(upload);

                            //To send Push Notification
                            mDatabaseReference = FirebaseDatabase.getInstance().getReference("PushNotification");
                            PushNotification pushNotification = new PushNotification("नवीन PDF नोट्स",fileNameString,notesType,materialType,subMaterial,key);
                            mDatabaseReference.child(mDatabaseReference.push().getKey()).setValue(pushNotification);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            // textViewStatus.setText((int) progress + "% Uploading...");
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "Please select the file", Toast.LENGTH_SHORT).show();
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
