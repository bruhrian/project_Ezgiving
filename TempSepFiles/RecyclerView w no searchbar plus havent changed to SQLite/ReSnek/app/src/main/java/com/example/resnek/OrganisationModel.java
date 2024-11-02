package com.example.resnek;

public class OrganisationModel {
    String orgName;
    String orgTaskActivity;
    int image;
    String desc;


    public OrganisationModel(String orgName, String orgTaskActivity, int image, String desc) {
        this.orgName = orgName;
        this.orgTaskActivity = orgTaskActivity;
        this.image = image;
        this.desc = desc;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getOrgTaskActivity() {
        return orgTaskActivity;
    }

    public int getImage() {
        return image;
    }
    public String getDesc() { return desc; }
}
