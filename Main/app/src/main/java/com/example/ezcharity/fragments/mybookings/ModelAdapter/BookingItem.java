package com.example.ezcharity.fragments.mybookings.ModelAdapter;

public class BookingItem {
    private int id; // Unique ID
    private String type;
    private String organization;

    private String type2;

    public BookingItem(int id, String type, String organization, String type2) {
        this.id = id;
        this.type = type;
        this.organization = organization;
        this.type2 = type2;
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
    public String getType2(){return type2;}

    public void setType2(String cancelled) {
    }

}
