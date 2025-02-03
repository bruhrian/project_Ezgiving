package com.example.mybookingswadminlogs.myBookings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mybookingswadminlogs.R;

public class MainActivity extends AppCompatActivity {
    private TextView confirmationTxt;
    private Button bookingBtn, donationBtn;
    private DBmain dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize database helper
        dbHelper = new DBmain(this);

        // Find UI elements
        confirmationTxt = findViewById(R.id.DynamicConfirmTxt);
        bookingBtn = findViewById(R.id.bookingbtn);
        donationBtn = findViewById(R.id.donobtn);

        // Dummy organization name (replace with actual dynamic value if needed)
        String organizationName = "Charity Organization";

        // Booking button click listener
        bookingBtn.setOnClickListener(v -> {
            // Insert booking into the database
            long result = dbHelper.insertRecord("booking", organizationName);

            if (result != -1) {
                Toast.makeText(getApplicationContext(), "Booking Confirmed", Toast.LENGTH_SHORT).show();
                confirmationTxt.setText("Booking Confirmed");
                navigateToMyBookings();
            } else {
                Toast.makeText(getApplicationContext(), "Error in booking!", Toast.LENGTH_SHORT).show();
            }
        });

        // Donation button click listener
        donationBtn.setOnClickListener(v -> {
            // Insert donation into the database
            long result = dbHelper.insertRecord("donation", organizationName);

            if (result != -1) {
                Toast.makeText(getApplicationContext(), "Donation Successful", Toast.LENGTH_SHORT).show();
                confirmationTxt.setText("Donated Successfully");
                navigateToMyBookings();
            } else {
                Toast.makeText(getApplicationContext(), "Error in donation!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Navigate to MyBookingsActivity
    private void navigateToMyBookings() {
        Intent intent = new Intent(MainActivity.this, myBookings.class);
        startActivity(intent);
    }
}
