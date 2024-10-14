package com.example.youthconnectproject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class PasswordPage extends AppCompatActivity {

    Button btn_go_back, btn_update_password;
    TextInputEditText editTextEmail;
    FirebaseAuth mAuth;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_password_page);

        mAuth = FirebaseAuth.getInstance();
        btn_go_back = findViewById(R.id.btn_go_back);
        btn_update_password = findViewById(R.id.btn_update_password);
        editTextEmail = findViewById(R.id.email);

        btn_update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userEmail = Objects.requireNonNull(editTextEmail.getText()).toString().trim();
                if (!TextUtils.isEmpty(userEmail)){
                    ResetPassword();
                } else {
                    editTextEmail.setError("Email Field can't be empty!");
                }
            }
        });

        btn_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void ResetPassword() {
        mAuth.sendPasswordResetEmail(userEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(PasswordPage.this, "Reset Password link has been sent to your email", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}