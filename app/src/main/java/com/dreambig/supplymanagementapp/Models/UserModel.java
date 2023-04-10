package com.dreambig.supplymanagementapp.Models;

public class UserModel {
    private String _id;
    private String id_no;
    private String email;
    private PasswordModel password;
    private String photo_URL;
    private FullNameModel full_name;
    private String mobile_number;
    private String department;
    private String position;
    private Boolean isApproved;

    public UserModel(){

    }

    public UserModel(String id_no,String email, PasswordModel password, String photo_URL, FullNameModel full_name, String mobile_number, String department, String position) {
        this.id_no = id_no;
        this.email = email;
        this.password = password;
        this.photo_URL = photo_URL;
        this.full_name = full_name;
        this.mobile_number = mobile_number;
        this.department = department;
        this.position = position;
    }

    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    public String getId_no() {
        return id_no;
    }

    public void setId_no(String id_no) {
        this.id_no = id_no;
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

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
