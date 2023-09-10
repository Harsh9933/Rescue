package com.example.myapplication;

public class Users {
    String name , email , address , noOfMem , expertise;

    public Users(){

    }

    public Users(String name, String email, String address, String noOfMem, String expertise) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.noOfMem = noOfMem;
        this.expertise = expertise;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNoOfMem() {
        return noOfMem;
    }

    public void setNoOfMem(String noOfMem) {
        this.noOfMem = noOfMem;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }
}
