package com.example.youthconnectproject;

public class Users {
    private String userID;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private String userAdmin;

    public Users() {}

    public Users(String userID, String userFirstName, String userLastName, String userEmail, String userAdmin) {
        this.userID = userID;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.userAdmin = userAdmin;
    }

    public String getUserId() {
        return userID;
    }
    public void setUserId(String userID) {
        this.userID = userID;
    }
    public String getUserFirstName() {
        return userFirstName;
    }
    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }
    public String getUserLastName() {
        return userLastName;
    }
    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }
    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public String getUserAdmin() {
        return userAdmin;
    }
    public void setUserAdmin(String userAdmin) {
        this.userAdmin = userAdmin;
    }
}
