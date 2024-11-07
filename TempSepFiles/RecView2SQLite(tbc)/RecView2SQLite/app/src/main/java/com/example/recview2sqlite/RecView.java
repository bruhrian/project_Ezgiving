package com.example.recview2sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class RecView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_view);

        String name = getIntent().getStringExtra("NAME");
        String taskactivity = getIntent().getStringExtra("TASKACTIVITY");
        int image = getIntent().getIntExtra("IMAGE", 0);
        String desc = getIntent().getStringExtra("DESC");

        TextView orgnameTextView = findViewById(R.id.organisationName);
        TextView taskactivityTextView = findViewById(R.id.orgTaskAct);
        TextView descriptionTextView = findViewById(R.id.organisationDesc);
        TextView telephoneTextView = findViewById(R.id.organisationTel)
        ImageView orgImage = findViewById(R.id.organisationImage);

        orgnameTextView.setText(name);
        taskactivityTextView.setText(taskactivity);
        orgImage.setImageResource(image);
        descriptionTextView.setText(desc);
        telephoneTextView.setText(telephone);

    }
}