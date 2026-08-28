package com.example.izheco.models;

public class Category {
    private int id;
    private String name;
    private String imageName;
    private int hasGive;
    private int hasSell;
    private int hasExchange;

    // Конструкторы
    public Category() {}

    public Category(String name, String imageName, int hasGive, int hasSell, int hasExchange) {
        this.name = name;
        this.imageName = imageName;
        this.hasGive = hasGive;
        this.hasSell = hasSell;
        this.hasExchange = hasExchange;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getHasGive() {
        return hasGive;
    }

    public void setHasGive(int hasGive) {
        this.hasGive = hasGive;
    }

    public int getHasSell() {
        return hasSell;
    }

    public void setHasSell(int hasSell) {
        this.hasSell = hasSell;
    }

    public int getHasExchange() {
        return hasExchange;
    }

    public void setHasExchange(int hasExchange) {
        this.hasExchange = hasExchange;
    }

    @Override
    public String toString() {
        return name;
    }
}