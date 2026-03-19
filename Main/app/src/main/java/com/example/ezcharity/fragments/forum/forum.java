package com.example.ezcharity.fragments.forum;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ezcharity.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class forum extends Fragment {

    private ListView listView;
    private ArrayList<Post> forumPosts;
    private PostAdapter adapter;
    private SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "ForumApp";
    private static final String POSTS_KEY = "forum_posts";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);

        listView = view.findViewById(R.id.listView);

        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Load saved posts
        forumPosts = loadPosts();
        if (forumPosts == null) {
            forumPosts = new ArrayList<>();
        }

        adapter = new PostAdapter(requireContext(), forumPosts);
        listView.setAdapter(adapter);

        FloatingActionButton addButton = view.findViewById(R.id.addButton);
        FloatingActionButton backButton = view.findViewById(R.id.backButton);

        // Add new post
        addButton.setOnClickListener(v -> showAddPostDialog());

        // Navigate back
        backButton.setOnClickListener(v -> {
            // Handle navigation logic here
            requireActivity().onBackPressed();
        });

        return view;
    }

    private void showAddPostDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add New Post");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_post, null);
        builder.setView(dialogView);

        final EditText postInput = dialogView.findViewById(R.id.postInput);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String email = getLoggedInUserEmail(); // Retrieve the email of the logged-in user
            String postContent = postInput.getText().toString().trim();
            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(postContent)) {
                forumPosts.add(new Post(email, postContent));
                savePosts();
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(requireContext(), "Post content cannot be empty!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private void savePosts() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(forumPosts);
        editor.putString(POSTS_KEY, json);
        editor.apply();
    }

    private ArrayList<Post> loadPosts() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(POSTS_KEY, null);
        Type type = new TypeToken<ArrayList<Post>>() {}.getType();
        return gson.fromJson(json, type);
    }

    private String getLoggedInUserEmail() {
        SharedPreferences userPrefs = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return userPrefs.getString("user_email", "Anonymous"); // Default to "Anonymous" if no email is found
    }

    static class Post {
        private String username;
        private String content;
        private ArrayList<String> replies;

        public Post(String username, String content) {
            this.username = username;
            this.content = content;
            this.replies = new ArrayList<>();
        }

        public void addReply(String username, String reply) {
            replies.add(username + ": " + reply);
        }

        public String getUsername() {
            return username;
        }

        public String getContent() {
            return content;
        }

        public String getReplies() {
            if (replies.isEmpty()) {
                return "No replies yet.";
            }
            StringBuilder replyBuilder = new StringBuilder();
            for (String reply : replies) {
                replyBuilder.append(reply).append("\n");
            }
            return replyBuilder.toString().trim();
        }
    }

    class PostAdapter extends ArrayAdapter<Post> {
        public PostAdapter(@NonNull Context context, @NonNull ArrayList<Post> posts) {
            super(context, 0, posts);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_post, parent, false);
            }

            Post post = getItem(position);

            TextView usernameView = convertView.findViewById(R.id.textUsername);
            TextView contentView = convertView.findViewById(R.id.textPostContent);
            TextView repliesView = convertView.findViewById(R.id.textReplies);
            ImageButton deleteButton = convertView.findViewById(R.id.deleteButton);

            usernameView.setText(post.getUsername());
            contentView.setText(post.getContent());
            repliesView.setText(post.getReplies());

            deleteButton.setOnClickListener(v -> {
                forumPosts.remove(position);
                savePosts();
                notifyDataSetChanged();
            });

            repliesView.setOnClickListener(v -> showReplyDialog(position));

            return convertView;
        }

        private void showReplyDialog(int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Add Reply");

            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_add_post, null);
            builder.setView(dialogView);

            final EditText replyInput = dialogView.findViewById(R.id.postInput);

            builder.setPositiveButton("Reply", (dialog, which) -> {
                String email = getLoggedInUserEmail(); // Retrieve the email of the logged-in user
                String replyContent = replyInput.getText().toString().trim();
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(replyContent)) {
                    forumPosts.get(position).addReply(email, replyContent);
                    savePosts();
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(requireContext(), "Reply content cannot be empty!", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("Cancel", null);
            builder.create().show();
        }
    }
}
