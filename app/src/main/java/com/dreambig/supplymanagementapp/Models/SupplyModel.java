package com.dreambig.supplymanagementapp.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


public class SupplyModel {

    private String _id;
    private String product_code;
    private String item_type;
    private String photo_url;
    private String item_name;
    private String storage_no;
    private String category;
    private Integer current_supply;
    private String unit_measurement;
    private String source_of_fund;
    private Double unit_cost;
    private String desc;

    public SupplyModel(String _id, String product_code, String item_type, String photo_url, String item_name, String storage_no, String category, Integer current_supply, String unit_measurement, String source_of_fund, Double unit_cost, String desc) {
        this._id = _id;
        this.product_code = product_code;
        this.item_type = item_type;
        this.photo_url = photo_url;
        this.item_name = item_name;
        this.storage_no = storage_no;
        this.category = category;
        this.current_supply = current_supply;
        this.unit_measurement = unit_measurement;
        this.source_of_fund = source_of_fund;
        this.unit_cost = unit_cost;
        this.desc = desc;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getStorage_no() {
        return storage_no;
    }

    public void setStorage_no(String storage_no) {
        this.storage_no = storage_no;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getCurrent_supply() {
        return current_supply;
    }

    public void setCurrent_supply(Integer current_supply) {
        this.current_supply = current_supply;
    }

    public String getUnit_measurement() {
        return unit_measurement;
    }

    public void setUnit_measurement(String unit_measurement) {
        this.unit_measurement = unit_measurement;
    }

    public String getSource_of_fund() {
        return source_of_fund;
    }

    public void setSource_of_fund(String source_of_fund) {
        this.source_of_fund = source_of_fund;
    }

    public Double getUnit_cost() {
        return unit_cost;
    }

    public void setUnit_cost(Double unit_cost) {
        this.unit_cost = unit_cost;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
