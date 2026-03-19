package com.example.ezcharity.fragments.logs;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ezcharity.R;
import com.example.ezcharity.fragments.logs.ModelAdapter.AdminLog;
import com.example.ezcharity.fragments.logs.ModelAdapter.AdminLogAdapter;
import com.example.ezcharity.fragments.logs.ModelAdapter.AdminLogsDBHelper;

import java.util.ArrayList;
import java.util.List;

public class logs extends Fragment implements AdminLogAdapter.OnItemLongClickListener {

    private AdminLogsDBHelper db;
    private RecyclerView recyclerView;
    private AdminLogAdapter adapter;
    private List<AdminLog> logList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logs, container, false);

        db = new AdminLogsDBHelper(requireContext()); // Use requireContext() for database
        recyclerView = view.findViewById(R.id.adminlogView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        logList = new ArrayList<>();
        loadLogs();

        adapter = new AdminLogAdapter(logList);
        adapter.setOnItemLongClickListener(this); // Set long-click listener
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void loadLogs() {
        logList.clear(); // Clear previous logs
        Cursor cursor = db.getAllLogs();
        if (cursor.getCount() == 0) return;

        while (cursor.moveToNext()) {
            String username = cursor.getString(1);
            String type = cursor.getString(2);
            String organisation = cursor.getString(3);
            String type2 = cursor.getString(4);
            logList.add(new AdminLog(username, type, organisation, type2));
        }
        cursor.close();
    }

    // Handle long-press event
    @Override
    public void onItemLongClick(int position) {
        AdminLog selectedLog = logList.get(position);

        if (selectedLog.getType2().equals("confirmed")) {
            db.updateLogStatus(selectedLog.getUsername(), selectedLog.getType(), selectedLog.getOrganisation(), "cancelled");
            Toast.makeText(requireContext(), "Status changed to cancelled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Already cancelled", Toast.LENGTH_SHORT).show();
        }

        // Refresh logs after update
        loadLogs();
        adapter.notifyDataSetChanged();
    }
}