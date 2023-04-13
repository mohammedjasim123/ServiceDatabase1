package com.example.servicedatabase1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText email,password,confimrpwd;

    TextView login;
    Button register;
    FirebaseAuth mauth;
    FirebaseDatabase firebaseDatabase;
    String UserID;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.txtemail1);
        password = findViewById(R.id.txtpwd1);
        confimrpwd = findViewById(R.id.txtcnfm);
        register = findViewById(R.id.btnregister);
        mauth = FirebaseAuth.getInstance();
        login = findViewById(R.id.tvlogin);

        firebaseDatabase = FirebaseDatabase.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createuser();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });
    }

    private void createuser() {

        String Email = email.getText().toString();
        String Password = password.getText().toString();
        String Confirm = confimrpwd.getText().toString();

        if (TextUtils.isEmpty(Email)){
            email.setError("Field cannot be empty");
            email.requestFocus();
        } else if (TextUtils.isEmpty(Password)) {
            password.setError("Field cannot be empty");
            password.requestFocus();
        } else if (TextUtils.isEmpty(Confirm)) {
            confimrpwd.setError("Field cannot be empty");
            confimrpwd.requestFocus();
        }  else if (TextUtils.equals(Confirm, Password)) {
            //Toast.makeText(this, "Password Match", Toast.LENGTH_SHORT).show();

            mauth.createUserWithEmailAndPassword(Email, Password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Map<String, Object> values = new HashMap<>();
                            values.put("Email", Email);
                            values.put("Password", Password);
                            databaseReference = firebaseDatabase.getReference().child("User Details");
                            databaseReference.setValue(values)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                email.setText("");
                                                password.setText("");
                                                confimrpwd.setText("");
                                                Toast.makeText(RegisterActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));

                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterActivity.this, "Failed to store data", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "User Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            confimrpwd.setError("Password does not match");
            confimrpwd.requestFocus();
        }
        }

    }
