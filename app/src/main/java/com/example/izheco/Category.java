package com.example.izheco;

import androidx.constraintlayout.widget.ConstraintLayout;

public class Category {
    private String category_name;
    private int image;

    private int give, sell, exchange;

    public boolean expanded;

    public Category(String category_name, int image, int give, int sell, int exchange) {
        this.category_name = category_name;
        this.image = image;
        this.give = give;
        this.sell = sell;
        this.exchange = exchange;
        this.expanded = false;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public int getGive() {
        return give;
    }

    public int getSell() {
        return sell;
    }

    public int getExchange() {
        return exchange;
    }

    public String getCategory_name() {
        return category_name;
    }

    public int getImage() {
        return image;
    }
}
