package com.example.drawertest3.ui.forum;

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
import com.example.drawertest3.databinding.FragmentForumBinding;
import com.example.drawertest3.databinding.FragmentMyaccountBinding;
import com.example.drawertest3.ui.myaccount.MyAccountViewModel;

public class ForumFragment extends Fragment {

    private FragmentForumBinding fragmentForumBinding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ForumViewModel forumViewModel =
                new ViewModelProvider(this).get(ForumViewModel.class);

        fragmentForumBinding = FragmentForumBinding.inflate(inflater, container, false);
        View root = fragmentForumBinding.getRoot();

        final TextView textView = fragmentForumBinding.textForum;
        forumViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentForumBinding = null;
    }

}