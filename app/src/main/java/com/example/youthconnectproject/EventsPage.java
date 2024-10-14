package com.example.youthconnectproject;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;
import java.util.Objects;

public class EventsPage extends AppCompatActivity {

    Button btn_go_back, btn_add_event, btn_view_events;
    TextInputEditText editTextEventName, editTextEventDescription, editTextEventActivities, editTextEventDate;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_events_page);
        btn_go_back = findViewById(R.id.btn_go_back);
        btn_add_event = findViewById(R.id.btn_add_event);
        btn_view_events = findViewById(R.id.btn_view_events);
        editTextEventName = findViewById(R.id.event_name);
        editTextEventDescription = findViewById(R.id.event_description);
        editTextEventActivities = findViewById(R.id.event_activities);
        editTextEventDate = findViewById(R.id.event_date);
        mAuth= FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();

        editTextEventDate.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select Date").build();
            datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            datePicker.addOnPositiveButtonClickListener(selection -> {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(selection);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                editTextEventDate.setText(sdf.format(calendar.getTime()));
            });
        });

        btn_add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_name, event_date, event_description, event_activities;
                event_name = String.valueOf(editTextEventName.getText());
                event_date = String.valueOf(editTextEventDate.getText());
                event_description = String.valueOf(editTextEventDescription.getText());
                event_activities = String.valueOf(editTextEventActivities.getText());

                if (TextUtils.isEmpty(event_name)) {
                    editTextEventName.setError("Event name can't be empty!");
                }
                if (TextUtils.isEmpty(event_date)){
                    editTextEventDate.setError("Event date can't be empty!");
                }
                if (TextUtils.isEmpty(event_description)) {
                    editTextEventDescription.setError("Event description can't be empty!");
                }
                if (TextUtils.isEmpty(event_activities)) {
                    editTextEventActivities.setError("Event activities can't be empty!");
                }
                if (!TextUtils.isEmpty(event_name) && !TextUtils.isEmpty(event_date) && !TextUtils.isEmpty(event_description) && !TextUtils.isEmpty(event_activities)){
                    addEventData(event_name, event_date, event_description, event_activities);
                }
            }
        });

        btn_view_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),EventsViewPage.class));
            }
        });

        btn_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdminPage.class));
                finish();
            }
        });
    }

    private void addEventData(String eventName, String eventDate, String eventDescription, String eventActivities) {
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = Objects.requireNonNull(user).getUid();
        DocumentReference docRef = fStore.collection("Users").document(userId);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String uploaded_by = documentSnapshot.getString("userFirstName") + " " + documentSnapshot.getString("userLastName");
                    CollectionReference dbEvents = fStore.collection("Events");

                    Events events = new Events(null, eventName, eventDate, eventDescription, eventActivities, uploaded_by);
                    dbEvents.add(events).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            String eventID = documentReference.getId();
                            events.setEventID(eventID);
                            documentReference.set(events);
                            Toast.makeText(EventsPage.this, "Your Event has been added successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),EventsPage.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EventsPage.this, "Fail to add event \n" + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}