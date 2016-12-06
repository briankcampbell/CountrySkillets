package com.poweredbyrita.countryskillets;

import android.location.Location;

/**
 * Created by Biarre on 7/10/2015.
 */
public class CustomObject {
    public Item entryItem;
    public int entryItemName;
    public int entryItemCount;
    public int entryItemLocation;
    public int entryItemVendor;
    public Location entryLocation;
    public int entryLocationImage;
    public int entryLocationName;

    public CustomObject(){
    }

    public CustomObject(Item item){
        this.entryItem = item;
    }

    public CustomObject(Location location){
        this.entryLocation = location;
    }

    public CustomObject(int image, int name){
        this.entryLocationImage = image;
        this.entryLocationName = name;
    }

    public CustomObject(int itemName, int count, int itemLocation){
        this.entryItemName= itemName;
        this.entryItemCount = count;
        this.entryItemLocation = itemLocation;
    }
}

