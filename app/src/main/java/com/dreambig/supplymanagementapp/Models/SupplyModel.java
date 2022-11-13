package com.dreambig.supplymanagementapp.Models;

public class SupplyModel {
    private String serial_number;
    private String item_name;
    private String item_image;
    private String item_desc;
    private String category;
    private Integer max_quantity;
    private Integer available;
    private String location;
    private Boolean isReturnable;

    public SupplyModel(String serial_number, String item_name, String item_image, String item_desc, String category, Integer max_quantity, Integer available, String location, Boolean isReturnable) {
        this.serial_number = serial_number;
        this.item_name = item_name;
        this.item_image = item_image;
        this.item_desc = item_desc;
        this.category = category;
        this.max_quantity = max_quantity;
        this.available = available;
        this.location = location;
        this.isReturnable = isReturnable;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getMax_quantity() {
        return max_quantity;
    }

    public void setMax_quantity(Integer max_quantity) {
        this.max_quantity = max_quantity;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getReturnable() {
        return isReturnable;
    }

    public void setReturnable(Boolean returnable) {
        isReturnable = returnable;
    }
}
