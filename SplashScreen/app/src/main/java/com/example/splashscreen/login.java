package com.example.splashscreen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    private EditText login_email, login_pass;
    FirebaseAuth mAuth;
    Button callSignUp,callMusic,forgot_password;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login2);

        //Showing one time notice
        SharedPreferences preferences = getSharedPreferences("preferences",MODE_PRIVATE);
        boolean firstStart = preferences.getBoolean("firstStart",true);

        if (firstStart){
            showStartDialog();
        }


        progressBar= (ProgressBar) findViewById(R.id.progress_bar);
        mAuth = FirebaseAuth.getInstance();

        login_email=findViewById(R.id.login_email_id);
        login_pass=findViewById(R.id.login_pass_id);
        forgot_password=findViewById(R.id.forgot_password);


        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetEmail=new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter your registered Eamil.");
                passwordResetDialog.setView(resetEmail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extract email and sent reset link here
                        String email = resetEmail.getText().toString();
                        mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(login.this, "Password Reset Link sent to Your Email", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(login.this, "Error! Link not Sent." +e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close the dialog
                    }
                });
                passwordResetDialog.create().show();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        callSignUp=findViewById(R.id.signup_screen);
        callMusic=findViewById(R.id.direct_to_music);

        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this,registration.class));
                finish();
            }
        });

        callMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this,LocalMusicMain.class));
            }
        });

    }

    public void login(View view) {

        String email =login_email.getText().toString().trim();
        String pass =login_pass.getText().toString().trim();

        if (email.isEmpty()){
            login_email.setError("Email Required");
            login_email.requestFocus();
            return;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            login_email.setError("Email not valid");
            login_email.requestFocus();
            return;
        }
        else if (pass.isEmpty()){
            login_pass.setError("Password Required");
            login_pass.requestFocus();
            return;
        }
        else {
            userLogin(email,pass);
        }
    }

    private void userLogin(String email, String pass) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            finish();
                            // Sign in success, update UI with the signed-in user's information
                            startActivity(new Intent(login.this, UserProfile.class));
                            Toast.makeText(login.this, "Login success", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if (task.getException() instanceof FirebaseAuthInvalidUserException){
                                progressBar.setVisibility(View.GONE);
                                login_email.setError("Email not Registered");
                                login_email.requestFocus();
                                return;
                            }
                            else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                // If sign in fails, display a message to the user.
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(login.this, "Email and password do not match !", Toast.LENGTH_SHORT).show();
                            }

                            else{
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }


                        }
                    }
                });
    }

    //to keep the user logged in screen after one login
    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this,UserProfile.class));
        }
    }


    //To show the one time message
    private void showStartDialog() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("One Time Dialog")
                .setMessage("Thank You for Installing AudioRoot!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
        SharedPreferences preferences= getSharedPreferences("preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("firstStart",false);
        editor.apply();
    }

}

