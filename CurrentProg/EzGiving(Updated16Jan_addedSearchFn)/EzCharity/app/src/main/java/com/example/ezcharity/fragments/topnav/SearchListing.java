package com.example.ezcharity.fragments.topnav;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.ezcharity.R;
import com.example.ezcharity.fragments.home.DBRV.DBmain;
import com.example.ezcharity.fragments.home.DBRV.User;
import com.example.ezcharity.fragments.home.DBRV.UserAdapter;

import java.util.ArrayList;


public class SearchListing extends Fragment {

    private SearchView searchView;
    private RecyclerView searchRecyclerView;
    private UserAdapter adapter;
    private DBmain databaseHelper; // SQLite helper class

    public SearchListing() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_listing, container, false);

        // Initialize views
        searchView = view.findViewById(R.id.searchView);
        searchRecyclerView = view.findViewById(R.id.SearchRecyclerList);

        // Initialize SQLite helper and RecyclerView
        databaseHelper = new DBmain(getContext());
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load all data initially
        displayData("");

        // Set up search functionality
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                displayData(newText); // Filter data dynamically
                return true;
            }
        });

        return view;
    }

    private void displayData(String query) {
        Cursor cursor;
        if (TextUtils.isEmpty(query)) {
            // Load all data if the query is empty
            cursor = databaseHelper.getAllData();
        } else {
            // Filter data based on the search query
            cursor = databaseHelper.searchData(query);
        }

        ArrayList<User> userList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
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

        // Pass data to the adapter
        adapter = new UserAdapter(getContext(), userList, databaseHelper);
        searchRecyclerView.setAdapter(adapter);

        if (cursor != null) {
            cursor.close();
        }
    }
}