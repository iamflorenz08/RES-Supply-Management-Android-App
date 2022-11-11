package com.dreambig.supplymanagementapp.Models;

public class UserModel {
    private String email;
    private PasswordModel password;
    private String photo_URL;
    private FullNameModel full_name;
    private String mobile_number;
    private String department;
    private String position;

    public UserModel(String email, PasswordModel password, String photo_URL, FullNameModel full_name, String mobile_number, String department, String position) {
        this.email = email;
        this.password = password;
        this.photo_URL = photo_URL;
        this.full_name = full_name;
        this.mobile_number = mobile_number;
        this.department = department;
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PasswordModel getPassword() {
        return password;
    }

    public void setPassword(PasswordModel password) {
        this.password = password;
    }

    public String getPhoto_URL() {
        return photo_URL;
    }

    public void setPhoto_URL(String photo_URL) {
        this.photo_URL = photo_URL;
    }

    public FullNameModel getFull_name() {
        return full_name;
    }

    public void setFull_name(FullNameModel full_name) {
        this.full_name = full_name;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
