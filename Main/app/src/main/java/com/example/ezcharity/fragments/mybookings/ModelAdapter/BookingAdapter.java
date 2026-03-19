package com.example.ezcharity.fragments.mybookings.ModelAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezcharity.R;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private final List<BookingItem> bookingList;
    private final OnBookingActionListener actionListener;
    private final Context context;

    // Fix constructor parameter order
    public BookingAdapter(List<BookingItem> bookingList, Context context, OnBookingActionListener actionListener) {
        this.bookingList = bookingList;
        this.context = context;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        BookingItem bookingItem = bookingList.get(position);
        holder.typeText.setText(bookingItem.getType());
        holder.organizationText.setText(bookingItem.getOrganization());

        // Set click listener for delete button
        holder.deleteButton.setOnClickListener(v -> showConfirmationDialog(bookingItem, position, v));
    }

    private void showConfirmationDialog(BookingItem bookingItem, int position, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext()); // Fix context issue
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_confirmation, null);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();

        TextView messageText = dialogView.findViewById(R.id.dialogMessage);
        messageText.setText("Are you sure you want to delete this listing?");

        dialogView.findViewById(R.id.dialogConfirmButton).setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onDelete(bookingItem, position);
            }
            dialog.dismiss();
        });

        dialogView.findViewById(R.id.dialogCancelButton).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView typeText, organizationText;
        ImageButton deleteButton;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            typeText = itemView.findViewById(R.id.typeText);
            organizationText = itemView.findViewById(R.id.organizationText);

            deleteButton = itemView.findViewById(R.id.delete_booking_btn);
            if (deleteButton == null) {
                Log.e("BookingAdapter", "deleteButton is null! Check item_booking.xml");
            }
        }
    }

    // Interface for delete action
    public interface OnBookingActionListener {
        void onDelete(BookingItem bookingItem, int position);
    }
}
