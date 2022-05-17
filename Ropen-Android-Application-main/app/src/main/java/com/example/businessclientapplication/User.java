package com.example.businessclientapplication;

public class User {
    public String email;
    public boolean status;

    public User(String email, boolean status, String... values) {
        this.email = email;
        this.status = status;
    }

    public User() {
        this.email = "";
        this.status = true;
    }


    public String getEmail() {
        return email;
    }

    public boolean isStatus() {
        return status;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
