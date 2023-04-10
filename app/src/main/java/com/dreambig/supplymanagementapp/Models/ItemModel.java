package com.dreambig.supplymanagementapp.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "item_tbl")
public class ItemModel {
    @PrimaryKey(autoGenerate = true)
    private Integer id_no;
    private String user_id;
    private String product_code;
    private String item_type;
    private String item_name;
    private String item_image;
    private Integer quantity;
    private String unit_measurement;
    private Double total_cost;
    private String notes;

    public ItemModel(){}
    public ItemModel(String product_code, String item_type, String item_name, String item_image, Integer quantity, String unit_measurement, Double total_cost, String notes) {
        this.product_code = product_code;
        this.item_type = item_type;
        this.item_name = item_name;
        this.item_image = item_image;
        this.quantity = quantity;
        this.unit_measurement = unit_measurement;
        this.total_cost = total_cost;
        this.notes = notes;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public Integer getId_no() {
        return id_no;
    }

    public void setId_no(Integer id_no) {
        this.id_no = id_no;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUnit_measurement() {
        return unit_measurement;
    }

    public void setUnit_measurement(String unit_measurement) {
        this.unit_measurement = unit_measurement;
    }

    public Double getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(Double total_cost) {
        this.total_cost = total_cost;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
