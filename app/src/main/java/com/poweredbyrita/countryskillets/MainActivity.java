package com.poweredbyrita.countryskillets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends Activity {
    ListView listView;
    CustomAdapterLocation arrayAdapterLocation;
    public static final String TAG = "MYTAG";
    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<RestaurantLocation> locations = new ArrayList<>();
    private ArrayList<Vendor> vendors = new ArrayList<>();
    private ArrayList<Group> groups = new ArrayList<>();
    public MyDatabaseHelper myHelper;
    public ArrayList<String> itemGroups = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myHelper = new MyDatabaseHelper(this);




        if (items == null) {
            items = new ArrayList<>();
        }

        if(myHelper.getItems().isEmpty()){
            Runnable runnable = new Runnable() {
                public void run() {
                    populateItemsTable();
                }
            };

            Thread myThread = new Thread(runnable);
            myThread.start();

            if(items.isEmpty()) {
                try {
                    items = myHelper.getItems();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if(items.isEmpty()) {
                while (items.isEmpty()) {
                    Toast.makeText(this, "R.I.T.A. is Initializing...Please Wait",
                            Toast.LENGTH_SHORT).show();
                }
            }

            if (items.size() <= 0) {
                //Log.i(TAG, "MainActivity - populateItemsTable(): Items Table NOT POPULATED");
                Toast.makeText(this, "ERROR - Items Table NOT POPULATED - Please Reinstall or Contact Developer",
                        Toast.LENGTH_LONG).show();
            } else {
                //Log.i(TAG, "MainActivity - populateItemsTable(): Items Table POPULATED");
                Toast.makeText(this, "R.I.T.A. Successfully Initialized All Items",
                        Toast.LENGTH_LONG).show();

                Toast.makeText(this, "R.I.T.A. has added default items to all locations",
                        Toast.LENGTH_LONG).show();

                Toast.makeText(this, "R.I.T.A. is Ready",
                        Toast.LENGTH_LONG).show();
            }


        }

        locations = myHelper.getLocations();

        populateLocationsList();
    }

    private void populateLocationsList() {
        listView = (ListView) findViewById(R.id.locationsList);
        arrayAdapterLocation = new CustomAdapterLocation(this, R.layout.layout_row_locations, locations);
        listView.setAdapter(arrayAdapterLocation);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View v, final int position, long l) {
                Runnable runnable = new Runnable() {
                    public void run() {
                        Log.i("MyCountrySkilletsApp", "I clicked on position " + position);

                        RestaurantLocation lvItem = (RestaurantLocation) adapterView.getItemAtPosition(position);

                        String locName = lvItem.getName();
                        int locId = lvItem.getId();

                        Log.i(TAG, "Main Activity: Location Name = " + locName + "     Location ID = " + locId);

                        Intent intent = new Intent(MainActivity.this, InventoryActivity.class);
                        intent.putExtra("locName", locName);
                        intent.putExtra("locId", locId);
                        intent.putStringArrayListExtra("itemGroups", itemGroups);

                        startActivityForResult(intent, 1);
                    }
                };

                Thread myThread = new Thread(runnable);
                myThread.start();
            }
        });
    }

    private void populateLocationsTable(){
        myHelper.addLocation(new RestaurantLocation("W. Yosemite Ave. - Manteca, CA", R.drawable.cs_manteca));
        myHelper.addLocation(new RestaurantLocation("Harbor Street - Pittsburg, CA", R.drawable.cs_pittsburg_harbor));
        myHelper.addLocation(new RestaurantLocation("Marina Blvd. - Pittsburg, CA", R.drawable.cs_pittsburg_marina));

        locations = myHelper.getLocations();
        Log.i(TAG, "MainActivity - populateLocationsTable(): Locations Table Size = " + locations.size());
        if(locations.size() > 0){
            Log.i(TAG, "MainActivity - populateLocationsTable(): Locations Table Populated");
        } else {
            Log.i(TAG, "MainActivity - populateLocationsTable(): Locations Table Not Populated");
        }

        for(int i = 0; i < locations.size(); i++) {
            Log.i(TAG, "MainActivity - populateLocationsTable(): Locations Table item " + i + "  ID = " + locations.get(i).getId() + " " + locations.get(i).getName());
        }
    }

    public void gotoVendorsActivity(View v) {
        Intent intent = new Intent(this, VendorsActivity.class);
        startActivity(intent);
    }

    private void populateVendorsTable(){
        myHelper.addVendor(new Vendor("Athens Baking Company, Inc.", "(510)533-5705"));
        myHelper.addVendor(new Vendor("Cash & Carry", "(209)526-3923"));
        myHelper.addVendor(new Vendor("Farmer Brothers", "(209)466-0203"));
        myHelper.addVendor(new Vendor("SaveMart", "(209)823-1768"));
        myHelper.addVendor(new Vendor("Sysco", "(209)527-7700 x7754"));
        myHelper.addVendor(new Vendor("US. Foods", "(800)682-1228 x320"));

        vendors = myHelper.getVendors();

        for(int i = 0; i < vendors.size(); i++) {
            Log.i(TAG, "MainActivity - populateVendorsTable(): Vendor = " + vendors.get(i).getName() + " " + vendors.get(i).getPhone());
        }
    }

    private void populateGroupsTable(){
        myHelper.addGroup(new Group(0, "Front Items"));
        myHelper.addGroup(new Group(1, "Produce"));
        myHelper.addGroup(new Group(2, "Meats"));
        myHelper.addGroup(new Group(3, "Frozen Items"));
        myHelper.addGroup(new Group(4, "Refrigerated Items"));
        myHelper.addGroup(new Group(5, "Dry"));

        groups = myHelper.getGroups();

        for(int i = 0; i < groups.size(); i++){
            Log.i(TAG, "MainActivity - populateGroupsTable(): Group = " + groups.get(i).getId() + " "  + groups.get(i).getName());
        }

        Log.i(TAG, "MainActivity - populateGroupsTable(): groups.size() = " + groups.size());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

/*        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {

                    RestaurantLocation location = data.getParcelableExtra("location");

                    locations.add(location);
                    arrayAdapterLocation.notifyDataSetChanged();

                    myHelper = new MyDatabaseHelper(this);
                    SQLiteDatabase db = myHelper.getWritableDatabase();
                    myHelper.addItem(items.get(items.size() - 1));
                    items = myHelper.getItems();

                    for(int i = 0; i < items.size(); i++){
                        Log.i(TAG, "ID: " + items.get(i).getId() + "    "
                                        + "Name: " + items.get(i).getName() + "    "
                                        + "Count: " + items.get(i).getCount() + "    "
                                        + "Vendor: " + items.get(i).getVendor().getName() + "."

                        );
                    }
                    populateLocationsList();
                }
                break;

            case 1:
                if (resultCode == RESULT_OK) {

                    Item item = data.getParcelableExtra("item");

                    Bundle bundle = getIntent().getExtras();
                    int position = getIntent().getIntExtra("position", 0);
                    Log.i("LOG", "Position: " + position);

                    SQLiteDatabase db = myHelper.getWritableDatabase();
                    myHelper.editItem(item);
                    items = myHelper.getItems();
                    arrayAdapterLocation.notifyDataSetChanged();

                    for(int i = 0; i < items.size(); i++){
                        Log.i(TAG, "ID: " + items.get(i).getId() + "    "
                                        + "Name: " + items.get(i).getName() + "    "
                                        + "Count: " + items.get(i).getCount() + "    "
                                        + "Vendor: " + items.get(i).getVendor().getName() + "."

                        );
                    }

                    if(locations.size() != 0){
                        populateLocationsList();
                    }
                }
                break;
        }
*/    }

    private void populateItemsTable() {
        //Setup locations

        if(locations.isEmpty()) {
            populateLocationsTable();
        }
        locations = myHelper.getLocations();

        populateLocationsList();

        //setup Vendors
        if(vendors.isEmpty()) {
            populateVendorsTable();
        }

        vendors = myHelper.getVendors();

        //setup groups
        if(groups.isEmpty()) {
            populateGroupsTable();
        }

        groups = myHelper.getGroups();

        //If items table is empty, populate table with all items) {
        String[] defaultNamesFront;
        String[] defaultNamesProduce;
        String[] defaultNamesMeats;
        String[] defaultNamesFrozen;
        String[] defaultNamesRefrigerated;
        String[] defaultNamesDry;

        defaultNamesFront = new String[]{"Lemons", "Cucumbers", "Blue Cheese", "Caesar",
                "Thousand Island", "Honey Mustard", "Oriental", "Butter Tubs", "Cream Cheese",
                "Sour Cream", "Half & Half", "Wheat", "White", "Rye", "English Muffins",
                "Sourdough", "Bagels", "French Toast", "French Rolls", "Buns", "Milk",
                "Orange Juice", "Pies", "Whip", "Ice Cream", "Togo Ketchup", "Gallon Tobasco",
                "Ketchup Bottles", "Ketchup Refills", "Mustard", "Tapatio", "Tabasco",
                "Sugar Packets", "Equal", "Sweet 'n Low", "Splenda", "Honey", "French Vanilla",
                "Lollipops", "Syrup", "Sugar-Free Syrup", "Togo Large", "Togo Small",
                "Portion Cups", "Straws", "Guest Checks", "Napkins", "Cocktail Napkins",
                "Powdered Sugar", "T-Shirt Bags", "Apple Juice", "Cranberry Juice", "Tomato Juice",
                "Togo Silverware", "Bleach", "Cup Lids", "Cup Holders", "Hand Soap", "Pine-sol",
                "Assorted Jams"};
        defaultNamesProduce = new String[]{"Oranges", "Avocados", "Tomatoes", "Red Potatoes",
                "Red Onions", "Yellow Onions", "Cilantro", "Parsley", "Celery", "Red Cabbage",
                "Green Onion", "Zucchini", "Shredded Carrots", "Green Leaf", "Cantaloupe",
                "Honey Dew", "Pineapple", "Strawberries", "Mushrooms", "Green Bell", "Red Bell",
                "Iceberg", "Spinach", "Pickles"};
        defaultNamesMeats = new String[]{"Linguica", "Hot Link", "Bacon", "Roast Beef", "Ham",
                "Turkey", "Pork Loin", "Rib-Eye", "Sausage", "Sausage Rolls", "Veg. Patty",
                "Turkey Sausage", "CF's", "3-1 Patty", "Chicken Breast", "Wings", "Chicken Strips",
                "Fish", "Shrimp", "Corned Beef Hash", "Chorizo"};
        defaultNamesFrozen = new String[]{"Waffle Fries", "French Fries", "Onion Rings",
                "Hash Browns", "Clam Chowder", "Frozen Strawberries", "Frozen Blueberries",
                "Frozen Apples"};
        defaultNamesRefrigerated = new String[]{"American", "Swiss", "PepperJack", "Cheddar",
                "Jack", "Parmesan", "Egg Beaters", "Buttermilk", "Liquid Eggs", "Shell Eggs",
                "Tartar Sauce", "Egg Whites", "Mayo", "BBQ", "Cream Cheese"};
        defaultNamesDry = new String[]{"Kidney Beans", "Olives", "Ortega", "Gloves",
                "Ranch Packets", "Saltines", "A1", "Hollandaise", "Tuna", "Pan & Grill",
                "Fry Shortening", "Cake", "Biscuit", "Strawberry Glaze", "Grits", "Oats",
                "Tomatillos", "Sugar Bulk", "Salt", "Flour", "Vanilla Extract", "Cajun",
                "Parsley Flakes", "Paprika", "Cinnamon", "Spices", "Soda Large Cup",
                "Soda Small Cup", "Paper Towels", "Brown Sugar", "Raisins", "Pecans", "Almonds",
                "Chow Mein Noodles", "De-greaser", "Clear Wrap", "Toothpicks", "Fill-picks",
                "Sandwich Wrap", "Trash Bags", "Dish Soap", "Tortillas", "Grill Bricks",
                "Water Bottles", "El Pato", "Chili Beans"};

        //defaultVendorId = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        //Populate "Front" Kitchen
        //final List<Group> defaultItemGroups = Arrays.asList(defaultGroups);
        final List<String> defaultFrontItems = Arrays.asList(defaultNamesFront);
        final List<String> defaultProduce = Arrays.asList(defaultNamesProduce);
        final List<String> defaultMeats = Arrays.asList(defaultNamesMeats);
        final List<String> defaultFrozen = Arrays.asList(defaultNamesFrozen);
        final List<String> defaultRefrigerated = Arrays.asList(defaultNamesRefrigerated);
        final List<String> defaultDry = Arrays.asList(defaultNamesDry);
        //ArrayList<int> defaultItemVendorId = new ArrayList<>(Arrays.asList(defaultVendorId));

        Runnable runnable = new Runnable() {
            public void run() {
                //items = new ArrayList<>();
                //Add items to Items table in database
                for (int i = 0; i < defaultFrontItems.size(); i++) {
                    myHelper.addDefaultItem(new Item(myHelper.getItems().size(), defaultFrontItems.get(i), groups.get(0)));
                    items = myHelper.getItems();
                    //Log.i(TAG, "MainActivity - populateItemsTable() - defaultFrontItems: Item = " + items.get(myHelper.getItems().size()-1).getId() + " " + items.get(myHelper.getItems().size()-1).getName() + " - " + items.get(myHelper.getItems().size()-1).getGroup().getId() + " " + items.get(myHelper.getItems().size()-1).getGroup().getName());
                    //Toast.makeText(getParent(), "MainActivity - populateItemsTable() - defaultFrontItems: Item = " + items.get(myHelper.getItems().size()-1).getId() + " " + items.get(myHelper.getItems().size()-1).getName() + " - " + items.get(myHelper.getItems().size()-1).getGroup().getId() + " " + items.get(myHelper.getItems().size()-1).getGroup().getName(),
                    //Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "MainActivity - populateItemsTable(): Item Group Complete.  Items Table Size = " + items.size());

                //Populate "Back" Kitchen
                //"Produce" Group
                for (int i = 0; i < defaultProduce.size(); i++) {
                    myHelper.addDefaultItem(new Item(myHelper.getItems().size(), defaultProduce.get(i), groups.get(1)));
                    items = myHelper.getItems();
                    //Log.i(TAG, "MainActivity - populateItemsTable() - defaultProduce: Item = " + items.get(items.size()-1).getId() + " " + items.get(items.size()-1).getName() + " - " + items.get(items.size()-1).getGroup().getId() + " " + items.get(items.size()-1).getGroup().getName());
                    //Toast.makeText(getParent(), "MainActivity - populateItemsTable() - defaultProduce: Item = " + items.get(items.size()-1).getId() + " " + items.get(items.size()-1).getName() + " - " + items.get(items.size()-1).getGroup().getId() + " " + items.get(items.size()-1).getGroup().getName(),
                    //Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "MainActivity - populateItemsTable(): Item Group Complete.  Items Table Size = " + items.size());

                //"Meats" Group
                for (int i = 0; i < defaultMeats.size(); i++) {
                    myHelper.addDefaultItem(new Item(myHelper.getItems().size(), defaultMeats.get(i), groups.get(2)));
                    items = myHelper.getItems();
                    //Log.i(TAG, "MainActivity - populateItemsTable() - defaultMeats: Item = " + items.get(items.size()-1).getId() + " " + items.get(items.size()-1).getName() + " - " + items.get(items.size()-1).getGroup().getId() + " " + items.get(items.size()-1).getGroup().getName());
                    //Toast.makeText(getParent(),"MainActivity - populateItemsTable() - defaultMeats: Item = " + items.get(items.size()-1).getId() + " " + items.get(items.size()-1).getName() + " - " + items.get(items.size()-1).getGroup().getId() + " " + items.get(items.size()-1).getGroup().getName(),
                    //Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "MainActivity - populateItemsTable(): Item Group Complete.  Items Table Size = " + items.size());

                //"Frozen" Group
                for (int i = 0; i < defaultFrozen.size(); i++) {
                    myHelper.addDefaultItem(new Item(myHelper.getItems().size(), defaultFrozen.get(i), groups.get(3)));
                    items = myHelper.getItems();
                    //Log.i(TAG, "MainActivity - populateItemsTable() - defaultFrozen: Item = " + items.get(items.size()-1).getId() + " " + items.get(items.size()-1).getName() + " - " + items.get(items.size()-1).getGroup().getId() + " " + items.get(items.size()-1).getGroup().getName());
                    //Toast.makeText(getParent(), "MainActivity - populateItemsTable() - defaultFrozen: Item = " + items.get(items.size()-1).getId() + " " + items.get(items.size()-1).getName() + " - " + items.get(items.size()-1).getGroup().getId() + " " + items.get(items.size()-1).getGroup().getName(),
                     //Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "MainActivity - populateItemsTable(): Item Group Complete.  Items Table Size = " + items.size());

                //"Refrigerated" Group
                for (int i = 0; i < defaultRefrigerated.size(); i++) {
                    myHelper.addDefaultItem(new Item(myHelper.getItems().size(), defaultRefrigerated.get(i), groups.get(4)));
                    items = myHelper.getItems();
                    //Log.i(TAG, "MainActivity - populateItemsTable() - defaultRefrigerated: Item = " + items.get(items.size()-1).getId() + " " + items.get(items.size()-1).getName() + " - " + items.get(items.size()-1).getGroup().getId() + " " + items.get(items.size()-1).getGroup().getName());
                    //Toast.makeText(getParent(), "MainActivity - populateItemsTable() - defaultRefrigerated: Item = " + items.get(items.size()-1).getId() + " " + items.get(items.size()-1).getName() + " - " + items.get(items.size()-1).getGroup().getId() + " " + items.get(items.size()-1).getGroup().getName(),
                    //Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "MainActivity - populateItemsTable(): Item Group Complete.  Items Table Size = " + items.size());

                //"Dry" Group
                for (int i = 0; i < defaultDry.size(); i++) {
                    myHelper.addDefaultItem(new Item(myHelper.getItems().size(), defaultDry.get(i), groups.get(5)));
                    items = myHelper.getItems();
                    //Log.i(TAG, "MainActivity - populateItemsTable() - defaultDry: Item = " + items.get(items.size()-1).getId() + " " + items.get(items.size()-1).getName() + " - " + items.get(items.size()-1).getGroup().getId() + " " + items.get(items.size()-1).getGroup().getName());
                    //Toast.makeText(getParent(), "MainActivity - populateItemsTable() - defaultDry: Item = " + items.get(items.size()-1).getId() + " " + items.get(items.size()-1).getName() + " - " + items.get(items.size()-1).getGroup().getId() + " " + items.get(items.size()-1).getGroup().getName(),
                    //Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "MainActivity - populateItemsTable(): Item Group Complete.  Items Table Size = " + items.size());
                //Toast.makeText(getParent(), "MainActivity - populateItemsTable(): Item Group Complete.  Items Table Size = " + items.size(),
                //Toast.LENGTH_SHORT).show();

                items = myHelper.getItems();

                Log.i(TAG, "MainActivity - populateItemsTable(): Item Table Complete.  Items Table Size = " + items.size());
                //Toast.makeText(getParent(), "MainActivity - populateItemsTable(): Item Table Complete.  Items Table Size = " + items.size(),
                //Toast.LENGTH_SHORT).show();

                Log.i(TAG, "MainActivity - populateItemsTable(): Items Table Size = " + items.size());
                //Toast.makeText(getParent(), "MainActivity - populateItemsTable(): Items Table Size = " + items.size(),
                //Toast.LENGTH_SHORT).show();

                if (items.size() > 0) {
                    Log.i(TAG, "MainActivity - populateItemsTable(): Items Table POPULATED");
                    //Toast.makeText(getParent(), "MainActivity - populateItemsTable(): Items Table POPULATED",
                    //        Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "MainActivity - populateItemsTable(): Items Table NOT POPULATED");
                    //Toast.makeText(getParent(), "MainActivity - populateItemsTable(): Items Table NOT POPULATED",
                    //        Toast.LENGTH_SHORT).show();
                }

                for (int i = 0; i < locations.size(); i++) {
                    for (int j = 0; j < items.size(); j++) {
                        Item locationItem = items.get(j);
                        locationItem.setLocation(locations.get(i));
                        myHelper.addItemToLocation(items.get(j));
                        //Log.i(TAG, "MainActivity - populateItemsTable(): addItemToLocation = " + locations.get(i).getId() + " " + locations.get(i).getName() + "   " + items.get(j).getId() + " " + items.get(j).getName());
                        //Toast.makeText(getParent(), "MainActivity - populateItemsTable(): addItemToLocation = " + locations.get(i).getId() + " " + locations.get(i).getName() + "   " + items.get(j).getId() + " " + items.get(j).getName(),
                        //Toast.LENGTH_SHORT).show();
                    }
                }
                Log.i(TAG, "MainActivity - populateItemsTable(): Items added to locations");

            }
        };

        Thread myThread = new Thread(runnable);
        myThread.start();
/*
        Toast.makeText(getParent(), "MainActivity - populateItemsTable(): Item Table Complete.  Items Table Size = " + items.size(),
                Toast.LENGTH_SHORT).show();

        Toast.makeText(getParent(), "MainActivity - populateItemsTable(): Items Table Size = " + items.size(),
                Toast.LENGTH_SHORT).show();

        if (items.size() > 0) {
            //Log.i(TAG, "MainActivity - populateItemsTable(): Items Table POPULATED");
            Toast.makeText(getParent(), "MainActivity - populateItemsTable(): Items Table POPULATED",
                    Toast.LENGTH_SHORT).show();
        } else {
            //Log.i(TAG, "MainActivity - populateItemsTable(): Items Table NOT POPULATED");
            Toast.makeText(getParent(), "MainActivity - populateItemsTable(): Items Table NOT POPULATED",
                    Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(getParent(), "MainActivity - populateItemsTable(): Items added to locations",
                Toast.LENGTH_SHORT).show();
*/    }

}