package com.example.ezcharity.fragments.logs.ModelAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezcharity.R;

import java.util.List;

public class AdminLogAdapter extends RecyclerView.Adapter<AdminLogAdapter.ViewHolder> {

    private List<AdminLog> logs;
    private OnItemLongClickListener longClickListener; // Interface for long-click

    public AdminLogAdapter(List<AdminLog> logs) {
        this.logs = logs;
    }

    // Interface to handle long-clicks
    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdminLog log = logs.get(position);
        holder.username.setText("User: " + log.getUsername());
        holder.type.setText("Type: " + log.getType());
        holder.organisation.setText("Organisation: " + log.getOrganisation());
        holder.type2.setText("Status: " + log.getType2());

        // Set long-click listener on the itemView
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(position);
            }
            return true; // Consume the event
        });
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, type, organisation, type2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.log_username);
            type = itemView.findViewById(R.id.log_type);
            organisation = itemView.findViewById(R.id.log_organisation);
            type2 = itemView.findViewById(R.id.log_type2);
        }
    }
}
