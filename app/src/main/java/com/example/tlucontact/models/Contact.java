package com.example.tlucontact.models;

import java.io.Serializable;

public class Contact implements Serializable {
    private int id;
    private String name;
    private String phone;
    private Integer avatar;

    public Contact(int id,String name, String phone, Integer avatar) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAvatar(Integer avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getAvatar() {
        return avatar;
    }
}
