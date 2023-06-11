package com.example.android.whatsapp.models;

import java.util.ArrayList;

public class userStatus {
    private String name;
    private String profileImg;

    public userStatus(String name, String profileImg, String senderUid, long lastUpdated, ArrayList<status> statusList) {
        this.name = name;
        this.profileImg = profileImg;
        this.senderUid = senderUid;
        this.lastUpdated = lastUpdated;
        this.statusList = statusList;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    private String senderUid ;
    private long lastUpdated;
    private ArrayList<status> statusList;


    public userStatus(String name, String profileImg, long lastUpdated, ArrayList<status> statusList) {
        this.name = name;
        this.profileImg = profileImg;
        this.lastUpdated = lastUpdated;
        this.statusList = statusList;
    }

    public userStatus() {
    }

    public ArrayList<status> getStatusList() {
        return statusList;
    }

    public void setStatusList(ArrayList<status> statusList) {
        this.statusList = statusList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
