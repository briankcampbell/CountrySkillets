package com.poweredbyrita.countryskillets;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Biarre on 7/9/2015.
 */
public class Vendor implements Parcelable {
    private int id;
    private String name;
    private String phone;

    public Vendor() {
    }

    public Vendor(String name, String phone){
        this.name = name;
        this.phone = phone;
    }

    public Vendor(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;

    }

    protected Vendor(Parcel in) {
        id = in.readInt();
        name = in.readString();
        phone = in.readString();
    }

    public static final Creator<Vendor> CREATOR = new Creator<Vendor>() {
        @Override
        public Vendor createFromParcel(Parcel in) {
            return new Vendor(in);
        }

        @Override
        public Vendor[] newArray(int size) {
            return new Vendor[size];
        }
    };

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(phone);
    }
}
