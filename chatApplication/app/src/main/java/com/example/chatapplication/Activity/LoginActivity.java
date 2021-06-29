package com.example.chatapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText mEmail,mPassword;
    Button mLoginButton;
    TextView mCreateBtn;
    ProgressBar progressBar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mEmail=findViewById(R.id.emaillog);
        mPassword=findViewById(R.id.editTextPasswordlog);
        progressBar=findViewById(R.id.progressBar);
        mLoginButton=findViewById(R.id.btn_log);
        mCreateBtn=findViewById(R.id.mCreateBtn);
        auth=FirebaseAuth.getInstance();

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=mEmail.getText().toString().trim();
                String pass=mPassword.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    mPassword.setError("Password is Required");
                    return;
                }
                if(pass.length()<6){
                    mPassword.setError("Password must be >=6 characters");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this,"User Created.",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), AnaActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Error."+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

    }
}