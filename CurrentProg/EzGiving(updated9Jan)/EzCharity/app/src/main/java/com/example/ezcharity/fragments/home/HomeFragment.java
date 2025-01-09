package com.example.ezcharity.fragments.home;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ezcharity.R;
import com.example.ezcharity.fragments.topnav.AddListing;
import com.example.ezcharity.fragments.topnav.Notifications;
import com.example.ezcharity.fragments.topnav.SearchListing;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

// TBC on settling defaulting to nth on top of home page
public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the FloatingActionButtons
        FloatingActionButton addListingButton = view.findViewById(R.id.addlisting_btn);
        FloatingActionButton searchButton = view.findViewById(R.id.search_btn);
        FloatingActionButton notificationButton = view.findViewById(R.id.notification_btn);

        // Set onClickListeners for the buttons
        addListingButton.setOnClickListener(v -> openFragment(new AddListing()));
        searchButton.setOnClickListener(v -> openFragment(new SearchListing()));
        notificationButton.setOnClickListener(v -> openFragment(new Notifications()));
    }

    private void openFragment(Fragment fragment) {
        // Replace the current fragment with the new one
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null); // Add to backstack to enable back navigation
        transaction.commit();
    }
}
