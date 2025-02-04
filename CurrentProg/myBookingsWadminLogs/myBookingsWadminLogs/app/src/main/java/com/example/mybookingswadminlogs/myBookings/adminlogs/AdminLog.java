package com.example.mybookingswadminlogs.myBookings.adminlogs;

public class AdminLog {
    private String username;
    private String type;
    private String organisation;
    private String type2; // New field for the status (confirmed/cancelled)

    public AdminLog(String username, String type, String organisation, String type2) {
        this.username = username;
        this.type = type;
        this.organisation = organisation;
        this.type2 = type2; // Assign the status value
    }

    public String getUsername() {
        return username;
    }

    public String getType() {
        return type;
    }

    public String getOrganisation() {
        return organisation;
    }

    public String getType2() {
        return type2; // New getter for the status
    }
}
