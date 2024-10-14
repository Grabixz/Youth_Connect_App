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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventsViewPage extends AppCompatActivity {

    RecyclerView eventRV;
    ArrayList<Events> eventsArrayList;
    EventsRVAdapter eventsRVAdapter;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    ProgressBar loadingPB;
    Button btn_go_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_events_view_page);

        eventRV = findViewById(R.id.eventsRV);
        loadingPB = findViewById(R.id.eventsPB);
        btn_go_back = findViewById(R.id.btn_go_back);
        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        eventsArrayList = new ArrayList<>();
        eventRV.setHasFixedSize(true);
        eventRV.setLayoutManager(new LinearLayoutManager(this));
        eventsRVAdapter = new EventsRVAdapter(eventsArrayList, this);
        eventRV.setAdapter(eventsRVAdapter);

        btn_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                DocumentReference df = null;
                if (currentUser != null) {
                    df = fStore.collection("Users").document(currentUser.getUid());
                    df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(Objects.equals(documentSnapshot.getString("userAdmin"), "1")){
                                startActivity(new Intent(getApplicationContext(),EventsPage.class));
                                finish();
                            } else {
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();
                            }
                        }
                    });
                }
            }
        });

        fStore.collection("Events").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    loadingPB.setVisibility(View.GONE);
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        Events e = d.toObject(Events.class);
                        eventsArrayList.add(e);
                    }
                } else {
                    Toast.makeText(EventsViewPage.this, "No events found in database", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EventsViewPage.this, "Failed to get event data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}