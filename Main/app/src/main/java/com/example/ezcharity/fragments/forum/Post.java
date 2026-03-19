package com.example.ezcharity.fragments.forum;

import java.util.ArrayList;

public class Post {
    private String username;
    private String message;
    private ArrayList<String> replies;

    public Post(String username, String message) {
        this.username = username;
        this.message = message;
        this.replies = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<String> getReplies() {
        return replies;
    }

    public void addReply(String reply) {
        replies.add(reply);
    }
}
