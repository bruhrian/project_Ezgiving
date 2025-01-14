package com.example.kawkaw;

public class User {
    private final String name, type;
    private final byte[] image;

    public User(String name, String type, byte[] image) {
        this.name = name;
        this.type = type;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public byte[] getImage() {
        return image;
    }
}
