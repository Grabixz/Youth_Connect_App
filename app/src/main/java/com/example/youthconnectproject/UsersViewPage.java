package com.example.youthconnectproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UsersViewPage extends AppCompatActivity {

    RecyclerView userRV;
    ArrayList<Users> usersArrayList;
    UsersRVAdapter usersRVAdapter;
    FirebaseFirestore fStore;
    ProgressBar loadingPB;
    Button btn_go_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_users_view_page);

        userRV = findViewById(R.id.usersRV);
        loadingPB = findViewById(R.id.usersPB);
        btn_go_back = findViewById(R.id.btn_go_back);
        fStore = FirebaseFirestore.getInstance();

        usersArrayList = new ArrayList<>();
        userRV.setHasFixedSize(true);
        userRV.setLayoutManager(new LinearLayoutManager(this));
        usersRVAdapter = new UsersRVAdapter(usersArrayList, this);
        userRV.setAdapter(usersRVAdapter);

        btn_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AdminPage.class));
                finish();
            }
        });

        fStore.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    loadingPB.setVisibility(View.GONE);
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        Users u = d.toObject(Users.class);
                        usersArrayList.add(u);
                    }
                } else {
                    Toast.makeText(UsersViewPage.this, "No users found in database", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UsersViewPage.this, "Failed to get user data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}