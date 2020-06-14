package com.example.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

public class registration extends AppCompatActivity {
    private EditText signup_email,signup_pass,signup_confirm_pass;

    private FirebaseAuth mAuth;
    Button callSignIn;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        signup_email=findViewById(R.id.signup_email_id);
        signup_pass=findViewById(R.id.signup_pass_id);
        signup_confirm_pass=findViewById(R.id.signup_confirmPass_id);
        progressBar= (ProgressBar) findViewById(R.id.progress_bar);

        //initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        callSignIn=findViewById(R.id.login_screen);

        callSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(registration.this,login.class));
                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    public void signUp(View view) {
        String email=signup_email.getText().toString().trim();
        String pass= signup_pass.getText().toString().trim();
        String confirm_pass= signup_confirm_pass.getText().toString().trim();


        if (TextUtils.isEmpty(email)){
            signup_email.setError("Email Required");
            signup_email.requestFocus();
            return;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signup_email.setError("Email not valid");
            signup_email.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(pass)){
            signup_pass.setError("Password Required");
            signup_pass.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(confirm_pass)){
            signup_confirm_pass.setError("Please Confirm Password");
            signup_confirm_pass.requestFocus();
            return;
        }
        else if (pass.length()<6){
            signup_pass.setError("Password cannot be less than 6 characters");
            signup_pass.requestFocus();
            return;
        }
        else if (pass.equals(confirm_pass)){
            createAccount(email,pass);

        }
        else {
            signup_confirm_pass.setError("Passwords do not match!");
            signup_confirm_pass.requestFocus();
            return;
        }
    }

    private void createAccount(final String email, String pass) {
        //Firebase account sign up code
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            finish();
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(registration.this, "Sign Up Successful!", Toast.LENGTH_SHORT).show();
                            //FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(registration.this,UpdateProfile.class));
                        }
                        else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                progressBar.setVisibility(View.GONE);
                                signup_email.setError("Email already exist");
                                signup_email.requestFocus();
                                return;
                            }
                            else{
                                // If sign in fails, display a message to the user.
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(registration.this, "Internet Connetion Problem !", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
    }
}

