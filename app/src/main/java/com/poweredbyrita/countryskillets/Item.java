package com.poweredbyrita.countryskillets;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Biarre on 7/9/2015.
 */


public class Item implements Parcelable {
    private int id;
    private String name;
    private Group group;
    private int count;
    private Vendor vendor;
    private ArrayList<RestaurantLocation> locations;
    private RestaurantLocation location;
    private int locationId;
    private int locationItemId;
    private int groupId;
    private int vendorId;

    public Item(ArrayList<Item> children, ArrayList<Item> newList){

    }

    public Item() {

    }

    public Item(int id, String name, Group group) {
        this.id = id;
        this.name = name;
        this.group = group;
    }

    public Item(String name, Group group) {
        this.name = name;
        this.group = group;
    }

    public Item(String name, Group group, int count) {
        this.name = name;
        this.group = group;
        this.count = count;
    }

    public Item(String name, Group group, int count, Vendor vendor) {
        this.name = name;
        this.group = group;
        this.count = count;
        this.vendor = vendor;
    }

    public Item(int id, String name, Group group, int count, Vendor vendor, RestaurantLocation location) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.count = count;
        this.vendor = vendor;
        this.location = location;
        this.locations.add(location);
    }

    public Item(String name, Group group, int count, Vendor vendor, RestaurantLocation location) {
        this.name = name;
        this.group = group;
        this.count = count;
        this.vendor = vendor;
        this.location = location;
        this.locations.add(location);
    }

    public Item(int id, String name, Group group, int count, Vendor vendor, RestaurantLocation location, int locationItemId) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.count = count;
        this.vendor = vendor;
        this.location = location;
        this.locationItemId = locationItemId;
    }

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

    public Group getGroup() { return group; }

    public void setGroup(Group group) { this.group = group; }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Vendor getVendor() { return vendor; }

    public void setVendor(Vendor vendor) { this.vendor = vendor; }

    public RestaurantLocation getLocation() {
        return location;
    }

    public void setLocation(RestaurantLocation location) {
        this.location = location;
    }

    public ArrayList<RestaurantLocation> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<RestaurantLocation> locations) {
        this.locations = locations;
    }

    public int getLocationItemId() {
        return locationItemId;
    }

    public void setLocationItemId(int locationItemId) {
        this.locationItemId = locationItemId;
    }

    public static Creator<Item> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(groupId);
        parcel.writeInt(count);
        parcel.writeInt(vendorId);
        parcel.writeInt(location.getId());
    }

    public Item(Parcel input){
        this.id = input.readInt();
        this.name = input.readString();
        this.groupId = input.readInt();
        this.count = input.readInt();
        this.vendorId = input.readInt();
        this.locationId = input.readInt();
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
