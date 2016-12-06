package com.poweredbyrita.countryskillets;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

public class InventoryActivity extends Activity implements
    SearchView.OnQueryTextListener, SearchView.OnCloseListener {
    public static final String TAG = MainActivity.TAG;
    private static MyDatabaseHelper myHelper;
    public static ExpandableListView expandableListView;
    private static MyExpandableListAdapter adapter;
    private static ArrayList<RestaurantLocation> locations;
    private ArrayList<Item> items;
    private static ArrayList<Item> locationItems;
    private ArrayList<Vendor> vendors;
    private ArrayList<String> itemGroups;
    private View summary;
    private SearchView search;

    // more efficient than HashMap for mapping integers to objects
    static ArrayList<Group> originalGroupList;
    static ArrayList<Group> groups;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        myHelper = new MyDatabaseHelper(this);

        locations = myHelper.getLocations();
        items = myHelper.getItems();
        groups = myHelper.getGroups();
        vendors = myHelper.getVendors();
        summary = findViewById(R.id.btnSummary);
        originalGroupList = new ArrayList<>();
        originalGroupList.addAll(groups);

        // 1. get passed intent
        Intent intent = getIntent();
        int locId = locations.get(intent.getIntExtra("locId", 0)).getId();
        String locName = intent.getStringExtra("locName");
        itemGroups = intent.getStringArrayListExtra("itemGroups");

        //Log.i(TAG, "Inventory Activity: Location Name = " + locName + "    locId = " + locId);

        // 2. Display Location Name below Activity Title
        TextView t1 = (TextView) findViewById(R.id.inventoryLocation);
        t1.setText(locName);

        populateExpandableItemsList(locId);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search = (SearchView) findViewById(R.id.itemSearchFilter);
        search.setBackgroundColor(Color.WHITE);
        search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        search.setIconifiedByDefault(false);
        search.setOnQueryTextListener(this);
        search.setOnCloseListener(this);
        search.setSelected(false);


        InputMethodManager imm = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(search.getWindowToken(), 0);


/*        Runnable runnable = new Runnable() {
            public void run() {
                summary.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void onClick(View v) {
                        final FrameLayout frameReport = (FrameLayout) findViewById(R.id.frameReport);
                        final FrameLayout flistItemEdit = (FrameLayout) findViewById(R.id.flistItemEditFrame);
                        ArrayList<LinearLayout> itemRows = new ArrayList<>();
                        ArrayList<TextView> itemNames = new ArrayList<>();
                        ArrayList<TextView> itemCounts = new ArrayList<>();
                        ArrayList<Item> reportItems = new ArrayList<>();

                        LinearLayout itemRow;
                        TextView itemGroup;
                        TextView itemName;
                        TextView itemCount;

                        frameReport.setVisibility(View.VISIBLE);
//                        frameReport.setBackgroundColor(Color.DKGRAY);


                        for (int i = 0; i < groups.size(); i++) {
                            //listSummary.expandGroup(i);
                            itemRow = new LinearLayout(frameReport.getContext());
                            itemGroup = new TextView(itemRow.getContext());
                            itemGroup.setText(groups.get(i).getName());
                            itemGroup.setTextSize(18);
                            itemGroup.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            itemRow.addView(itemGroup);
                            itemRows.add(itemRow);

                            frameReport.removeView(itemRows.get(i));
                            frameReport.addView(itemRows.get(i));

                            Log.i(MainActivity.TAG, "InventoryActivity - frameReport: Item Row " + i);

                            for (int j = 0; j < groups.get(i).getChildren().size(); j++) {
                                itemName = new TextView(itemRow.getContext());
                                itemName.setText(groups.get(i).getChildren().get(j).getName());
                                itemName.setTextSize(12);
                                itemName.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

                                itemCount = new TextView(itemRow.getContext());
                                itemCount.setText(String.valueOf(groups.get(i).getChildren().get(j).getCount()));
                                itemCount.setTextSize(12);
                                itemCount.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

                                itemRow.setOrientation(LinearLayout.HORIZONTAL);
                                itemRow.addView(itemName);
                                itemRow.addView(itemCount);
                                itemRow.setWeightSum(2);

                                itemRows.add(itemRow);

                                frameReport.removeView(itemRows.get(j));
                                frameReport.addView(itemRows.get(j));
                                //Log.i(MainActivity.TAG, "InventoryActivity - frameReport: Item Row " + j + "  itemName = " + itemName.getText() + "     itemCount = " + itemCount.getText());

                            }

                        }

                        Log.i(MainActivity.TAG, "InventoryActivity - frameReport: itemRows size = " + itemRows.size());
                        for (int i = 0; i < itemRows.size(); i++) {
                            //frameReport.removeView(itemRows.get(i));
                            //frameReport.addView(itemRows.get(i));
                            //Log.i(MainActivity.TAG, "InventoryActivity - frameReport: Row " + itemRows.get(i).getId() + " added.");
                        }

                        frameReport.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LinearLayout inventoryContent = (LinearLayout) findViewById(R.id.inventoryContent);
                                frameReport.removeAllViews();
                                frameReport.addView(inventoryContent);
                            }
                        });

                    }

                });
            }
        };

        Thread myThread = new Thread(runnable);
        myThread.start();

*/
    }

    private void populateExpandableItemsList(int locId){
        createData(locId);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableItemsList);
        adapter = new MyExpandableListAdapter(this, groups);
        expandableListView.setAdapter(adapter);


    }

     //method to expand all groups
     public static boolean expandAll() {
        int count = adapter.getGroupCount();
        for (int i = 0; i < count; i++){
            expandableListView.expandGroup(i);
        }
         return true;
     }

    //method to collapse all groups
    public void collapseAll() {
        int count = adapter.getGroupCount();
        for (int i = 0; i < count; i++){
            expandableListView.collapseGroup(i);
        }
    }

    //@TargetApi(Build.VERSION_CODES.KITKAT)
    public static void createData(int locId) {
        //Log.i(TAG, "     ");
        //Log.i(TAG, "     ");
        Log.i(TAG, "InventoryActivity - createData: locId = " + (locId) + ": " + locations.get
                (locId).getName());

        locationItems = myHelper.getLocationItems(locId);

        for (int i = 0; i < groups.size(); i++) {
            Log.i(TAG, "InventoryActivity: Item Group = " + groups.get(i).getName());
            for (int j = 0; j < locationItems.size(); j++) {
                if(locationItems.get(j).getGroup().getId() == groups.get(i).getId()) {
                    groups.get(i).children.add(locationItems.get(j));
                }
            }
        }

    }

    @Override
    public boolean onClose() {
        adapter.filterData("");
        collapseAll();

        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        expandAll();
        query = query.toLowerCase();
        Log.v(MainActivity.TAG, String.valueOf(groups.size()));
        groups.clear();

        if(query.isEmpty()){
            groups.addAll(originalGroupList);
        }
        else {

            for(Group group: originalGroupList){

                ArrayList<Item> itemList = group.getChildren();
                ArrayList<Item> newList = new ArrayList<>();
                for(Item item: itemList){
                    if(item.getName().toLowerCase().contains(query) ||
                            item.getName().toLowerCase().contains(query)){
                        newList.add(item);
                    }
                }

                Log.v(MainActivity.TAG, "Group: " + group.getName());

                if(newList.size() > 0){
                    Group nGroup = new Group(group.getName(), newList);
                    nGroup.setName(group.getName());
                    nGroup.getChildren().addAll(newList);
                    groups.add(nGroup);

                    Log.v(MainActivity.TAG, "nGroup: " + nGroup.getName());
                    for(int i = 0; i < nGroup.getChildren().size(); i++) {
                        Log.v(MainActivity.TAG, "Item: " + nGroup.getChildren().get(i).getName());
                    }
                }
            }
        }

        Log.v(MainActivity.TAG, "Groups size: " + String.valueOf(groups.size()));

        adapter.notifyDataSetChanged();
        expandableListView.deferNotifyDataSetChanged();
        expandableListView.setAdapter(adapter);
        expandAll();

        if(groups.size() == originalGroupList.size()){
            collapseAll();
        } else {
            expandAll();
        }

        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        query = query.toLowerCase();
        Log.v(MainActivity.TAG, String.valueOf(groups.size()));
        groups.clear();

        if(query.isEmpty()){
            groups.addAll(originalGroupList);
        }
        else {

            for(Group group: originalGroupList){

                ArrayList<Item> itemList = group.getChildren();
                ArrayList<Item> newList = new ArrayList<>();
                for(Item item: itemList){
                    if(item.getName().toLowerCase().contains(query)){
                        newList.add(item);
                    }
                }

                Log.v(MainActivity.TAG, "Group: " + group.getName());

                if(newList.size() > 0){
                    Group nGroup = new Group(group.getName(), newList);
                    nGroup.setName(group.getName());
                    nGroup.getChildren().addAll(newList);
                    groups.add(nGroup);

                    Log.v(MainActivity.TAG, "nGroup: " + nGroup.getName());
                    for(int i = 0; i < nGroup.getChildren().size(); i++) {
                        Log.v(MainActivity.TAG, "Item: " + nGroup.getChildren().get(i).getName());
                    }
                }
            }
        }

        Log.v(MainActivity.TAG, "Groups size: " + String.valueOf(groups.size()));

        adapter.notifyDataSetChanged();
        expandableListView.deferNotifyDataSetChanged();
        expandableListView.setAdapter(adapter);

        if(groups.size() == originalGroupList.size()){
            collapseAll();
        } else {
            expandAll();
        }

        return false;
    }

}