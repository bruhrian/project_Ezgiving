package com.example.ezcharity.fragments.addbooking;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ezcharity.ConfirmationActivity;
import com.example.ezcharity.R;
import com.example.ezcharity.fragments.home.DBRV.DBmain;

import java.util.ArrayList;
import java.util.Calendar;

public class addBooking extends Fragment {

    private Spinner organizationSpinner;
    private TextView selectedDateText;
    private Button selectDateButton, proceedButton;
    private ImageView organizationImageView;
    private String selectedOrganization = "Select Organization", selectedDate;
    private SQLiteDatabase database;
    private ArrayList<Integer> organizationIds = new ArrayList<>(); // Store IDs for images

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Open or create the database
        database = context.openOrCreateDatabase("UserDatabase.db", Context.MODE_PRIVATE, null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_booking, container, false);

        organizationSpinner = view.findViewById(R.id.organizationSpinner);
        selectedDateText = view.findViewById(R.id.selectedDateText);
        selectDateButton = view.findViewById(R.id.selectDateButton);
        proceedButton = view.findViewById(R.id.proceedButton);
        organizationImageView = view.findViewById(R.id.orgImageView1);

        // Populate the spinner with data from the SQLite database
        ArrayList<String> organizations = fetchOrganizations();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, organizations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        organizationSpinner.setAdapter(adapter);

        // Set spinner value from the passed "selected_name" in the Bundle
        if (getArguments() != null) {
            String selectedName = getArguments().getString("selected_name");
            if (selectedName != null && organizations.contains(selectedName)) {
                int position = organizations.indexOf(selectedName);
                organizationSpinner.setSelection(position);
            }
        }

        // Listener for the spinner
        organizationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOrganization = organizations.get(position);

                if (position > 0) { // Skip "Select Organization"
                    organizationImageView.setVisibility(View.VISIBLE);
                    organizationImageView.setImageBitmap(getImageFromDB(organizationIds.get(position - 1))); // Adjust for "Select Organization" option
                } else {
                    organizationImageView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedOrganization = "Select Organization";
                organizationImageView.setVisibility(View.INVISIBLE);
            }
        });

        // Set up DatePicker
        selectDateButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                    (DatePicker view1, int selectedYear, int selectedMonth, int selectedDay) -> {
                        selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        selectedDateText.setText("Date: " + selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        // Handle Proceed button click
        proceedButton.setOnClickListener(v -> {
            if (!selectedOrganization.equals("Select Organization") && selectedDate != null) {
                Intent intent = new Intent(requireContext(), ConfirmationActivity.class);
                intent.putExtra("organization", selectedOrganization);
                intent.putExtra("date", selectedDate);
                startActivity(intent);
            } else {
                Toast.makeText(requireContext(), "Please select an organization and a date.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    private ArrayList<String> fetchOrganizations() {
        ArrayList<String> organizations = new ArrayList<>();
        organizations.add("Select Organization");

        DBmain dbMain = new DBmain(requireContext());
        Cursor cursor = dbMain.getBookingListings(); // Use the helper method from DBmain

        if (cursor != null && cursor.moveToFirst()) {
            try {
                int nameIndex = cursor.getColumnIndex("Name");
                int idIndex = cursor.getColumnIndex("ID");
                if (nameIndex == -1 || idIndex == -1) {
                    throw new IllegalArgumentException("Required columns do not exist in the database.");
                }

                do {
                    String name = cursor.getString(nameIndex);
                    int id = cursor.getInt(idIndex);
                    organizations.add(name);
                    organizationIds.add(id);
                } while (cursor.moveToNext());
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Error fetching organizations: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        } else {
            Toast.makeText(requireContext(), "No organizations found for bookings.", Toast.LENGTH_SHORT).show();
        }

        return organizations;
    }

    private Bitmap getImageFromDB(int organizationId) {
        Cursor cursor = database.rawQuery("SELECT Image FROM Users WHERE id = ?", new String[]{String.valueOf(organizationId)});
        if (cursor != null && cursor.moveToFirst()) {
            byte[] imageBytes = cursor.getBlob(0);
            cursor.close();
            if (imageBytes != null) {
                return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            }
        }
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (database != null) {
            database.close();
        }
    }
}
