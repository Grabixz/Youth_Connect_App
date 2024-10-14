package com.example.youthconnectproject;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class EventsRVAdapter extends RecyclerView.Adapter<EventsRVAdapter.ViewHolder> {
    ArrayList<Events> eventsArrayList;
    Context context;

    public EventsRVAdapter(ArrayList<Events> eventsArrayList, Context context) {
        this.eventsArrayList = eventsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public EventsRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.event_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Events event = eventsArrayList.get(position);
        holder.eventNameTV.setText(event.getEventName());
        holder.eventDescriptionTV.setText(event.getEventDescription());
        holder.eventActivitiesTV.setText(event.getEventActivities());
        holder.eventDateTV.setText(event.getEventDate());
        holder.eventUploadTV.setText(event.getEventUploaded());

        Button btn_edit_event = holder.itemView.findViewById(R.id.btn_edit_event);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseFirestore.getInstance().collection("Users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    if(Objects.equals(documentSnapshot.getString("userAdmin"), "1")) {
                        btn_edit_event.setVisibility(View.VISIBLE);
                        btn_edit_event.setOnClickListener(view -> {
                            Intent intent = new Intent(context, EventsEditPage.class);
                            intent.putExtra("eventName", event.getEventName());
                            intent.putExtra("eventDescription", event.getEventDescription());
                            intent.putExtra("eventActivities", event.getEventActivities());
                            intent.putExtra("eventDate", event.getEventDate());
                            intent.putExtra("eventID", event.getEventID());
                            context.startActivity(intent);
                        });
                    } else {
                        btn_edit_event.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return eventsArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView eventNameTV, eventDescriptionTV, eventActivitiesTV, eventDateTV, eventUploadTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventNameTV = itemView.findViewById(R.id.event_name_card);
            eventDescriptionTV = itemView.findViewById(R.id.event_description_card);
            eventActivitiesTV = itemView.findViewById(R.id.event_activities_card);
            eventDateTV = itemView.findViewById(R.id.event_date_card);
            eventUploadTV = itemView.findViewById(R.id.event_uploaded_card);
        }
    }
}
