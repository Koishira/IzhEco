package com.example.izheco.models;

import java.io.Serializable;

public class Place implements Serializable {
    private int id;
    private int categoryId;
    private String subcategoryType;
    private String name;
    private String logoName;
    private String extraInfo;
    private String phone;
    private String website;
    private String vk;

    // Конструкторы
    public Place() {}

    public Place(int categoryId, String subcategoryType, String name, String logoName,
                 String extraInfo, String phone, String website, String vk) {
        this.categoryId = categoryId;
        this.subcategoryType = subcategoryType;
        this.name = name;
        this.logoName = logoName;
        this.extraInfo = extraInfo;
        this.phone = phone;
        this.website = website;
        this.vk = vk;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getSubcategoryType() {
        return subcategoryType;
    }

    public void setSubcategoryType(String subcategoryType) {
        this.subcategoryType = subcategoryType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoName() {
        return logoName;
    }

    public void setLogoName(String logoName) {
        this.logoName = logoName;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getVk() {
        return vk;
    }

    public void setVk(String vk) {
        this.vk = vk;
    }

    @Override
    public String toString() {
        return name;
    }
}