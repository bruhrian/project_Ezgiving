package com.example.ezcharity.fragments.home;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ezcharity.R;
import com.example.ezcharity.fragments.addbooking.addBooking;
import com.example.ezcharity.fragments.adddonation.addDonation;
import com.example.ezcharity.fragments.home.DBRV.DBmain;
import com.example.ezcharity.fragments.home.DBRV.User;
import com.example.ezcharity.fragments.home.DBRV.UserAdapter;
import com.example.ezcharity.fragments.home.DBRV.UserInterface;
import com.example.ezcharity.fragments.logs.logs;
import com.example.ezcharity.fragments.topnav.AddListing;
import com.example.ezcharity.fragments.topnav.Notifications;
import com.example.ezcharity.fragments.topnav.SearchListing;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements UserInterface {

    private Button allButton, donationButton, bookingButton;
    private RecyclerView recyclerView;

    private DBmain databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        recyclerView = view.findViewById(R.id.contents);
        allButton = view.findViewById(R.id.btn_all_content);
        donationButton = view.findViewById(R.id.DonationFilter_btn);
        bookingButton = view.findViewById(R.id.BookingFilter_btn);
        databaseHelper = new DBmain(getContext());

        ImageSlider imageSlider = view.findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.commchest_news, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.ffth_news, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.muhammadiyah_news, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.scs_news, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.sgbuddhistclinic_news, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.willinghearts_news, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.sgheartfounds_news, ScaleTypes.FIT));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        // Set RecyclerView layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set button click listeners for filters
        allButton.setOnClickListener(v -> {
            displayData(databaseHelper.getAllData());
            //Toast.makeText(getContext(), "Showing all listings", Toast.LENGTH_SHORT).show();
        });

        donationButton.setOnClickListener(v -> {
            displayData(databaseHelper.getDonationListings());
            //Toast.makeText(getContext(), "Showing donation listings", Toast.LENGTH_SHORT).show();
        });

        bookingButton.setOnClickListener(v -> {
            displayData(databaseHelper.getBookingListings());
            //Toast.makeText(getContext(), "Showing booking listings", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the FloatingActionButtons
        FloatingActionButton addListingButton = view.findViewById(R.id.addlisting_btn);
        FloatingActionButton searchButton = view.findViewById(R.id.search_btn);
        FloatingActionButton adminlogsButton = view.findViewById(R.id.adminlogs_btn);

        // Retrieve email from SharedPreferences
        String email = requireContext().getSharedPreferences("UserPrefs", requireContext().MODE_PRIVATE)
                .getString("user_email", "");

        // Show or hide the addListingButton based on email
        if ("admin@ezgiving.com".equals(email)) {
            addListingButton.setVisibility(View.VISIBLE);
            adminlogsButton.setVisibility(View.VISIBLE);
        } else {
            addListingButton.setVisibility(View.GONE);
            adminlogsButton.setVisibility(View.GONE);
        }

        // Set onClickListeners for the buttons
        addListingButton.setOnClickListener(v -> {
            //Toast.makeText(getContext(), "Navigating to Add Listing", Toast.LENGTH_SHORT).show();
            openFragment(new AddListing());
        });

        searchButton.setOnClickListener(v -> {
            //Toast.makeText(getContext(), "Navigating to Search Listings", Toast.LENGTH_SHORT).show();
            openFragment(new SearchListing());
        });
        adminlogsButton.setOnClickListener(v -> {
            openFragment(new logs());
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        displayData(databaseHelper.getAllData());
    }

    private void openFragment(Fragment fragment) {
        // Replace the current fragment with the new one
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null); // Add to backstack to enable back navigation
        transaction.commit();
    }

    private void displayData(Cursor cursor) {
        ArrayList<User> userList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("ID"));
                String name = cursor.getString(cursor.getColumnIndex("Name"));
                String type = cursor.getString(cursor.getColumnIndex("Type"));
                String task = cursor.getString(cursor.getColumnIndex("Task"));
                String location = cursor.getString(cursor.getColumnIndex("Location"));
                byte[] image = cursor.getBlob(cursor.getColumnIndex("Image"));

                userList.add(new User(id, name, type, task, location, image));
            } while (cursor.moveToNext());
        }

        UserAdapter adapter = new UserAdapter(getContext(), userList, databaseHelper, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int pos) {
        // Retrieve the selected User object
        Cursor cursor = databaseHelper.getAllData();
        if (cursor.moveToPosition(pos)) {
            String type = cursor.getString(cursor.getColumnIndex("Type"));
            String name = cursor.getString(cursor.getColumnIndex("Name"));

            // Create a Bundle to pass data
            Bundle bundle = new Bundle();
            bundle.putString("selected_name", name);

            // Determine the fragment to navigate to based on the "Type"
            Fragment targetFragment;
            if ("Bookings".equalsIgnoreCase(type)) {
                targetFragment = new addBooking();
            } else if ("Donation".equalsIgnoreCase(type)) {
                targetFragment = new addDonation();
            } else {
                Toast.makeText(getContext(), "Unknown listing type", Toast.LENGTH_SHORT).show();
                return;
            }

            // Set arguments to the target fragment
            targetFragment.setArguments(bundle);

            // Navigate to the target fragment
            openFragment(targetFragment);
        } else {
            Toast.makeText(getContext(), "Failed to retrieve data for the clicked item", Toast.LENGTH_SHORT).show();
        }
    }

}
