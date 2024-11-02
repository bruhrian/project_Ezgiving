package com.example.drawertest3.ui.addbkordono;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.drawertest3.databinding.FragmentAddbookingordonationBinding;

public class AddBkorDonoFragment extends Fragment {

    private FragmentAddbookingordonationBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddBkorDonoViewModel slideshowViewModel =
                new ViewModelProvider(this).get(AddBkorDonoViewModel.class);

        binding = FragmentAddbookingordonationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}