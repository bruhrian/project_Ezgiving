package com.example.ezcharity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ezcharity.fragments.logs.ModelAdapter.AdminLogsDBHelper;
import com.example.ezcharity.fragments.mybookings.myBookings;
import com.example.ezcharity.fragments.mybookings.ModelAdapter.myBookingsDB;


public class ConfirmationActivity extends AppCompatActivity {

    private TextView confirmationText;
    private myBookingsDB myBookingsDB;
    private AdminLogsDBHelper adminLogsDBHelper;

    private static final String PREFS_NAME = "UserPrefs"; // Same as Login.java
    private static final String EMAIL_KEY = "user_email"; // Key to get stored email

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        Button returnButton = findViewById(R.id.returnButton);
        Button MyBookingsButton = findViewById(R.id.MyBookingsButton);

        // Initialize the database helper instances
        myBookingsDB = new myBookingsDB(this);
        adminLogsDBHelper = new AdminLogsDBHelper(this);

        // Retrieve logged-in user's email
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String userEmail = sharedPreferences.getString(EMAIL_KEY, "UnknownUser");

        confirmationText = findViewById(R.id.confirmationText);

        // Retrieve data from Intent
        String organization = getIntent().getStringExtra("organization");
        String date = getIntent().getStringExtra("date");  // From addBooking
        String amount = getIntent().getStringExtra("amount");  // From addDonation

        // Build confirmation message
        StringBuilder confirmationMessage = new StringBuilder("Thank you for your action!\n\n");

        if (organization != null) {
            confirmationMessage.append("Organization: ").append(organization).append("\n");
        }
        if (date != null) {
            confirmationMessage.append("Booking Date: ").append(date).append("\n");

            // Insert booking into the main database
            long result = myBookingsDB.insertRecord("booking date: " + date, organization);

            if (result != -1) {
                Toast.makeText(getApplicationContext(), "Booking Confirmed", Toast.LENGTH_SHORT).show();

                // Log booking into Admin Logs database
                long logResult = adminLogsDBHelper.insertLog(userEmail, "booking date: " + date, organization);
                if (logResult != -1) {
                    Toast.makeText(getApplicationContext(), "Booking Logged Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to log booking to Admin Logs!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Error in booking!", Toast.LENGTH_SHORT).show();
            }
        }
        if (amount != null) {
            confirmationMessage.append("Donation Amount: $").append(amount).append("\n");

            // Insert donation into the main database
            long result = myBookingsDB.insertRecord("donation: " + amount, organization);

            if (result != -1) {
                Toast.makeText(getApplicationContext(), "Donation Successful", Toast.LENGTH_SHORT).show();

                // Log donation into Admin Logs database
                long logResult = adminLogsDBHelper.insertLog(userEmail, "donation: " + amount, organization);
                if (logResult != -1) {
                    Toast.makeText(getApplicationContext(), "Donation Logged Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to log donation to Admin Logs!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Error in donation!", Toast.LENGTH_SHORT).show();
            }
        }

        confirmationText.setText(confirmationMessage.toString());

        // Return to home page
        returnButton.setOnClickListener(v -> {
            Intent i = new Intent(ConfirmationActivity.this, MainActivity.class);
            startActivity(i);
        });

        // Fix this navigation button later
        MyBookingsButton.setOnClickListener(v -> {
            myBookings fragment = new myBookings();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

}
