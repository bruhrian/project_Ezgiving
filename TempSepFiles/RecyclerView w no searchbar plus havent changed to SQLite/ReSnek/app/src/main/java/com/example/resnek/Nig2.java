package com.example.resnek;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Nig2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nig2);

        String name = getIntent().getStringExtra("NAME");
        String taskactivity = getIntent().getStringExtra("TASKACTIVITY");
        int image = getIntent().getIntExtra("IMAGE", 0);
        String desc = getIntent().getStringExtra("DESC");

        TextView orgnameTextView = findViewById(R.id.orgName_Title);
        TextView taskactivityTextView = findViewById(R.id.orgTaskActivity);
        TextView descriptionTextView = findViewById(R.id.orgDesc);
        ImageView orgImage = findViewById(R.id.orgImage);

        orgnameTextView.setText(name);
        taskactivityTextView.setText(taskactivity);
        orgImage.setImageResource(image);
        descriptionTextView.setText(desc);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}