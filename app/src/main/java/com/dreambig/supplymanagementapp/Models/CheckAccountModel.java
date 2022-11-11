package com.dreambig.supplymanagementapp.Models;

public class CheckAccountModel {
    private Boolean isExist;
    private Boolean isGmail;

    public CheckAccountModel(Boolean isExist, Boolean isGmail) {
        this.isExist = isExist;
        this.isGmail = isGmail;
    }

    public Boolean getExist() {
        return isExist;
    }

    public void setExist(Boolean exist) {
        isExist = exist;
    }

    public Boolean getGmail() {
        return isGmail;
    }

    public void setGmail(Boolean gmail) {
        isGmail = gmail;
    }
}
