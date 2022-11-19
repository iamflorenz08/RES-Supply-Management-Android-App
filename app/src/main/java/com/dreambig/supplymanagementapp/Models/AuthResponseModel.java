package com.dreambig.supplymanagementapp.Models;

public class AuthResponseModel {
    private Boolean isExist;
    private Boolean isGmail;

    public AuthResponseModel(Boolean isExist, Boolean isGmail) {
        this.isExist = isExist;
        this.isGmail = isGmail;
    }

    public Boolean getExist() {
        return isExist;
    }

    public void setExist(Boolean exist) {
        isExist = exist;
    }

    public Boolean getisGmail() {
        return isGmail;
    }

    public void setisGmail(Boolean gmail) {
        isGmail = gmail;
    }
}
