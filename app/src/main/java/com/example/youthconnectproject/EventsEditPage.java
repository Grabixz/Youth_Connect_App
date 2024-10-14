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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class EventsEditPage extends AppCompatActivity {

    TextInputEditText editTextEventName, editTextEventDescription, editTextEventActivities, editTextEventDate;
    Button btn_go_back, btn_update_event;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_events_edit_page);

        editTextEventName = findViewById(R.id.edit_event_name);
        editTextEventDescription = findViewById(R.id.event_description);
        editTextEventActivities = findViewById(R.id.event_activities);
        editTextEventDate = findViewById(R.id.event_date);
        btn_update_event = findViewById(R.id.btn_update_event);
        btn_go_back = findViewById(R.id.btn_go_back);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

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

        Intent intent = getIntent();
        String eventName = intent.getStringExtra("eventName");
        String eventDescription = intent.getStringExtra("eventDescription");
        String eventActivities = intent.getStringExtra("eventActivities");
        String eventDate = intent.getStringExtra("eventDate");
        String eventID = intent.getStringExtra("eventID");

        editTextEventName.setText(eventName);
        editTextEventDescription.setText(eventDescription);
        editTextEventActivities.setText(eventActivities);
        editTextEventDate.setText(eventDate);

        btn_update_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_name, event_description, event_activities, event_date;
                event_name = String.valueOf(editTextEventName.getText());
                event_date = String.valueOf(editTextEventDate.getText());
                event_description = String.valueOf(editTextEventDescription.getText());
                event_activities = String.valueOf(editTextEventActivities.getText());

                if (TextUtils.isEmpty(event_name)){
                    editTextEventName.setError("Event name can't be empty!");
                }
                if (TextUtils.isEmpty(event_date)) {
                    editTextEventDate.setError("Event Date can't be empty!");
                }
                if (TextUtils.isEmpty(event_description)) {
                    editTextEventDescription.setError("Event description can't be empty!");
                }
                if (TextUtils.isEmpty(event_activities)) {
                    editTextEventActivities.setError("Event activities can't be empty!");
                }
                if (!TextUtils.isEmpty(event_name) && !TextUtils.isEmpty(event_date) && !TextUtils.isEmpty(event_description) && !TextUtils.isEmpty(event_activities)) {
                    updateEventData(eventID, event_name, event_date, event_description, event_activities);
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

    private void updateEventData(String eventID, String eventName, String eventDate, String eventDescription, String eventActivities) {
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = Objects.requireNonNull(user).getUid();
        DocumentReference docRef = fStore.collection("Users").document(userId);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String updated_by = documentSnapshot.getString("userFirstName") + " " + documentSnapshot.getString("userLastName");
                DocumentReference df = fStore.collection("Events").document(eventID);
                Map<String, Object> updates = new HashMap<>();
                updates.put("eventName", eventName);
                updates.put("eventDate", eventDate);
                updates.put("eventDescription", eventDescription);
                updates.put("eventActivities", eventActivities);
                updates.put("eventUploaded", updated_by);
                df.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EventsEditPage.this, "Event Info Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),EventsViewPage.class));
                        finish();
                    }
                });
            }
        });
    }
}