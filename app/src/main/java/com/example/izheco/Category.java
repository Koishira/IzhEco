package com.example.izheco;

public class Category {
    String category_name;
    int image;

    public Category(String category_name, int image) {
        this.category_name = category_name;
        this.image = image;
    }

    public String getCategory_name() {
        return category_name;
    }

    public int getImage() {
        return image;
    }
}
