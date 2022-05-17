package com.example.businessclientapplication;

public class Client extends User{
    public int phoneNumber;
    public String fullName;

    public Client(String email, int phoneNumber, String fullName) {
        super(email, true);
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
