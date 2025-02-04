package com.example.mybookingswadminlogs.myBookings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mybookingswadminlogs.R;
import com.example.mybookingswadminlogs.myBookings.adminlogs.AdminLogsDBHelper;

public class MainActivity extends AppCompatActivity {
    private TextView confirmationTxt;
    private Button bookingBtn, donationBtn;
    private DBmain dbHelper;

    // Initialize AdminLogsDBHelper for logging to Admin Logs
    private AdminLogsDBHelper adminLogsDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize database helpers
        dbHelper = new DBmain(this);
        adminLogsDBHelper = new AdminLogsDBHelper(this);

        // Find UI elements
        confirmationTxt = findViewById(R.id.DynamicConfirmTxt);
        bookingBtn = findViewById(R.id.bookingbtn);
        donationBtn = findViewById(R.id.donobtn);

        // Dummy organization name (replace with actual dynamic value if needed)
        String organizationName = "Charity Organization";

        // Booking button click listener
        bookingBtn.setOnClickListener(v -> {
            // Insert booking into the main database
            long result = dbHelper.insertRecord("booking", organizationName);

            if (result != -1) {
                Toast.makeText(getApplicationContext(), "Booking Confirmed", Toast.LENGTH_SHORT).show();
                confirmationTxt.setText("Booking Confirmed");

                // Log booking into Admin Logs database
                long logResult = adminLogsDBHelper.insertLog("User@chat.com", "booking", organizationName);

                if (logResult != -1) { // Log insertion succeeded
                    Toast.makeText(getApplicationContext(), "Booking Logged Successfully", Toast.LENGTH_SHORT).show();
                } else { // Log insertion failed
                    Toast.makeText(getApplicationContext(), "Failed to log booking to Admin Logs!", Toast.LENGTH_SHORT).show();
                }

                navigateToMyBookings(); // Navigate to your existing MyBookingsActivity
            } else {
                Toast.makeText(getApplicationContext(), "Error in booking!", Toast.LENGTH_SHORT).show();
            }
        });

        // Donation button click listener
        donationBtn.setOnClickListener(v -> {
            // Insert donation into the main database
            long result = dbHelper.insertRecord("donation", organizationName);

            if (result != -1) {
                Toast.makeText(getApplicationContext(), "Donation Successful", Toast.LENGTH_SHORT).show();
                confirmationTxt.setText("Donated Successfully");

                // Log donation into Admin Logs database
                long logResult = adminLogsDBHelper.insertLog("User@chat.com", "donation", organizationName);

                if (logResult != -1) { // Log insertion succeeded
                    Toast.makeText(getApplicationContext(), "Donation Logged Successfully", Toast.LENGTH_SHORT).show();
                } else { // Log insertion failed
                    Toast.makeText(getApplicationContext(), "Failed to log donation to Admin Logs!", Toast.LENGTH_SHORT).show();
                }

                navigateToMyBookings(); // Navigate to your existing MyBookingsActivity
            } else {
                Toast.makeText(getApplicationContext(), "Error in donation!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Navigate to MyBookingsActivity (kept from your original code)
    private void navigateToMyBookings() {
        Intent intent = new Intent(MainActivity.this, myBookings.class);
        startActivity(intent);
    }
}
