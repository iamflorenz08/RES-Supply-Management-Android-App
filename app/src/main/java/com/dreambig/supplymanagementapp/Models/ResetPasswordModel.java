package com.dreambig.supplymanagementapp.Models;

public class ResetPasswordModel {
    private String token;
    private String newPassword;

    public ResetPasswordModel(String token, String newPassword) {
        this.token = token;
        this.newPassword = newPassword;
    }

    public ResetPasswordModel() {

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
