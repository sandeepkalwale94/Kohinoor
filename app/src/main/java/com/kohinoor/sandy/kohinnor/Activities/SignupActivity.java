package com.kohinoor.sandy.kohinnor.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kohinoor.sandy.kohinnor.Model.DataModel;
import com.kohinoor.sandy.kohinnor.Model.ProfileData;
import com.kohinoor.sandy.kohinnor.R;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    private AutoCompleteTextView email;
    private EditText pass;
    private EditText confpass;
    private Button signup;
    private TextView signin;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

        email = (AutoCompleteTextView) findViewById(R.id.signup_email);
        loadAutoComplete();
        pass = (EditText) findViewById(R.id.signup_password);
        confpass = (EditText) findViewById(R.id.signup_confpassword);
        signup = (Button) findViewById(R.id.email_sign_up_button);
        signin = (TextView) findViewById(R.id.signInTextView);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartLoginActivity();

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSignUp();
            }
        });


    }

    public void StartLoginActivity()
    {
        finish();
        Intent myIntent = new Intent(this, LoginActivity.class);

        startActivity(myIntent);
    }
    public void uploadUserInDB()
    {
        String name = "Champion";
        String Email = email.getText().toString().trim();
        String photoUrl = "www.mypic.com";
        String aboutmeText = "I am a Champion";
        String dob = "31-Feb-1980";
        String address = "On Earth";

        ProfileData profileData = new ProfileData(name,Email,photoUrl,aboutmeText,dob,address);

        myRef = database.getReference("Users");
        FirebaseUser user = mAuth.getCurrentUser();
        String usersId = user.getUid();

        myRef.push().child(usersId);

        myRef.child(usersId).setValue(profileData);
    }
    private void loadAutoComplete() {
        // getLoaderManager().initLoader(0, null, this);
    }
    public void initSignUp()
    {

        String emailString = email.getText().toString().trim();
        String passString = pass.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(emailString, passString)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(),"Registration successfull! Please enter credential to Log in",Toast.LENGTH_LONG).show();
                            uploadUserInDB();
                            StartLoginActivity();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
}


