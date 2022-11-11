package com.dreambig.supplymanagementapp.Models;

public class PasswordModel {
    private String password;
    private Boolean isGmail;

    public PasswordModel(String password, Boolean isGmail) {
        this.password = password;
        this.isGmail = isGmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getGmail() {
        return isGmail;
    }

    public void setGmail(Boolean gmail) {
        isGmail = gmail;
    }
}
