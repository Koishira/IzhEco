package com.example.izheco;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Place implements Parcelable {
    private String place_name;
    private int image;

    public Place(String place_name, int image) {
        this.place_name = place_name;
        this.image = image;
    }

    protected Place(Parcel in) {
        place_name = in.readString();
        image = in.readInt();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    public String getPlace_name() {
        return place_name;
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
        parcel.writeString(place_name);
        parcel.writeInt(image);
    }
}
