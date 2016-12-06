package com.poweredbyrita.countryskillets;

import java.util.ArrayList;

/**
 * Created by Biarre on 7/22/2015.
 */
public class Group {
    public int id;
    public String name;
    public Item child;
    public final ArrayList<Item> children = new ArrayList<Item>();

    public Group(String name) {
        this.name = name;
    }

    public Group(String name, ArrayList<Item> newList) {
    }

    public Group() {

    }

    public Group(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Group(int id, String name, Item child) {
        this.id = id;
        this.name = name;
        this.child = child;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Item> getChildren() {
        return children;
    }

    public Item getChild() { return child; }

    public void setChild(Item child) { this.child = child; }
}
