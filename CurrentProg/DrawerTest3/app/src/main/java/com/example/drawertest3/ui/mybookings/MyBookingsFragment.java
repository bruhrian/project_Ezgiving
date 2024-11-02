package com.example.drawertest3.ui.mybookings;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.drawertest3.R;
import com.example.drawertest3.databinding.FragmentMyBookingsBinding;


public class MyBookingsFragment extends Fragment {

    private FragmentMyBookingsBinding fragmentMyBookingsBinding;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        MyBookingsViewModel myBookingsViewModel =
                new ViewModelProvider(this).get(MyBookingsViewModel.class);

        fragmentMyBookingsBinding = FragmentMyBookingsBinding.inflate(inflater, container, false);
        View root = fragmentMyBookingsBinding.getRoot();

        final TextView textView = fragmentMyBookingsBinding.textMybookings;
        myBookingsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentMyBookingsBinding = null;
    }
}