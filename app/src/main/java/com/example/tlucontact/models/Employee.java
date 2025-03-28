package com.example.tlucontact.models;

public class Employee extends Contact{
    private String email;
    private String unit;
    private String position;

    public Employee(int id,String name, String phone, Integer avatar, String email, String unit, String position) {
        super(id,name, phone, avatar);
        this.email = email;
        this.unit = unit;
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public String getUnit() {
        return unit;
    }

    public String getPosition() {
        return position;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
