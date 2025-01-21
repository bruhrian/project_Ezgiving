package com.example.paymentui3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView organisationImageView;
    private Spinner organisationSpinner;
    private EditText amountEditText;
    private Button proceedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the views
        organisationImageView = findViewById(R.id.orgImageView);
        organisationSpinner = findViewById(R.id.orgSpinner);
        amountEditText = findViewById(R.id.amount_ET);
        proceedButton = findViewById(R.id.ProceedPaymentBtn);

        // Set up the spinner with organisation names
        String[] organisations = {"None", "Organisation A", "Organisation B", "Organisation C"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, organisations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        organisationSpinner.setAdapter(adapter);

        // Handle spinner item selection
        organisationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOrganisation = parent.getItemAtPosition(position).toString();
                if (selectedOrganisation.equals("None")) {
                    organisationImageView.setVisibility(View.INVISIBLE);
                } else {
                    organisationImageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });

        proceedButton.setOnClickListener(v -> {
            String selectedOrganisation = organisationSpinner.getSelectedItem().toString();
            String amount = amountEditText.getText().toString().trim();

            if (selectedOrganisation.equals("None")) {
                Toast.makeText(MainActivity.this, "Please select an organisation.", Toast.LENGTH_SHORT).show();
            } else if (amount.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter a valid amount.", Toast.LENGTH_SHORT).show();
            } else {
                // Show the toast
                String message = "Organisation: " + selectedOrganisation + "\nAmount: $" + amount;
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();

                // Navigate to the next activity
                Intent intent = new Intent(MainActivity.this, choosePayment.class);
                intent.putExtra("organisation", selectedOrganisation);
                intent.putExtra("amount", amount);
                startActivity(intent);
            }
        });
    }
}