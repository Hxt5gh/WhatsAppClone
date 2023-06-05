package com.example.android.whatsapp.models;

public class UsersClass {

    String profilepic;
    String userName;
    String uId;
    String password;
    String mail;
    String lastMsg;

    public UsersClass() {
    }



    public UsersClass(String profilepic , String userName, String uId, String password, String mail, String lastMsg) {
        this.profilepic = profilepic;
        this.userName = userName;
        this.uId = uId;
        this.password = password;
        this.mail = mail;
        this.lastMsg = lastMsg;
    }

    public UsersClass(String userName, String mail , String password) {
        this.userName = userName;
        this.password = password;
        this.mail = mail;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }
}
