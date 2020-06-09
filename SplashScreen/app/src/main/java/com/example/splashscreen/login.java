package com.example.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    private EditText login_email, login_pass;
    private FirebaseAuth mAuth;
    Button callSignUp,callMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login2);

        login_email=findViewById(R.id.login_email_id);
        login_pass=findViewById(R.id.login_pass_id);

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
            Toast.makeText(this, "Email empty", Toast.LENGTH_SHORT).show();
        }
        else if (pass.isEmpty()){
            Toast.makeText(this, "Password empty", Toast.LENGTH_SHORT).show();
        }
        else {
            userLogin(email,pass);
        }
    }

    private void userLogin(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            startActivity(new Intent(login.this, Dashboard.class));
                            Toast.makeText(login.this, "Login successs", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(login.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}

