package com.example.ezcharity.fragments.adddonation;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ezcharity.ConfirmationActivity;
import com.example.ezcharity.R;
import com.example.ezcharity.fragments.adddonation.choosePayment;
import com.example.ezcharity.fragments.home.DBRV.DBmain;

import java.util.ArrayList;

public class addDonation extends Fragment {

    private ImageView organisationImageView;
    private Spinner organisationSpinner;
    private EditText amountEditText;
    private Button proceedButton;
    private DBmain dbMain;
    private SQLiteDatabase sqLiteDatabase;

    private ArrayList<String> organisationNames = new ArrayList<>();
    private ArrayList<Integer> organisationIds = new ArrayList<>(); // Organisation IDs for fetching images

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_donation, container, false);

        // Initialize views
        organisationImageView = view.findViewById(R.id.orgImageView);
        organisationSpinner = view.findViewById(R.id.orgSpinner);
        amountEditText = view.findViewById(R.id.amount_ET);
        proceedButton = view.findViewById(R.id.ProceedPaymentBtn);

        // Initialize database
        dbMain = new DBmain(getContext());
        sqLiteDatabase = dbMain.getReadableDatabase();

        // Load organisation data using the custom method
        loadDonationListings();

        // Set up spinner with organisation names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, organisationNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        organisationSpinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // Set spinner value from the passed "Name" in the Bundle
        if (getArguments() != null) {
            String selectedName = getArguments().getString("selected_name");
            if (selectedName != null && organisationNames.contains(selectedName)) {
                int position = organisationNames.indexOf(selectedName);
                organisationSpinner.setSelection(position);
            }
        }

        // Handle spinner item selection
        organisationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) { // Skip "None" selection
                    organisationImageView.setVisibility(View.VISIBLE);
                    organisationImageView.setImageBitmap(getImageFromDB(organisationIds.get(position - 1))); // Adjust for "None"
                } else {
                    organisationImageView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });

        // Handle proceed button click
        proceedButton.setOnClickListener(v -> {
            String selectedOrganisation = organisationSpinner.getSelectedItem().toString();
            String amount = amountEditText.getText().toString().trim();

            if (selectedOrganisation.equals("None")) {
                Toast.makeText(getContext(), "Please select an organisation.", Toast.LENGTH_SHORT).show();
            } else if (amount.isEmpty()) {
                Toast.makeText(getContext(), "Please enter a valid amount.", Toast.LENGTH_SHORT).show();
            } else {
                // Navigate to choosePayment and pass organisation and amount
                Intent intent = new Intent(getContext(), choosePayment.class);
                intent.putExtra("organization", selectedOrganisation);
                intent.putExtra("amount", amount);
                startActivity(intent);
            }
        });

        return view;
    }

    private void loadDonationListings() {
        organisationNames.clear();
        organisationIds.clear();
        organisationNames.add("None"); // Default "None" option

        Cursor cursor = getDonationListings();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                try {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                    organisationNames.add(name);
                    organisationIds.add(id);
                    Log.d("DB", "Loaded Organisation: " + name + ", ID: " + id);
                } catch (Exception e) {
                    Log.e("DB", "Error reading data: " + e.getMessage());
                }
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            Log.e("DB", "Cursor is null or empty. No data found in the Users table.");
            Toast.makeText(getContext(), "No organisations available. Please check your database.", Toast.LENGTH_SHORT).show();
        }

        // Notify adapter about data changes
        if (getContext() != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, organisationNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            organisationSpinner.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private Cursor getDonationListings() {
        // Replace with your SQL query to fetch donation listings from DBmain
        return sqLiteDatabase.rawQuery("SELECT ID, Name FROM Users WHERE Type = ?", new String[]{"Donation"});
    }

    private Bitmap getImageFromDB(int organisationId) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT Image FROM Users WHERE id = ?", new String[]{String.valueOf(organisationId)});
        if (cursor != null && cursor.moveToFirst()) {
            byte[] imageBytes = cursor.getBlob(0);
            cursor.close();
            if (imageBytes != null) {
                Log.d("DB", "Image loaded for ID: " + organisationId);
                return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            } else {
                Log.e("DB", "No image bytes found for ID: " + organisationId);
            }
        } else {
            Log.e("DB", "Cursor is null or no rows found for ID: " + organisationId);
        }
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sqLiteDatabase != null) sqLiteDatabase.close();
    }
}
