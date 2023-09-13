package com.example.myapplication;

public class DataClass {
    private String name;
    private String address;
    private String email;
    private String expertise;
    private String noOfMem;
    private String profileImageURL;

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getExpertise() {
        return expertise;
    }

    public String getNoOfMem() {
        return noOfMem;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }


    public DataClass(String name, String address, String email, String expertise, String noOfMem, String profileImageURL) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.expertise = expertise;
        this.noOfMem = noOfMem;
        this.profileImageURL = profileImageURL;
    }

    public DataClass(){

    }
}