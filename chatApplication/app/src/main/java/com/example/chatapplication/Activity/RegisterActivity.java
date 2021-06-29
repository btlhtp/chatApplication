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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button mRegisterBtn;
    TextView mLoginbtn;
    FirebaseAuth auth;
    ProgressBar progressBar;
    FirebaseDatabase database;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.editTextPassword);

        mRegisterBtn = findViewById(R.id.btn_register);
        mLoginbtn = findViewById(R.id.registerBtn);

        auth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar2);

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), AnaActivity.class));
            finish();

        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String pass = mPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    mPassword.setError("Password is Required");
                    return;
                }
                if (pass.length() < 6) {
                    mPassword.setError("Password must be >=6 characters");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            database=FirebaseDatabase.getInstance();

                          reference=database.getInstance().getReference().child("Kullanıcılar").child(auth.getUid());
                            Map map= new HashMap();
                            map.put("resim","null");
                            map.put("isim","null");
                            map.put("egitim","null");
                            map.put("dogumtarihi","null");
                            map.put("hakkimda","null");
                            reference.setValue(map);


                            Toast.makeText( RegisterActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), AnaActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Error." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                progressBar.setVisibility(View.INVISIBLE);
            }

        });
        mLoginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

    }
}