package com.example.tlucontact.models;

public class Unit extends Contact {
    private String address;

    public Unit(int id,String name, String phone, Integer avatar, String address) {
        super(id,name, phone, avatar);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

