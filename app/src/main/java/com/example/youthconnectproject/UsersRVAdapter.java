package com.example.youthconnectproject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class UsersRVAdapter extends RecyclerView.Adapter<UsersRVAdapter.ViewHolder> {
    ArrayList<Users> usersArrayList;
    Context context;

    public UsersRVAdapter(ArrayList<Users> usersArrayList, Context context) {
        this.usersArrayList = usersArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public UsersRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.user_items, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users user = usersArrayList.get(position);
        String full_name = user.getUserFirstName() + " " + user.getUserLastName();
        holder.userNameTV.setText(full_name);
        holder.userEmailTV.setText(user.getUserEmail());
        String admin_user = user.getUserAdmin();
        if (admin_user.equals("1")){
            holder.userAdminTV.setText("ADMIN USER");
        } else {
            holder.userAdminTV.setText("NORMAL USER");
        }

        Button btn_view_user = holder.itemView.findViewById(R.id.btn_view_user);
        btn_view_user.setOnClickListener(view -> {
            Intent intent = new Intent(context, AccountPage.class);
            intent.putExtra("userFirstName", user.getUserFirstName());
            intent.putExtra("userLastName", user.getUserLastName());
            intent.putExtra("userID", user.getUserId());
            intent.putExtra("userAdmin", user.getUserAdmin());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView userNameTV, userEmailTV, userAdminTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTV = itemView.findViewById(R.id.user_name_card);
            userEmailTV = itemView.findViewById(R.id.user_email_card);
            userAdminTV = itemView.findViewById(R.id.user_admin_card);
        }
    }
}
