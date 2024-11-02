package com.example.drawertest3.ui.myaccount;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

//import com.example.drawertest3.databinding.FragmentGalleryBinding;
import com.example.drawertest3.databinding.FragmentMyaccountBinding;

public class MyAccountFragment extends Fragment {

    private FragmentMyaccountBinding binding; //copy from import

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MyAccountViewModel myAccountViewModel =
                new ViewModelProvider(this).get(MyAccountViewModel.class);

        binding = FragmentMyaccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textMyaccount;
        myAccountViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}