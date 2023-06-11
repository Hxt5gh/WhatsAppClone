package com.example.android.whatsapp.models;

public class status
{
    private String imageUri;
    private long timeStam;

    public status(String imageUri, long timeStam) {
        this.imageUri = imageUri;
        this.timeStam = timeStam;
    }

    public status() {
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public long getTimeStam() {
        return timeStam;
    }

    public void setTimeStam(long timeStam) {
        this.timeStam = timeStam;
    }
}
