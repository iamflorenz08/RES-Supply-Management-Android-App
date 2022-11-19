package com.dreambig.supplymanagementapp.Models;

public class RequestModel {
    private Boolean isGmail;

    public RequestModel(Boolean isGmail) {
        this.isGmail = isGmail;
    }

    public Boolean getGmail() {
        return isGmail;
    }

    public void setGmail(Boolean gmail) {
        isGmail = gmail;
    }
}
