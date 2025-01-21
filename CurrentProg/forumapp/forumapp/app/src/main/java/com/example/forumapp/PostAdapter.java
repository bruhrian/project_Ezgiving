package com.example.forumapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PostAdapter extends ArrayAdapter<Post> {

    private ReplyListener replyListener;

    public PostAdapter(Context context, ArrayList<Post> posts, ReplyListener replyListener) {
        super(context, 0, posts);
        this.replyListener = replyListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.post_item, parent, false);
        }

        Post post = getItem(position);

        TextView usernameTextView = convertView.findViewById(R.id.usernameTextView);
        TextView messageTextView = convertView.findViewById(R.id.messageTextView);
        LinearLayout repliesContainer = convertView.findViewById(R.id.repliesContainer);
        TextView replyButton = convertView.findViewById(R.id.replyButton);

        if (post != null) {
            usernameTextView.setText(post.getUsername());
            messageTextView.setText(post.getMessage());

            // Clear previous replies
            repliesContainer.removeAllViews();
            for (String reply : post.getReplies()) {
                TextView replyTextView = new TextView(getContext());
                replyTextView.setText(reply);
                repliesContainer.addView(replyTextView);
            }

            // Reply button click listener
            replyButton.setOnClickListener(v -> replyListener.onReply(post));
        }

        return convertView;
    }

    public interface ReplyListener {
        void onReply(Post post);
    }
}
