package com.example.bookingapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        TextView confirmationText = findViewById(R.id.confirmationText);
        Button returnButton = findViewById(R.id.returnButton);

        // Retrieve the data passed from MainActivity
        String organization = getIntent().getStringExtra("organization");
        String date = getIntent().getStringExtra("date");

        // Display the confirmation message
        String message = "Booking Confirmed!\n\nOrganization: " + organization + "\nDate: " + date;
        confirmationText.setText(message);

        // Return to home page
        returnButton.setOnClickListener(v -> finish());
    }
}
