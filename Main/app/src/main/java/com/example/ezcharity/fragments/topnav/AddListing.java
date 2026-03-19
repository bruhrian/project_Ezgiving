package com.example.ezcharity.fragments.topnav;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.ezcharity.R;
import com.example.ezcharity.fragments.home.DBRV.DBmain;
import com.example.ezcharity.fragments.home.DBRV.User;
import com.example.ezcharity.fragments.home.DBRV.UserAdapter;
import com.example.ezcharity.fragments.home.HomeFragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class AddListing extends Fragment {

    private EditText nameEditText, taskEditText, locationEditText;
    private ImageView userImageView;
    private Button addButton;
    private RadioGroup typeRadioGroup;
    private static final int PICK_IMAGE_REQUEST = 101;

    private DBmain databaseHelper;
    private String selectedType = "Donation";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_listing, container, false);
        nameEditText = view.findViewById(R.id.edit_orgname);
        taskEditText = view.findViewById(R.id.edit_taskact);
        locationEditText = view.findViewById(R.id.edit_location);
        userImageView = view.findViewById(R.id.avatar);
        addButton = view.findViewById(R.id.btn_add);
        typeRadioGroup = view.findViewById(R.id.TypeCharity);

        databaseHelper = new DBmain(requireContext());

        typeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.btn_dono) {
                selectedType = "Donation";
            } else if (checkedId == R.id.btn_book) {
                selectedType = "Bookings";
            }
        });

        userImageView.setOnClickListener(v -> selectImageFromGallery());

        addButton.setOnClickListener(v -> {
            saveDataToDatabase();
            // After saving data, show updated listings in the HomeFragment
            displayUpdatedList();
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), data.getData());
                userImageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveDataToDatabase() {
        String name = nameEditText.getText().toString().trim();
        String task = taskEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();
        BitmapDrawable drawable = (BitmapDrawable) userImageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageBytes = stream.toByteArray();

        boolean isInserted = databaseHelper.insertData(name, imageBytes, selectedType, task, location);
        if (isInserted) {
            Toast.makeText(requireContext(), "Data saved successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Failed to save data", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayUpdatedList() {
        // Switch back to HomeFragment and pass the updated list
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new HomeFragment());
        transaction.addToBackStack(null); // Add to backstack to enable back navigation
        transaction.commit();
    }
}
