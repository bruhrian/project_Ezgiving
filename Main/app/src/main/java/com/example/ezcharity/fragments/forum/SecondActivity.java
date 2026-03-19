package com.example.ezcharity.fragments.forum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.ezcharity.R;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        findViewById(R.id.openForumButton).setOnClickListener(v -> {
            Intent intent = new Intent(SecondActivity.this, forum.class);
            startActivity(intent);
        });
    }
}