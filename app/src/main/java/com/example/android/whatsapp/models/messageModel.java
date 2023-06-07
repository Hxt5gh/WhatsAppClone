package com.example.android.whatsapp.models;

public class messageModel {

    String uId;
    String message;
    long lastMessage;

    public messageModel(String uId , String message) {
        this.message = message;
        this.uId = uId;

    }

    public messageModel(String uId, String message, long lastMessage) {
        this.uId = uId;
        this.message = message;
        this.lastMessage = lastMessage;
    }

    public messageModel() {
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(long lastMessage) {
        this.lastMessage = lastMessage;
    }
}
