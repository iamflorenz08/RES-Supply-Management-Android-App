package com.dreambig.supplymanagementapp.Models;

import androidx.room.Entity;

import java.util.ArrayList;
import java.util.List;


public class RequisitionModel {
    private String user_id;
    private String status;
    private List<ItemModel> items;
    private String createdAt;
    private String updatedAt;

    public RequisitionModel(String user_id, String status, List<ItemModel> items) {
        this.user_id = user_id;
        this.status = status;
        this.items = items;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ItemModel> getItems() {
        return items;
    }

    public void setItems(List<ItemModel> items) {
        this.items = items;
    }
}
