package com.dreambig.supplymanagementapp.Models;

import com.google.gson.annotations.SerializedName;

public class SavedItemModel {
    private String _id;
    private String user;
    private SupplyModel item;

    public SavedItemModel(String user, SupplyModel item) {
        this.user = user;
        this.item = item;
    }

    public SavedItemModel(String _id, String user, SupplyModel item) {
        this._id = _id;
        this.user = user;
        this.item = item;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public SupplyModel getItem() {
        return item;
    }

    public void setItem(SupplyModel item) {
        this.item = item;
    }
}
