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
import com.kohinoor.sandy.kohinnor.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    private AutoCompleteTextView email;
    private EditText pass;
    private Button signin;
    private TextView signup;
    private String emailString;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (AutoCompleteTextView) findViewById(R.id.signin_email);
        loadAutoComplete();
        pass = (EditText) findViewById(R.id.signin_password);
        signin = (Button) findViewById(R.id.email_sign_in_button);
        signup = (TextView) findViewById(R.id.signUpTextView);

        mAuth = FirebaseAuth.getInstance();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // StartHomeActivity();
                initLogin();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartSignupActivity();
            }
        });



    }

    private void loadAutoComplete() {
       // getLoaderManager().initLoader(0, null, this);
    }
    public void initLogin()
    {

        emailString = email.getText().toString().trim();
        String passwordString = pass.getText().toString().trim();
        mAuth.signInWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                           StartHomeActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
    public void StartSignupActivity()
    {
        finish();
        Intent myIntent = new Intent(this, SignupActivity.class);

        startActivity(myIntent);
    }
    public void StartHomeActivity()
    {
        finish();
        emailString = email.getText().toString().trim();
        Intent myIntent = new Intent(this, HomeActivity.class);
        myIntent.putExtra("email", emailString);
        startActivity(myIntent);
    }
}

