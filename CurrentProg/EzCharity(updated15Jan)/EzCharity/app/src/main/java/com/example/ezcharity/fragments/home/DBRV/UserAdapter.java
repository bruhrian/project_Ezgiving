package com.example.ezcharity.fragments.home.DBRV;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezcharity.R;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private final Context context;
    private final ArrayList<User> userList;
    private final DBmain databaseHelper;

    public UserAdapter(Context context, ArrayList<User> userList, DBmain databaseHelper) {
        this.context = context;
        this.userList = userList;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.alist, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.nameTextView.setText(user.getName());
        holder.typeTextView.setText(user.getType());
        holder.taskTextView.setText(user.getTask());
        holder.locationTextView.setText(user.getLocation());

        Bitmap bitmap = BitmapFactory.decodeByteArray(user.getImage(), 0, user.getImage().length);
        holder.imageView.setImageBitmap(bitmap);

        // Handle cancel button click
        holder.cancelButton.setOnClickListener(v -> {
            boolean isDeleted = databaseHelper.deleteData(user.getId());
            if (isDeleted) {
                Toast.makeText(context, "Listing deleted", Toast.LENGTH_SHORT).show();
                userList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, userList.size());
            } else {
                Toast.makeText(context, "Failed to delete listing", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageButton cancelButton;
        TextView nameTextView, typeTextView, taskTextView, locationTextView;
        ImageView imageView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            cancelButton = itemView.findViewById(R.id.btn_deletelisting);
            nameTextView = itemView.findViewById(R.id.orgNameTextView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
            imageView = itemView.findViewById(R.id.imageView);
            taskTextView = itemView.findViewById(R.id.taskoractivityTextView);
            locationTextView = itemView.findViewById(R.id.LocationTextView);


        }
    }
}
