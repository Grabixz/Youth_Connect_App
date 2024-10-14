package com.example.youthconnectproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AccountPage extends AppCompatActivity {

    Button btn_go_back, btn_update_password, btn_update_profile, btn_make_admin, btn_delete_user;
    TextInputEditText editTextFirstName, editTextLastName;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_page);
        fStore= FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();
        btn_update_password = findViewById(R.id.btn_update_password);
        btn_update_profile = findViewById(R.id.btn_update_profile);
        btn_make_admin = findViewById(R.id.btn_make_admin);
        btn_delete_user = findViewById(R.id.btn_delete_user);
        btn_go_back = findViewById(R.id.btn_go_back);
        editTextFirstName = findViewById(R.id.first_name);
        editTextLastName = findViewById(R.id.last_name);

        Intent intent = getIntent();
        String intent_userFirstName = intent.getStringExtra("userFirstName");
        String intent_userLastName = intent.getStringExtra("userLastName");
        String intent_userID = intent.getStringExtra("userID");
        String intent_userAdmin = intent.getStringExtra("userAdmin");

        editTextFirstName.setText(intent_userFirstName);
        editTextLastName.setText(intent_userLastName);
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = Objects.requireNonNull(user).getUid();
        DocumentReference docRef = fStore.collection("Users").document(userId);

        if (userId.equals(intent_userID) || intent_userID == null) {
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        editTextFirstName.setText(documentSnapshot.getString("userFirstName"));
                        editTextLastName.setText(documentSnapshot.getString("userLastName"));
                        btn_update_password.setVisibility(View.VISIBLE);
                        btn_update_profile.setVisibility(View.VISIBLE);
                        btn_delete_user.setVisibility(View.VISIBLE);
                        btn_make_admin.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            editTextFirstName.setText(intent_userFirstName);
            editTextLastName.setText(intent_userLastName);
            btn_update_password.setVisibility(View.GONE);
            btn_update_profile.setVisibility(View.GONE);
            btn_delete_user.setVisibility(View.GONE);
            btn_make_admin.setVisibility(View.VISIBLE);
            if (Objects.equals(intent_userAdmin, "1")) {
                btn_make_admin.setText("Remove as Admin");
                btn_make_admin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DocumentReference df = fStore.collection("Users").document(intent_userID);
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("userAdmin", "0");
                        df.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AccountPage.this, "User Removed as Admin", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),UsersViewPage.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AccountPage.this, "Failed to remove user as Admin", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            } else {
                btn_make_admin.setText("Make an Admin");
                btn_make_admin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DocumentReference df = fStore.collection("Users").document(intent_userID);
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("userAdmin", "1");
                        df.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AccountPage.this, "User Added as Admin", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),UsersViewPage.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AccountPage.this, "Failed to make user an Admin", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }

        btn_delete_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    currentUser.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AccountPage.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AccountPage.this, LoginPage.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AccountPage.this, "Failed to delete Account from Auth: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        btn_update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),PasswordPage.class));
            }
        });

        btn_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = Objects.requireNonNull(editTextFirstName.getText()).toString().trim();
                String lastName = Objects.requireNonNull(editTextLastName.getText()).toString().trim();

                if (firstName.isEmpty()){
                    editTextFirstName.setError("First Name cannot be empty");
                }
                if (lastName.isEmpty()) {
                    editTextFirstName.setError("Last Name cannot be empty");
                }
                if (!firstName.isEmpty() && !lastName.isEmpty()){
                    updateUserInformation(firstName, lastName);
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

    public void updateUserInformation(String newFirstName, String newLastName) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            DocumentReference df = fStore.collection("Users").document(user.getUid());
            Map<String, Object> updates = new HashMap<>();
            updates.put("userFirstName", newFirstName);
            updates.put("userLastName", newLastName);
            df.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(AccountPage.this, "Account Information Updated", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AccountPage.this, "Failed to Update Account Info", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}