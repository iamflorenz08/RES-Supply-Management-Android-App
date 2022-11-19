package com.dreambig.supplymanagementapp.Models;

public class ResponseModel {
    private String message;
    private Boolean isError;

    public ResponseModel(String message, Boolean isError) {
        this.message = message;
        this.isError = isError;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getError() {
        return isError;
    }

    public void setError(Boolean error) {
        isError = error;
    }
}
