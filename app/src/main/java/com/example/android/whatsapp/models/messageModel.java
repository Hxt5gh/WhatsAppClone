package com.example.android.whatsapp.models;

public class messageModel {

    String uId;
    String message;
    long lastMessage;
    String nName;
    String recvierName;
    String imageUri;

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getRecvierName() {
        return recvierName;
    }

    public void setRecvierName(String recvierName) {
        this.recvierName = recvierName;
    }


    public String getnName() {
        return nName;
    }

    public void setnName(String nName) {
        this.nName = nName;
    }

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
