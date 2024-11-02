package com.example.drawertest3.ui.adminlogs;

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
import com.example.drawertest3.databinding.FragmentAdminLogsBinding;
import com.example.drawertest3.databinding.FragmentMyaccountBinding;
import com.example.drawertest3.ui.myaccount.MyAccountViewModel;

public class AdminLogsFragment extends Fragment {

    private FragmentAdminLogsBinding fragmentAdminLogsBinding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AdminLogsViewModel adminLogsViewModel =
                new ViewModelProvider(this).get(AdminLogsViewModel.class);

        fragmentAdminLogsBinding = FragmentAdminLogsBinding.inflate(inflater, container, false);
        View root = fragmentAdminLogsBinding.getRoot();

        final TextView textView = fragmentAdminLogsBinding.textAdminlogs;
        adminLogsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentAdminLogsBinding = null;
    }

}