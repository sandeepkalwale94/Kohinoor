package com.kohinoor.sandy.kohinnor.Activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kohinoor.sandy.kohinnor.CustomClasses.ConnectivityReceiver;
import com.kohinoor.sandy.kohinnor.CustomClasses.MyApplication;
import com.kohinoor.sandy.kohinnor.Model.QuizData;
import com.kohinoor.sandy.kohinnor.R;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    private TextView mScore;
    private TextView mQuetion;
    private Button mOption1;
    private Button mOption2;
    private Button mOption3;
    private Button mOption4;
    private Button mQuit;
    private Button mNext;
    private int mQuetionNumber;
    private int mScoreCount;

    private PopupWindow mPopupWindow;
    //private CoordinatorLayout mainLayout;
    private LinearLayout mLinearLayout;



    FirebaseDatabase database;
    DatabaseReference mDatabaseReference;

    private List<QuizData> mQuizData;
    private List<String> mQuizList;
    public List<QuizData> mAllQuizData;


    private String StudyMaterialType;
    private String subItem;
    public  String notesType;
    private int QuetionCount;
    private int QuizNumber;
    private int QuetionLimit;
    private int QuetionSerialNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        mLinearLayout = (LinearLayout) findViewById(R.id.rl);

       // mainLayout = findViewById(R.layout.activity_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(R.string.PracticeQuiz);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mScore = findViewById(R.id.score);
        mQuetion = findViewById(R.id.quetion);
        mOption1 = findViewById(R.id.option1);
        mOption2 = findViewById(R.id.option2);
        mOption3 = findViewById(R.id.option3);
        mOption4 = findViewById(R.id.option4);
        mQuit = findViewById(R.id.quit);
        mNext = findViewById(R.id.next);
        mQuetionNumber = 0;
        QuetionSerialNumber = 1;
        mScoreCount = 0;
        mScore.setText(String.valueOf(mScoreCount));

        database = FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference();

        mQuizData = new ArrayList<>();
        mQuizList = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();

        if(bundle!=null)
        {
            StudyMaterialType = bundle.getString("StudyMaterialType");
            subItem = bundle.getString("subItem");
            notesType = bundle.getString("notesType");
            QuetionCount = bundle.getInt("QuetionCount");
            QuizNumber = bundle.getInt("QuizNumber");
        }
        //Dicide limit To get the quetions from Database
        //For Second quiz Quetion from mQuizData will be taken from 6 to 10
        QuetionLimit = (QuizNumber+1) * QuetionCount;

        //To get the quetions as per quiz number
        //For Quiz 2 it will take quetions from Quetion count(5,10,20)
        QuizNumber = QuizNumber * QuetionCount;
        readQuizData();

        mOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Option1Selected();
            }
        });
        mOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Option2Selected();
            }
        });
        mOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Option3Selected();
            }
        });
        mOption4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Option4Selected();
            }
        });
        mQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuitSelected();
            }
        });
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NextSelected();
            }
        });
        mNext.setEnabled(false);
        mNext.setBackgroundColor(getResources().getColor(R.color.LightGrey));
        mNext.setTextColor(getResources().getColor(R.color.DarkGrey));
        mOption1.setEnabled(true);
        mOption2.setEnabled(true);
        mOption3.setEnabled(true);
        mOption4.setEnabled(true);


    }
    private void readQuizData() {

        //First Quetion will be calculated Quiznumber onwards
        //For second quiz fisrt Quetion numbwr will be 5
        mQuetionNumber =  QuizNumber + mQuetionNumber;

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("StudyMaterial/" + notesType + "/" + StudyMaterialType + "/" + subItem + "/");

        //retrieving upload data from firebase database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    QuizData upload = postSnapshot.getValue(QuizData.class);
                    mQuizData.add(upload);
                }
                updateQuetion(mQuetionNumber);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void Option1Selected()
    {
        if(mOption1.getText().toString().trim().equals(mQuizData.get(mQuetionNumber).getmAnswer()))
        {
            // Toast.makeText(QuizActivity.this,"Correct",Toast.LENGTH_SHORT).show();
            mOption1.setBackgroundColor(getResources().getColor(R.color.Green));
            mScoreCount++;
            mScore.setText(String.valueOf(mScoreCount));
            mQuetionNumber++;
            if(mQuetionNumber >= QuetionLimit)
            {
                //Display Score Activity
                OnQuizComplete();
                // Toast.makeText(QuizActivity.this,"Your Score is" + mScoreCount,Toast.LENGTH_SHORT).show();
            }
            else
            {
                mNext.setEnabled(true);
                mNext.setBackgroundColor(getResources().getColor(R.color.ActionBar));
                mNext.setTextColor(getResources().getColor(R.color.White));
                mOption1.setEnabled(false);
                mOption2.setEnabled(false);
                mOption3.setEnabled(false);
                mOption4.setEnabled(false);

                //updateQuetion(mQuetionNumber);
            }
        }
        else
        {
            mOption1.setBackgroundColor(getResources().getColor(R.color.Red));
            //Toast.makeText(QuizActivity.this,"Ooops! Wrong",Toast.LENGTH_SHORT).show();
            if(mOption2.getText().toString().trim().equals(mQuizData.get(mQuetionNumber).getmAnswer()))
            {
                mOption2.setBackgroundColor(getResources().getColor(R.color.Green));
            }
            else if(mOption3.getText().toString().trim().equals(mQuizData.get(mQuetionNumber).getmAnswer()))
            {
                mOption3.setBackgroundColor(getResources().getColor(R.color.Green));
            }
            else if(mOption4.getText().toString().trim().equals(mQuizData.get(mQuetionNumber).getmAnswer()))
            {
                mOption4.setBackgroundColor(getResources().getColor(R.color.Green));
            }
            mQuetionNumber++;

            if(mQuetionNumber >= QuetionLimit)
            {
                //Display Score Activity
                OnQuizComplete();
                // Toast.makeText(QuizActivity.this,"Your Score is" + mScoreCount,Toast.LENGTH_SHORT).show();
            }
            else
            {
                mNext.setEnabled(true);
                mNext.setBackgroundColor(getResources().getColor(R.color.ActionBar));
                mNext.setTextColor(getResources().getColor(R.color.White));
                mOption1.setEnabled(false);
                mOption2.setEnabled(false);
                mOption3.setEnabled(false);
                mOption4.setEnabled(false);
                //updateQuetion(mQuetionNumber);
            }

        }
    }
    public void Option2Selected()
    {
        if(mOption2.getText().toString().trim().equals(mQuizData.get(mQuetionNumber).getmAnswer()))
        {
            // Toast.makeText(QuizActivity.this,"Correct",Toast.LENGTH_SHORT).show();
            mOption2.setBackgroundColor(getResources().getColor(R.color.Green));
            mScoreCount++;
            mScore.setText(String.valueOf(mScoreCount));
            mQuetionNumber++;
            if(mQuetionNumber >= QuetionLimit)
            {
                //Display Score Activity
                OnQuizComplete();
                // Toast.makeText(QuizActivity.this,"Your Score is" + mScoreCount,Toast.LENGTH_SHORT).show();
            }
            else
            {
                mNext.setEnabled(true);
                mNext.setBackgroundColor(getResources().getColor(R.color.ActionBar));
                mNext.setTextColor(getResources().getColor(R.color.White));
                mOption1.setEnabled(false);
                mOption2.setEnabled(false);
                mOption3.setEnabled(false);
                mOption4.setEnabled(false);
                //updateQuetion(mQuetionNumber);
            }
        }
        else
        {
            mOption2.setBackgroundColor(getResources().getColor(R.color.Red));
            if(mOption1.getText().toString().trim().equals(mQuizData.get(mQuetionNumber).getmAnswer()))
            {
                mOption1.setBackgroundColor(getResources().getColor(R.color.Green));
            }
            else if(mOption3.getText().toString().trim().equals(mQuizData.get(mQuetionNumber).getmAnswer()))
            {
                mOption3.setBackgroundColor(getResources().getColor(R.color.Green));
            }
            else if(mOption4.getText().toString().trim().equals(mQuizData.get(mQuetionNumber).getmAnswer()))
            {
                mOption4.setBackgroundColor(getResources().getColor(R.color.Green));
            }
                        //Toast.makeText(QuizActivity.this,"Ooops! Wrong",Toast.LENGTH_SHORT).show();
            mQuetionNumber++;
            if(mQuetionNumber >= QuetionLimit)
            {
                //Display Score Activity
                OnQuizComplete();
                // Toast.makeText(QuizActivity.this,"Your Score is" + mScoreCount,Toast.LENGTH_SHORT).show();
            }
            else
            {

                mNext.setEnabled(true);
                mNext.setBackgroundColor(getResources().getColor(R.color.ActionBar));
                mNext.setTextColor(getResources().getColor(R.color.White));
                mOption1.setEnabled(false);
                mOption2.setEnabled(false);
                mOption3.setEnabled(false);
                mOption4.setEnabled(false);
                //updateQuetion(mQuetionNumber);
            }

        }
    }
    public void Option3Selected()
    {
        if(mOption3.getText().toString().trim().equals(mQuizData.get(mQuetionNumber).getmAnswer()))
        {
            // Toast.makeText(QuizActivity.this,"Correct",Toast.LENGTH_SHORT).show();
            mOption3.setBackgroundColor(getResources().getColor(R.color.Green));
            mScoreCount++;
            mScore.setText(String.valueOf(mScoreCount));
            mQuetionNumber++;
            if(mQuetionNumber >= QuetionLimit)
            {
                //Display Score Activity
                OnQuizComplete();
                // Toast.makeText(QuizActivity.this,"Your Score is" + mScoreCount,Toast.LENGTH_SHORT).show();
            }
            else
            {
                mNext.setEnabled(true);
                mNext.setBackgroundColor(getResources().getColor(R.color.ActionBar));
                mNext.setTextColor(getResources().getColor(R.color.White));
                mOption1.setEnabled(false);
                mOption2.setEnabled(false);
                mOption3.setEnabled(false);
                mOption4.setEnabled(false);
                //updateQuetion(mQuetionNumber);
            }
        }
        else
        {
            mOption3.setBackgroundColor(getResources().getColor(R.color.Red));
            if(mOption2.getText().toString().trim().equals(mQuizData.get(mQuetionNumber).getmAnswer()))
            {
                mOption2.setBackgroundColor(getResources().getColor(R.color.Green));
            }
            else if(mOption1.getText().toString().trim().equals(mQuizData.get(mQuetionNumber).getmAnswer()))
            {
                mOption1.setBackgroundColor(getResources().getColor(R.color.Green));
            }
            else if(mOption4.getText().toString().trim().equals(mQuizData.get(mQuetionNumber).getmAnswer()))
            {
                mOption4.setBackgroundColor(getResources().getColor(R.color.Green));
            }
            //Toast.makeText(QuizActivity.this,"Ooops! Wrong",Toast.LENGTH_SHORT).show();
            mQuetionNumber++;
            if(mQuetionNumber >= QuetionLimit)
            {
                //Display Score Activity
                OnQuizComplete();
                // Toast.makeText(QuizActivity.this,"Your Score is" + mScoreCount,Toast.LENGTH_SHORT).show();
            }
            else
            {
                mNext.setEnabled(true);
                mNext.setBackgroundColor(getResources().getColor(R.color.ActionBar));
                mNext.setTextColor(getResources().getColor(R.color.White));
                mOption1.setEnabled(false);
                mOption2.setEnabled(false);
                mOption3.setEnabled(false);
                mOption4.setEnabled(false);
                //updateQuetion(mQuetionNumber);
            }

        }
    }
    public void Option4Selected()
    {
        if(mOption4.getText().toString().trim().equals(mQuizData.get(mQuetionNumber).getmAnswer()))
        {
            // Toast.makeText(QuizActivity.this,"Correct",Toast.LENGTH_SHORT).show();
            mOption4.setBackgroundColor(getResources().getColor(R.color.Green));
            mScoreCount++;
            mScore.setText(String.valueOf(mScoreCount));
            mQuetionNumber++;
            if(mQuetionNumber >= QuetionLimit)
            {
                //Display Score Activity
                OnQuizComplete();
                // Toast.makeText(QuizActivity.this,"Your Score is" + mScoreCount,Toast.LENGTH_SHORT).show();
            }
            else
            {
                mNext.setEnabled(true);
                mNext.setBackgroundColor(getResources().getColor(R.color.ActionBar));
                mNext.setTextColor(getResources().getColor(R.color.White));
                mOption1.setEnabled(false);
                mOption2.setEnabled(false);
                mOption3.setEnabled(false);
                mOption4.setEnabled(false);
                //updateQuetion(mQuetionNumber);
            }
        }
        else
        {
            mOption4.setBackgroundColor(getResources().getColor(R.color.Red));
            if(mOption2.getText().toString().trim().equals(mQuizData.get(mQuetionNumber).getmAnswer()))
            {
                mOption2.setBackgroundColor(getResources().getColor(R.color.Green));
            }
            else if(mOption3.getText().toString().trim().equals(mQuizData.get(mQuetionNumber).getmAnswer()))
            {
                mOption3.setBackgroundColor(getResources().getColor(R.color.Green));
            }
            else if(mOption1.getText().toString().trim().equals(mQuizData.get(mQuetionNumber).getmAnswer()))
            {
                mOption1.setBackgroundColor(getResources().getColor(R.color.Green));
            }
            //Toast.makeText(QuizActivity.this,"Ooops! Wrong",Toast.LENGTH_SHORT).show();
            mQuetionNumber++;
            if(mQuetionNumber >= QuetionLimit)
            {
                //Display Score Activity
                OnQuizComplete();
                // Toast.makeText(QuizActivity.this,"Your Score is" + mScoreCount,Toast.LENGTH_SHORT).show();
            }
            else
            {
                mNext.setEnabled(true);
                mNext.setBackgroundColor(getResources().getColor(R.color.ActionBar));
                mNext.setTextColor(getResources().getColor(R.color.White));
                mOption1.setEnabled(false);
                mOption2.setEnabled(false);
                mOption3.setEnabled(false);
                mOption4.setEnabled(false);
                //updateQuetion(mQuetionNumber);
            }

        }
    }
    public void NextSelected()
    {
        mNext.setEnabled(false);
        mNext.setBackgroundColor(getResources().getColor(R.color.LightGrey));
        mNext.setTextColor(getResources().getColor(R.color.DarkGrey));
        mOption1.setEnabled(true);
        mOption2.setEnabled(true);
        mOption3.setEnabled(true);
        mOption4.setEnabled(true);
        updateQuetion(mQuetionNumber);
    }
    public void QuitSelected()
    {
        onBackPressed();
    }
    public void updateQuetion(final int quetionNumber)
    {

        mOption1.setBackgroundColor(getResources().getColor(R.color.LightGrey));
        mOption2.setBackgroundColor(getResources().getColor(R.color.LightGrey));
        mOption3.setBackgroundColor(getResources().getColor(R.color.LightGrey));
        mOption4.setBackgroundColor(getResources().getColor(R.color.LightGrey));

        mQuetion.setText(QuetionSerialNumber + ". " + mQuizData.get(quetionNumber).getmQuetion());
        mOption1.setText(mQuizData.get(quetionNumber).getmOption1());
        mOption2.setText(mQuizData.get(quetionNumber).getmOption2());
        mOption3.setText(mQuizData.get(quetionNumber).getmOption3());
        mOption4.setText(mQuizData.get(quetionNumber).getmOption4());
        QuetionSerialNumber++;

    }
    public void OnQuizComplete() {


        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_layout, null);

        //popupView.setBackgroundDrawable(getResources().getDrawable(R.color.LightGrey));
        TextView mScoreDisplay = (TextView) popupView.findViewById(R.id.popupscore);
        ImageView mRefresh = (ImageView) popupView.findViewById(R.id.refresh);
        ImageView mBack = (ImageView) popupView.findViewById(R.id.back);

        mScoreDisplay.setText(mScoreCount+ " of "+ QuetionCount);
        mPopupWindow = new PopupWindow(
                popupView,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                false
        );
        mPopupWindow.setOutsideTouchable(false);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        // Set an elevation value for popup window
        // Call requires API level 21
        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow.setElevation(5.0f);
        }

        mRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
                QuetionSerialNumber = 1;
                mQuetionNumber = QuizNumber + 0;
                mScoreCount = 0;
                mScore.setText(String.valueOf(mScoreCount));
                updateQuetion(mQuetionNumber);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mPopupWindow.dismiss();
                onBackPressed();
            }
        });
        mPopupWindow.showAtLocation(mLinearLayout, Gravity.CENTER,0,0);

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
    public class QuizDataSore
    {
        QuizDataSore()
        {

        }
        QuizDataSore(List<QuizData> quizData)
        {
            mAllQuizData = quizData;
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
}
