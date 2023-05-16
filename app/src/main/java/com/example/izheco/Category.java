package com.example.izheco;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Category implements Parcelable {
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

    protected Category(Parcel in) {
        category_name = in.readString();
        image = in.readInt();
        give = in.readInt();
        sell = in.readInt();
        exchange = in.readInt();
        expanded = in.readByte() != 0;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(category_name);
        parcel.writeInt(image);
        parcel.writeInt(give);
        parcel.writeInt(sell);
        parcel.writeInt(exchange);
        parcel.writeByte((byte) (expanded ? 1 : 0));
    }
}
