package com.example.mybookingswadminlogs.myBookings;

public class BookingItem {
    private int id; // Unique ID
    private String type;
    private String organization;

    public BookingItem(int id, String type, String organization) {
        this.id = id;
        this.type = type;
        this.organization = organization;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getOrganization() {
        return organization;
    }
}

