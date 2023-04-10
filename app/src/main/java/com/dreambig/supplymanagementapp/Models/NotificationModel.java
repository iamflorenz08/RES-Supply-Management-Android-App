package com.dreambig.supplymanagementapp.Models;

public class NotificationModel {
    private String _id;
    private RequisitionModel request;
    private String approval;
    private Boolean isRead;
    private String createdAt;

    public NotificationModel(String id, RequisitionModel request, String approval, Boolean isRead, String createdAt) {
        _id = id;
        this.request = request;
        this.approval = approval;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public RequisitionModel getRequest() {
        return request;
    }

    public void setRequest(RequisitionModel request) {
        this.request = request;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
