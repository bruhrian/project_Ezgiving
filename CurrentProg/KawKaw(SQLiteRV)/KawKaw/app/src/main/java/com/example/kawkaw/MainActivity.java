package com.example.kawkaw;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kawkaw.DBmain;
import com.example.kawkaw.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 101;
    private EditText nameEditText;
    private ImageView userImageView;
    private Button submitButton, displayButton;
    private RecyclerView recyclerView;
    private RadioGroup typeRadioGroup;

    private DBmain databaseHelper;
    private String selectedType = "Donation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEditText = findViewById(R.id.edit_name);
        userImageView = findViewById(R.id.avatar);
        submitButton = findViewById(R.id.btn_submit);
        displayButton = findViewById(R.id.btn_display);
        recyclerView = findViewById(R.id.RecyclerList);
        typeRadioGroup = findViewById(R.id.TypeCharity);

        databaseHelper = new DBmain(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        typeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.btn_dono) {
                selectedType = "Donation";
            } else if (checkedId == R.id.btn_book) {
                selectedType = "Bookings";
            }
        });

        userImageView.setOnClickListener(v -> selectImageFromGallery());

        submitButton.setOnClickListener(v -> saveDataToDatabase());

        displayButton.setOnClickListener(v -> displayData());
    }

    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                userImageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveDataToDatabase() {
        String name = nameEditText.getText().toString().trim();
        BitmapDrawable drawable = (BitmapDrawable) userImageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageBytes = stream.toByteArray();

        boolean isInserted = databaseHelper.insertData(name, imageBytes, selectedType);
        if (isInserted) {
            Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayData() {
        Cursor cursor = databaseHelper.getAllData();
        ArrayList<User> userList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("Name"));
                String type = cursor.getString(cursor.getColumnIndex("Type"));
                byte[] image = cursor.getBlob(cursor.getColumnIndex("Image"));

                userList.add(new User(name, type, image));
            } while (cursor.moveToNext());
        }

        UserAdapter adapter = new UserAdapter(this, userList);
        recyclerView.setAdapter(adapter);
    }
}
