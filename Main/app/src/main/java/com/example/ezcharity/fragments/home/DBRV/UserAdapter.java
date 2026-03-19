package com.example.ezcharity.fragments.home.DBRV;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private final UserInterface userInterface;
    private final Context context;
    private final ArrayList<User> userList;
    private final DBmain databaseHelper;

    public UserAdapter(Context context, ArrayList<User> userList, DBmain databaseHelper, UserInterface userInterface) {
        this.context = context;
        this.userList = userList;
        this.databaseHelper = databaseHelper;
        this.userInterface = userInterface;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.alist, parent, false);
        return new UserViewHolder(view, userInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        if (user == null) {
            return;
        }

        holder.nameTextView.setText(user.getName());
        holder.typeTextView.setText(user.getType());
        holder.taskTextView.setText(user.getTask());
        holder.locationTextView.setText(user.getLocation());

        // Check if the image is null before decoding
        if (user.getImage() != null && user.getImage().length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(user.getImage(), 0, user.getImage().length);
            holder.imageView.setImageBitmap(bitmap);
        } else {
            // Set a default image or make the ImageView invisible
            holder.imageView.setImageResource(R.drawable.image2); // Replace with a valid drawable
        }

        // Get the current user's email
        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        // Set visibility of cancelButton based on email
        if ("admin@ezgiving.com".equals(currentUserEmail)) {
            holder.cancelButton.setVisibility(View.VISIBLE);

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
        } else {
            holder.cancelButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageButton cancelButton;
        TextView nameTextView, typeTextView, taskTextView, locationTextView;
        ImageView imageView;

        public UserViewHolder(@NonNull View itemView, UserInterface userInterface) {
            super(itemView);
            cancelButton = itemView.findViewById(R.id.btn_deletelisting);
            nameTextView = itemView.findViewById(R.id.orgNameTextView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
            imageView = itemView.findViewById(R.id.imageView);
            taskTextView = itemView.findViewById(R.id.taskoractivityTextView);
            locationTextView = itemView.findViewById(R.id.LocationTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (userInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            userInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}