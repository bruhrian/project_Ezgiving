package com.example.ezcharity.fragments.mybookings;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezcharity.R;
import com.example.ezcharity.fragments.logs.ModelAdapter.AdminLogsDBHelper;
import com.example.ezcharity.fragments.mybookings.ModelAdapter.myBookingsDB;
import com.example.ezcharity.fragments.mybookings.ModelAdapter.BookingAdapter;
import com.example.ezcharity.fragments.mybookings.ModelAdapter.BookingItem;

import java.util.ArrayList;
import java.util.List;

public class myBookings extends Fragment implements BookingAdapter.OnBookingActionListener {

    private RecyclerView recyclerView;
    private BookingAdapter bookingAdapter;
    private myBookingsDB dbMain;
    private SQLiteDatabase sqLiteDatabase;
    private List<BookingItem> bookingList;

    // AdminLogsDBHelper for updating logs
    private AdminLogsDBHelper adminLogsDBHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_bookings, container, false);

        recyclerView = view.findViewById(R.id.myBookingList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getContext() != null) {
            dbMain = new myBookingsDB(getContext());
            sqLiteDatabase = dbMain.getWritableDatabase(); // Use writable database for deletion
            adminLogsDBHelper = new AdminLogsDBHelper(getContext()); // Initialize AdminLogsDBHelper
        }

        bookingList = fetchBookingsFromDatabase();
        bookingAdapter = new BookingAdapter(bookingList, getContext(), this); // Pass the listener
        recyclerView.setAdapter(bookingAdapter);

        return view;
    }

    private List<BookingItem> fetchBookingsFromDatabase() {
        List<BookingItem> bookingList = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT id, type, organization, COL_TYPE2 FROM " + myBookingsDB.TABLE_NAME, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0); // Get the ID for deletion purposes
                String type = cursor.getString(1);
                String organization = cursor.getString(2);
                String type2 = cursor.getString(3); // Fetch COL_TYPE2

                bookingList.add(new BookingItem(id, type, organization, type2)); // Updated constructor
            } while (cursor.moveToNext());
            cursor.close();
        }
        return bookingList;
    }

    @Override
    public void onDelete(BookingItem bookingItem, int position) {
        int rowsDeleted = sqLiteDatabase.delete(myBookingsDB.TABLE_NAME, "id = ?", new String[]{String.valueOf(bookingItem.getId())});
        if (rowsDeleted > 0) {
            // Update Admin Logs to mark booking as cancelled
            if (getContext() != null) {
                int rowsUpdated = adminLogsDBHelper.updateLogToCancelled(bookingItem.getOrganization());

                if (rowsUpdated > 0) {
                    //Toast.makeText(getContext(), "Booking deleted and marked as cancelled in Admin Logs!", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getContext(), "Booking deleted but failed to update Admin Logs!", Toast.LENGTH_SHORT).show();
                }
            }

            // Remove from the list and notify the adapter
            bookingList.get(position).setType2("Cancelled"); // Update UI status
            bookingAdapter.notifyItemChanged(position);
        } else {
            Toast.makeText(getContext(), "Failed to delete booking.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
        }
    }
}
