package com.poweredbyrita.countryskillets;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Biarre on 7/14/2015.
 */
public class RestaurantLocation implements Parcelable {
    private int id;
    private String name;
    private int imageId;

    public RestaurantLocation() {
    }

    public RestaurantLocation(int id, String name) {
        this.id = id;
        this.name = name;

    }

    public RestaurantLocation(int id, String name, int imageId) {
        this.id = id;
        this.name = name;
        this.imageId = imageId;

    }

    public RestaurantLocation(String name) {
        this.name = name;

    }

    public RestaurantLocation(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(imageId);
    }

    public RestaurantLocation(Parcel input){
        this.id = input.readInt();
        this.name = input.readString();
        this.imageId = input.readInt();
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>(){

        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
