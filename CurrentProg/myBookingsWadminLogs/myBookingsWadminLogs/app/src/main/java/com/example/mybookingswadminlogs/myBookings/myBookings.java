package com.example.mybookingswadminlogs.myBookings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.mybookingswadminlogs.R;
import com.example.mybookingswadminlogs.myBookings.adminlogs.AdminLogsActivity;
import com.example.mybookingswadminlogs.myBookings.adminlogs.AdminLogsDBHelper;

import java.util.ArrayList;
import java.util.List;

public class myBookings extends AppCompatActivity implements BookingAdapter.OnBookingActionListener {

    private RecyclerView recyclerView;
    private BookingAdapter bookingAdapter;
    private DBmain dbMain;
    private SQLiteDatabase sqLiteDatabase;
    private List<BookingItem> bookingList;
    private Button ToAdminLogs_btn;

    // Initialize AdminLogsDBHelper for updating logs
    private AdminLogsDBHelper adminLogsDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        recyclerView = findViewById(R.id.myBookingList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbMain = new DBmain(this);
        sqLiteDatabase = dbMain.getWritableDatabase(); // Use writable database for deletion
        adminLogsDBHelper = new AdminLogsDBHelper(this); // Initialize AdminLogsDBHelper

        bookingList = fetchBookingsFromDatabase();
        bookingAdapter = new BookingAdapter(bookingList, this, this); // Pass the listener
        recyclerView.setAdapter(bookingAdapter);

        ToAdminLogs_btn = findViewById(R.id.ToTheLogs_btn);
        ToAdminLogs_btn.setOnClickListener(view -> {
            Intent intent = new Intent(myBookings.this, AdminLogsActivity.class);
            startActivity(intent);
        });
    }

    private List<BookingItem> fetchBookingsFromDatabase() {
        List<BookingItem> bookingList = new ArrayList<>();

        // Updated query to fetch COL_TYPE2 (Confirmed/Cancelled)
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT id, type, organization, COL_TYPE2 FROM " + DBmain.TABLE_NAME, null);
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
        // Delete from SQLite database
        int rowsDeleted = sqLiteDatabase.delete(DBmain.TABLE_NAME, "id = ?", new String[]{String.valueOf(bookingItem.getId())});
        if (rowsDeleted > 0) {
            // Update Admin Logs to mark booking as cancelled
            int rowsUpdated = adminLogsDBHelper.updateLogToCancelled(bookingItem.getOrganization());

            if (rowsUpdated > 0) {
                Toast.makeText(this, "Booking deleted and marked as cancelled in Admin Logs!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Booking deleted but failed to update Admin Logs!", Toast.LENGTH_SHORT).show();
            }

            // Remove from the list and notify the adapter
            bookingList.get(position).setType2("Cancelled"); // Update UI status
            bookingAdapter.notifyItemChanged(position);
        } else {
            Toast.makeText(this, "Failed to delete booking.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
        }
    }
}
