package com.example.ezcharity.fragments.home.DBRV;

public class User {
    private final int id;
    private final String name, type, task, location;
    private final byte[] image;

    public User(int id, String name, String type, String task, String location, byte[] image) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.task = task;
        this.location = location;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getTask() {
        return task;
    }

    public String getLocation() {
        return location;
    }

    public byte[] getImage() {
        return image;
    }
}

