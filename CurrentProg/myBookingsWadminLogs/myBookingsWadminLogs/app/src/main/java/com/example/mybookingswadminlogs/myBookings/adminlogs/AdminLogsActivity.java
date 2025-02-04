package com.example.mybookingswadminlogs.myBookings.adminlogs;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import com.example.mybookingswadminlogs.R;

public class AdminLogsActivity extends AppCompatActivity implements AdminLogAdapter.OnItemLongClickListener {

    private AdminLogsDBHelper db;
    private RecyclerView recyclerView;
    private AdminLogAdapter adapter;
    private List<AdminLog> logList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_logs);

        db = new AdminLogsDBHelper(this);
        recyclerView = findViewById(R.id.adminlogView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        logList = new ArrayList<>();
        loadLogs();

        adapter = new AdminLogAdapter(logList);
        adapter.setOnItemLongClickListener(this); // Set long-click listener
        recyclerView.setAdapter(adapter);
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
            Toast.makeText(this, "Status changed to cancelled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Already cancelled", Toast.LENGTH_SHORT).show();
        }

        // Refresh logs after update
        loadLogs();
        adapter.notifyDataSetChanged();
    }
}
