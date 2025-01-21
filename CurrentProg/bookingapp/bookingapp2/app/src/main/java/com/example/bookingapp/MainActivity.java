package com.example.bookingapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Spinner organizationSpinner;
    private TextView selectedDateText;
    private Button selectDateButton, proceedButton;
    private String selectedOrganization = "Select Organization", selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        organizationSpinner = findViewById(R.id.organizationSpinner);
        selectedDateText = findViewById(R.id.selectedDateText);
        selectDateButton = findViewById(R.id.selectDateButton);
        proceedButton = findViewById(R.id.proceedButton);

        // Populate the spinner with options
        String[] organizations = {"Select Organization", "Willing Hearts", "SG Cancer Society", "Community Chest"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, organizations);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        organizationSpinner.setAdapter(adapter);

        // Listener for the spinner
        organizationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOrganization = organizations[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedOrganization = "Select Organization";
            }
        });

        // Set up DatePicker
        selectDateButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                    (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                        selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        selectedDateText.setText("Date: " + selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        // Handle Proceed button click
        proceedButton.setOnClickListener(v -> {
            if (!selectedOrganization.equals("Select Organization") && selectedDate != null) {
                Intent intent = new Intent(MainActivity.this, ConfirmationActivity.class);
                intent.putExtra("organization", selectedOrganization);
                intent.putExtra("date", selectedDate);
                startActivity(intent);
            } else {
                selectedDateText.setText("Please select an organization and a date.");
            }
        });
    }
}
