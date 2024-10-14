package com.example.youthconnectproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;



public class RegisterPage extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword, editTextFirstName, editTextLastName;
    Button buttonReg;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    ProgressBar progressBar;
    TextView textView;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth= FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_page);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextFirstName = findViewById(R.id.first_name);
        editTextLastName = findViewById(R.id.last_name);
        buttonReg = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.loginNow);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(intent);
                finish();
            }
        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password, first_name, last_name;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                first_name = String.valueOf(editTextFirstName.getText());
                last_name = String.valueOf(editTextLastName.getText());

                if (TextUtils.isEmpty(first_name)){
                    editTextFirstName.setError("First Name field cannot be empty");
                    progressBar.setVisibility(View.GONE);
                } if (TextUtils.isEmpty(last_name)){
                    editTextLastName.setError("Last Name field cannot be empty");
                    progressBar.setVisibility(View.GONE);
                } if (TextUtils.isEmpty(email)){
                    editTextEmail.setError("Email field cannot be empty");
                    progressBar.setVisibility(View.GONE);
                } if (TextUtils.isEmpty(password)){
                    editTextPassword.setError("Password field cannot be empty");
                    progressBar.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(first_name) && !TextUtils.isEmpty(last_name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                    mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String uid = user.getUid();
                                Users newUser = new Users(uid, first_name, last_name, email, "0");
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                DocumentReference df = db.collection("Users").document(uid);
                                df.set(newUser).addOnSuccessListener(aVoid -> {
                                    Toast.makeText(RegisterPage.this, "Account Created", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(RegisterPage.this, "Failed to create account", Toast.LENGTH_SHORT).show();
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterPage.this, "Failed to create account: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}