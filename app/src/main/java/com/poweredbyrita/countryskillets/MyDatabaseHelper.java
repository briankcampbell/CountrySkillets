package com.poweredbyrita.countryskillets;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Biarre on 5/17/2015.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "countrySkilletsDB";
    private static final String ITEMS_TABLE_NAME = "items";
    private static final String LOCATIONS_TABLE_NAME = "locations";
    private static final String VENDORS_TABLE_NAME = "vendors";
    private static final String GROUPS_TABLE_NAME = "groups";
    private static final String LOCATION_ITEMS_TABLE_NAME = "locationItems";
    private static final int DATABASE_VERSION = 1;
    private static final String ITEM_ID = "_itemId";
    private static final String ITEM_NAME = "itemName";
    private static final String ITEM_GROUP_ID = "itemGroup";
    private static final String ITEM_COUNT = "itemCount";
    private static final String VENDOR_ID = "_vendorId";
    private static final String VENDOR_NAME = "vendorName";
    private static final String VENDOR_PHONE = "vendorPhone";
    private static final String GROUP_ID = "_groupId";
    private static final String GROUP_NAME = "groupName";
    private static final String LOCATION_ID = "_locationId";
    private static final String LOCATION_IMAGE_ID = "locationImage";
    private static final String LOCATION_NAME = "locationName";
    private static final String LOCATION_ITEMS_ID = "_locationItemsID";
    private static final String LOCATION_ITEM_ID = "locationItemId";
    private static final String CREATE_ITEMS_TABLE = "CREATE TABLE " + ITEMS_TABLE_NAME + " ("
            + ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ITEM_NAME + " TEXT, "
            + ITEM_GROUP_ID + " INTEGER, "
            + VENDOR_ID + " INTEGER"
            + ");";
    private static final String CREATE_LOCATIONS_TABLE = "CREATE TABLE " + LOCATIONS_TABLE_NAME + " ("
            + LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + LOCATION_NAME + " TEXT, "
            + LOCATION_IMAGE_ID + " INTEGER"
            + ");";
    private static final String CREATE_VENDORS_TABLE = "CREATE TABLE " + VENDORS_TABLE_NAME + " ("
            + VENDOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + VENDOR_NAME + " TEXT, "
            + VENDOR_PHONE + " TEXT"
            + ");";
    private static final String CREATE_GROUPS_TABLE = "CREATE TABLE " + GROUPS_TABLE_NAME + " ("
            + GROUP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + GROUP_NAME + " TEXT"
            + ");";
    private static final String CREATE_LOCATION_ITEMS_TABLE = "CREATE TABLE " + LOCATION_ITEMS_TABLE_NAME + " ("
            + LOCATION_ITEMS_ID + " INTEGER PRIMARY KEY, "
            + LOCATION_ID + " INTEGER, "
            + LOCATION_ITEM_ID + " INTEGER, "
            + ITEM_COUNT + " INTEGER"
            + ");";
    
    private Context context;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.i(MainActivity.TAG, "Database helper onCreate method");


        try {
            db.execSQL(CREATE_ITEMS_TABLE);
            db.execSQL(CREATE_LOCATIONS_TABLE);
            db.execSQL(CREATE_VENDORS_TABLE);
            db.execSQL(CREATE_GROUPS_TABLE);
            db.execSQL(CREATE_LOCATION_ITEMS_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Log.i(MainActivity.TAG, CREATE_ITEMS_TABLE);
        Log.i(MainActivity.TAG, CREATE_LOCATIONS_TABLE);
        Log.i(MainActivity.TAG, CREATE_VENDORS_TABLE);
        Log.i(MainActivity.TAG, CREATE_GROUPS_TABLE);
        Log.i(MainActivity.TAG, CREATE_LOCATION_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {

    }

    public void addGroup(Group group){
        ContentValues groupValues = new ContentValues();

        if(getGroups().size() == 0) {
            groupValues.put(GROUP_ID, 0);
        }

        groupValues.put(GROUP_NAME, group.getName());

        //Log.i(MainActivity.TAG, "MyDatabaseHelper: Group Values = " + groupValues);

        //Inserting values into database
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(GROUPS_TABLE_NAME, null, groupValues);

        //db.close();


        Log.i(MainActivity.TAG, "MyDatabaseHelper: Group " + group.getName() + " inserted into the database");


    }

    public void editGroup(Group group){
        ContentValues groupValues = new ContentValues();

        groupValues.put(GROUP_ID, group.getId());
        groupValues.put(GROUP_NAME, group.getName());

        Log.i(MainActivity.TAG, "MyDatabaseHelper: Item Values = " + groupValues);

        //Inserting values into database
        SQLiteDatabase db = this.getWritableDatabase();
        db.replace(GROUPS_TABLE_NAME, null, groupValues);
        //db.close();

        Log.i(MainActivity.TAG, "MyDatabaseHelper: Group record replaced in the database");


    }

    public ArrayList<Group> getGroups(){
        ArrayList<Group> groups = new ArrayList<>();

        //Accessing and querying database.
        String query = "SELECT * FROM " + GROUPS_TABLE_NAME + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        //Populating ArrayList items with database entries.
        while(!cursor.isAfterLast()){
            Group group = new Group();
            group.setId(cursor.getInt(0));
            group.setName(cursor.getString(1));

            if(group.getId() <= -1){
                group.setId(groups.size());
                editGroup(group);
            }

            groups.add(group);

            cursor.moveToNext();
        }

        //Closing query and database out.
        cursor.close();
        //db.close();

        return groups;
    }

    public void removeGroup(Group group){
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(GROUPS_TABLE_NAME, ITEM_ID + "=" + group.getId(), null);

        if(result > 0){
            Log.i(MainActivity.TAG, result + " rows removed");
        }
    }

    public void addDefaultItem(Item item){
        ContentValues itemValues = new ContentValues();

        if(getItems().size() == 0) {
            itemValues.put(ITEM_ID, 0);
        } else {
            itemValues.put(ITEM_ID, item.getId());
        }

        itemValues.put(ITEM_NAME, item.getName());
        itemValues.put(ITEM_GROUP_ID, item.getGroup().getId());

        if(item.getVendor() != null) {
            itemValues.put(VENDOR_ID, item.getVendor().getId());
        }

        //Log.i(MainActivity.TAG, "MyDatabaseHelper: Item Values = " + itemValues);

        //Inserting values into database
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(ITEMS_TABLE_NAME, null, itemValues);

        //db.close();

        //Log.i(MainActivity.TAG, "MyDatabaseHelper: Item " + item.getName() + " inserted into the database");
    }

    public void addItem(Item item){
        ContentValues itemValues = new ContentValues();

        itemValues.put(ITEM_NAME, item.getName());

        if(item.getGroup() != null) {
            itemValues.put(ITEM_GROUP_ID, item.getGroup().getId());
        }

        if(item.getVendor() != null) {
            itemValues.put(VENDOR_ID, item.getVendor().getId());
        }

        //Log.i(MainActivity.TAG, "MyDatabaseHelper: Item Values = " + itemValues);

        //Inserting values into database
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(ITEMS_TABLE_NAME, null, itemValues);
        //db.close();

        //Log.i(MainActivity.TAG, "MyDatabaseHelper: Item " + item.getId() + " " + item.getName() + " inserted into the database");
    }

    public void editItem(Item item){
        ContentValues itemValues = new ContentValues();
        //ContentValues locationValues = new ContentValues();

        itemValues.put(ITEM_NAME, item.getName());
        itemValues.put(ITEM_GROUP_ID, item.getGroup().getId());
        itemValues.put(VENDOR_ID, item.getVendor().getId());

        Log.i(MainActivity.TAG, "MyDatabaseHelper: Item Values = " + itemValues);
        //Log.i(MainActivity.TAG, "MyDatabaseHelper: Location Values = " + locationValues);

        //Inserting values into database
        SQLiteDatabase db = this.getWritableDatabase();
        db.replace(ITEMS_TABLE_NAME, null, itemValues);
        db.close();

        Log.i(MainActivity.TAG, "MyDatabaseHelper: Item record replaced in the database");


    }

    public ArrayList<Item> getItems(){
        ArrayList<Item> items = new ArrayList<>();

        //Accessing and querying database.
        String query = "SELECT * FROM " + ITEMS_TABLE_NAME + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

        //Populating ArrayList items with database entries.
        while(!cursor.isAfterLast()){
            Item item = new Item();
            item.setId(cursor.getInt(0));
            item.setName(cursor.getString(1));
            item.setGroup(getGroups().get(cursor.getInt(2)));
            item.setVendor(getVendors().get(cursor.getInt(3)));

            if(item.getId() <= -1){
                item.setId(items.size());
                editItem(item);
            }

            items.add(item);

            cursor.moveToNext();
        }

        //Closing query and database out.
        cursor.close();
        //db.close();

        return items;
    }

    public void removeItem(Item item){
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(ITEMS_TABLE_NAME, ITEM_ID + "=" + item.getId(), null);

        if(result > 0){
            Log.i(MainActivity.TAG, result + " rows removed");
        }
    }

    public void addDefaultItemToLocations(Item item){
        ContentValues values = new ContentValues();

        for(int i = 0; i < getLocations().size(); i++) {
            values.put(LOCATION_ID, getLocations().get(i).getId());
            values.put(LOCATION_ITEM_ID, item.getId());
            values.put(ITEM_COUNT, item.getCount());


            Log.i(MainActivity.TAG, "MyDatabaseHelper: Values = " + values);

            //Inserting values into database
            SQLiteDatabase db = this.getWritableDatabase();
            db.insert(LOCATION_ITEMS_TABLE_NAME, null, values);
            //db.close();
            Log.i(MainActivity.TAG, "MyDatabaseHelper: Location Item Count record for " + item.getName() + " inserted into the database");
        }



    }

    public void addItemToLocation(Item item){
        ContentValues values = new ContentValues();
        String locId = String.valueOf(item.getLocation().getId()+1);
        String itemId = String.valueOf(item.getId());
        String locItemId = locId + itemId;

        item.setLocationItemId(Integer.parseInt(locItemId));

        values.put(LOCATION_ITEMS_ID, item.getLocationItemId());
        values.put(LOCATION_ID, item.getLocation().getId());
        values.put(LOCATION_ITEM_ID, item.getId());
        values.put(ITEM_COUNT, item.getCount());


        Log.i(MainActivity.TAG, "MyDatabaseHelper: Values = " + values);

        //Inserting values into database
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(LOCATION_ITEMS_TABLE_NAME, null, values);
        //db.close();

        //Log.i(MainActivity.TAG, "MyDatabaseHelper: Location Item Count record for " + item.getId() + " " + item.getName() + " " + item.getLocation().getName() + " inserted into the database");


    }

    public Item getLocationItemCount(Item item){
        int locationId = item.getLocation().getId();
        int itemId = item.getId();
        int itemCount = item.getCount();

        String query = "SELECT * FROM " + LOCATION_ITEMS_TABLE_NAME + " WHERE " + LOCATION_ID + " = " + locationId + " AND " + LOCATION_ITEM_ID + " = " + itemId + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        //Populating ArrayList items with database entries.
        while(!cursor.isAfterLast()) {
            itemCount = cursor.getInt(2);
        }

        item.setCount(itemCount);

        return item;
    }

    public ArrayList<Item> getLocationItems(int locId){
       RestaurantLocation location = getLocations().get(locId);
        ArrayList<Item> items = getItems();
        ArrayList<Item> locationItems = new ArrayList<>();
        Item locationItem;

        Log.i(MainActivity.TAG, "MyDatabaseHelper: locID = " + locId);
        Log.i(MainActivity.TAG, "MyDataBaseHelper: locName =  " + location.getName());

        //Accessing and querying database.
        String query = "SELECT * FROM " + LOCATION_ITEMS_TABLE_NAME + " WHERE " + LOCATION_ID + " = " + locId + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        //Populating ArrayList locationItems with database entries.
        while(!cursor.isAfterLast()) {
            //locationItem = new Item();
            locationItem = items.get(cursor.getInt(2));
            locationItem.setLocation(getLocations().get(cursor.getInt(1)));
            //locationItem.setId(cursor.getInt(2));
            //locationItem.setName(items.get(cursor.getInt(1)).getName());
            //locationItem.setGroup(items.get(cursor.getInt(1)).getGroup());
            //locationItem.setVendor(items.get(cursor.getInt(1)).getVendor());
            locationItem.setCount(cursor.getInt(3));
            locationItem.setLocationItemId(cursor.getInt(0));

            Log.i(MainActivity.TAG, "MyDatabaseHelper: LocationID = " + locationItem.getLocation().getId() + " " + locationItem.getLocation().getName());
            Log.i(MainActivity.TAG, "MyDatabaseHelper: Item = " + locationItem.getId() + " " + locationItem.getName());
            Log.i(MainActivity.TAG, "MyDatabaseHelper: LocationItemId = " + locationItem.getLocationItemId());
            Log.i(MainActivity.TAG, "MyDatabaseHelper: ItemGroup = " + locationItem.getGroup().getName());
            Log.i(MainActivity.TAG, "MyDatabaseHelper: ItemVendor = " + locationItem.getVendor().getName());
            Log.i(MainActivity.TAG, "MyDatabaseHelper: ItemCount = " + locationItem.getCount());

            locationItems.add(locationItem);

            cursor.moveToNext();
        }

        Log.i(MainActivity.TAG, "MyDatabaseHelper: Location Items ArrayList Size: " + locationItems.size());

        //Closing query and database out.
        cursor.close();
        //db.close();

        return locationItems;
    }

    public void editLocationItemCount(Item item){
        ContentValues values = new ContentValues();

        values.put(ITEM_COUNT, item.getCount());

        Log.i(MainActivity.TAG, "MyDatabaseHelper: Values = " + values + "    LocationId = " + item.getLocation().getId() + "   ItemId = " + item.getId() + "    LOCATION_ITEM_ID = " + LOCATION_ITEM_ID);

        //Inserting values into database
        SQLiteDatabase db = this.getWritableDatabase();
        //db.update(LOCATION_ITEMS_TABLE_NAME, values, LOCATION_ID + " = " + item.getLocation().getId() + " AND " + LOCATION_ITEM_ID + " = " + item.getId(), null);
        db.update(LOCATION_ITEMS_TABLE_NAME, values, LOCATION_ITEMS_ID + " = " + item.getLocationItemId(), null);
        db.close();

        Log.i(MainActivity.TAG, "MyDatabaseHelper: Location Item Count record inserted into the database");
    }

    public void removeItemFromLocation(Item item){
        int itemId = item.getId();
        int locId = item.getLocation().getId();
        int id = item.getLocationItemId();
        Log.i(MainActivity.TAG, "MyDatabaseHelper: " + item.getId() + " " + item.getName() + " is being deleted from " + item.getLocation().getId() + "   " + " " + item.getLocation().getName());

        SQLiteDatabase db = this.getWritableDatabase();
        //int result = db.delete(LOCATION_ITEMS_TABLE_NAME, LOCATION_ID + " = " + locId + " AND " + LOCATION_ITEM_ID + " = " + itemId, null);

        int result = db.delete(LOCATION_ITEMS_TABLE_NAME, LOCATION_ITEMS_ID + " = " + id, null);

        if(result > 0){
            Log.i(MainActivity.TAG, "MyDatabaseHelper: removed " + result + " row(s) from location table");
        }
    }

    public void addLocation(RestaurantLocation location){
        ContentValues values = new ContentValues();

        if(getLocations().size() == 0) {
            values.put(LOCATION_ID, 0);
        }

        values.put(LOCATION_NAME, location.getName());
        values.put(LOCATION_IMAGE_ID, location.getImageId());

        Log.i(MainActivity.TAG, "myDatabaseHelper - addLocation: Values = " + values);

        //Inserting values into database
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(LOCATIONS_TABLE_NAME, null, values);
        //db.close();

        Log.i(MainActivity.TAG, "Location record inserted into the database");


    }

    public void editLocation(RestaurantLocation location){
        ContentValues values = new ContentValues();

        values.put(LOCATION_ID, location.getId());
        values.put(LOCATION_NAME, location.getName());
        values.put(LOCATION_IMAGE_ID, location.getImageId());

        Log.i(MainActivity.TAG, "MyDatabaseHelper: Values = " + values);

        //Inserting values into database
        SQLiteDatabase db = this.getWritableDatabase();
        db.replace(ITEMS_TABLE_NAME, null, values);
        //db.close();

        Log.i(MainActivity.TAG, "MyDatabaseHelper: record replaced in the database");


    }

    public ArrayList<RestaurantLocation> getLocations(){
        ArrayList<RestaurantLocation> locations = new ArrayList<>();

        //Accessing and querying database.
        String query = "SELECT * FROM " + LOCATIONS_TABLE_NAME + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        //Populating ArrayList items with database entries.
        while(!cursor.isAfterLast()){
            RestaurantLocation location = new RestaurantLocation();

            location.setId(Integer.parseInt(cursor.getString(0)));
            location.setName(cursor.getString(1));
            location.setImageId(cursor.getInt(2));

            locations.add(location);

            cursor.moveToNext();
        }

        //Log.i(MainActivity.TAG, "MyDatabaseHelper: locations = " + locations);

        //Closing query and database out.
        cursor.close();
        //db.close();

        return locations;
    }

    public void removeLocation(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(LOCATIONS_TABLE_NAME, LOCATION_ID + "=" + id, null);

        if(result > 0){
            Log.i(MainActivity.TAG, result + " rows removed");
        }
    }

    public void addVendor(Vendor vendor){
        ContentValues values = new ContentValues();

        if(getVendors().size() == 0) {
            values.put(VENDOR_ID, 0);
        }

        values.put(VENDOR_NAME, vendor.getName());
        values.put(VENDOR_PHONE, vendor.getPhone());

        //Log.i(MainActivity.TAG, "MyDatabaseHelper: Values = " + values);

        //Inserting values into database
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(VENDORS_TABLE_NAME, null, values);
        //db.close();

        Log.i(MainActivity.TAG, "MyDatabaseHelper - addVendor: Vendor " + vendor.getName() + " " + vendor.getPhone() + " inserted into the database");


    }

    public void editVendor(Vendor vendor){
        ContentValues values = new ContentValues();

        values.put(VENDOR_ID, vendor.getId());
        values.put(VENDOR_NAME, vendor.getName());
        values.put(VENDOR_PHONE, vendor.getPhone());

        Log.i(MainActivity.TAG, "MyDatabaseHelper: Values = " + values);

        //Inserting values into database
        SQLiteDatabase db = this.getWritableDatabase();
        db.replace(VENDORS_TABLE_NAME, null, values);
        //db.close();

        Log.i(MainActivity.TAG, "MyDatabaseHelper: record inserted into the database");


    }

    public ArrayList<Vendor> getVendors(){
        ArrayList<Vendor> vendors = new ArrayList<>();

        //Accessing and querying database.
        String query = "SELECT * FROM " + VENDORS_TABLE_NAME + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        //Populating ArrayList items with database entries.
        while(!cursor.isAfterLast()){
            Vendor vendor = new Vendor();
            vendor.setId(Integer.parseInt(cursor.getString(0)));
            vendor.setName(cursor.getString(1));
            vendor.setPhone(cursor.getString(2));

            vendors.add(vendor);

            cursor.moveToNext();
        }

        //Closing query and database out.
        cursor.close();
        //db.close();

        //Log.i(MainActivity.TAG, "MyDatabaseHelper: vendors table size= " + vendors.size());

        return vendors;
    }

    public void removeVendor(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(VENDORS_TABLE_NAME, VENDOR_ID + "=" + id, null);

        if(result > 0){
            Log.i(MainActivity.TAG, result + " rows removed");
        }
    }

}
