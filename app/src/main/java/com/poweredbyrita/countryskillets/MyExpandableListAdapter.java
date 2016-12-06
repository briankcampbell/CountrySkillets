package com.poweredbyrita.countryskillets;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private ArrayList<Group> groups;
    public LayoutInflater inflater;
    public Activity activity;
    private ArrayList<Group> groupList;
    private ArrayList<Group> originalGroupList;
    private int lastExpandedGroupPosition;
    Cursor cursor;

    public MyExpandableListAdapter(InventoryActivity inventoryActivity, ArrayList<Group> groups) {
        this.activity = inventoryActivity;
        this.groups = groups;
        this.groupList = new ArrayList<>();
        this.groupList.addAll(groups);
        this.originalGroupList = new ArrayList<>();
        this.originalGroupList.addAll(groups);
        inflater = inventoryActivity.getLayoutInflater();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).children.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final Item item = (Item) getChild(groupPosition, childPosition);
/*
        Log.i(MainActivity.TAG, "MyExpandableListAdapter - getChildView:  itemId = " + item.getId()
                + "    itemName = " + item.getName() + "    itemGroup = " + item.getGroup().getName()
                + "    itemVendor = " + item.getVendor().getName() + "    itemLocation = "
                + item.getLocation().getId() + " - " + item.getLocation().getName());
*/
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_row_items, null);
        }

        final TextView itemName = (TextView) convertView.findViewById(R.id.lriItem);
        final TextView itemCount = (TextView) convertView.findViewById(R.id.lriCount);

        itemName.setText(item.getName());
        itemCount.setText(String.valueOf(item.getCount()));

        final MyDatabaseHelper myHelper = new MyDatabaseHelper(activity);

        itemName.requestFocus();

        final ArrayList<Vendor> vendors = myHelper.getVendors();
        final ArrayList<RestaurantLocation> locations = myHelper.getLocations();
        final View flItemEditBox = activity.findViewById(R.id.flItemEditBox);
        final View btnPlus = convertView.findViewById(R.id.btnPlus);
        final View btnMinus = convertView.findViewById(R.id.btnMinus);
        final View btnClearFields = activity.findViewById(R.id.flItemClearValuesBtn);
        final View btnAdd = activity.findViewById(R.id.flItemAddBtn);
        final View btnEdit = activity.findViewById(R.id.flItemEditBtn);
        final View btnRemove = activity.findViewById(R.id.flItemRemoveBtn);
        final View btnCancel = activity.findViewById(R.id.btnCancelEditItem);
        final EditText editName = (EditText) activity.findViewById(R.id.flItemEditName);
        final AutoCompleteTextView editGroup = (AutoCompleteTextView) activity.findViewById(R.id.flItemEditGroup);
        final AutoCompleteTextView editVendor = (AutoCompleteTextView) activity.findViewById(R.id.flItemEditVendor);
        final AutoCompleteTextView editLocation = (AutoCompleteTextView) activity.findViewById(R.id.flItemEditLocation);
        final EditText editCount = (EditText) activity.findViewById(R.id.flItemEditCount);
        final TextView lblName = (TextView) activity.findViewById(R.id.flItemEditNameLbl);
        final TextView lblVendor = (TextView) activity.findViewById(R.id.flItemEditVendorLbl);
        final TextView lblGroup = (TextView) activity.findViewById(R.id.flItemEditGroupLbl);
        final TextView lblLocation = (TextView) activity.findViewById(R.id.flItemEditLocationLbl);
        final ExpandableListView expandableItemsList = (ExpandableListView) activity.findViewById(R.id.expandableItemsList);
        final SearchView itemSearchFilter = (SearchView) activity.findViewById(R.id.itemSearchFilter);

        itemName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Item editItem = (Item) getChild(groupPosition, childPosition);
                editName.setText(editItem.getName());
                editGroup.setText(editItem.getGroup().getName());
                editVendor.setText(editItem.getVendor().getName());
                editLocation.setText(editItem.getLocation().getId() + " - "
                        + editItem.getLocation().getName());
                editCount.setText(String.valueOf(editItem.getCount()));
                editName.setSelected(true);

                // get the defined string-array
                final ArrayList<String> groupNames = new ArrayList<>();
                final ArrayList<String> vendorNames = new ArrayList<>();
                final ArrayList<String> locationNames = new ArrayList<>();
                final ArrayAdapter<String> groupNamesAdapter;
                final ArrayAdapter<String> vendorNamesAdapter;
                final ArrayAdapter<String> locationNamesAdapter;

                Log.i(MainActivity.TAG, "MyExpandableListAdapter - itemEdit: Groups" +
                        ".size() = " + groups.size());

                for(int i = 0; i < groups.size(); i++) {
                    Log.i(MainActivity.TAG, "MyExpandableListAdapter - itemEdit: " +
                            "groups.getName(" + i + ") = " + groups.get(i).getName());
                    groupNames.add(groups.get(i).getName());
                }
                groupNames.toArray();

                for(int i = 0; i < vendors.size(); i++) {
                    Log.i(MainActivity.TAG, "MyExpandableListAdapter - itemEdit: " +
                            "vendors.getName(" + i + ") = " + vendors.get(i).getName());
                    vendorNames.add(vendors.get(i).getName());
                }
                vendorNames.toArray();

                for(int i = 0; i < locations.size(); i++) {
                    Log.i(MainActivity.TAG, "MyExpandableListAdapter - itemEdit: " +
                            "locations.getName(" + i + ") = " + locations.get(i).getName());
                    locationNames.add(locations.get(i).getName());
                }
                locationNames.toArray();

                groupNamesAdapter = new ArrayAdapter<>(activity, android.R.layout
                        .simple_list_item_1, groupNames);

                vendorNamesAdapter = new ArrayAdapter<>(activity, android.R.layout
                        .simple_list_item_1, vendorNames);

                locationNamesAdapter = new ArrayAdapter<>(activity, android.R.layout
                        .simple_list_item_1, locationNames);

                // set adapter for the auto complete fields
                editGroup.setAdapter(groupNamesAdapter);
                editVendor.setAdapter(vendorNamesAdapter);
                editLocation.setAdapter(locationNamesAdapter);

                // specify the minimum type of characters before drop-down list is shown
                editGroup.setThreshold(1);
                editVendor.setThreshold(1);
                editLocation.setThreshold(1);


                editName.requestFocus();
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editName, InputMethodManager.SHOW_IMPLICIT);

                Log.i(MainActivity.TAG, "flItemEdit: ItemId = " + editItem.getId());
                Log.i(MainActivity.TAG, "flItemEdit: Name = " + editItem.getName());
                Log.i(MainActivity.TAG, "flItemEdit: Vendor = " + editItem.getVendor().getName());
                Log.i(MainActivity.TAG, "flItemEdit: Group = " + editItem.getGroup().getName());
                Log.i(MainActivity.TAG, "flItemEdit: Location = " + editItem.getLocation().getName());
                Log.i(MainActivity.TAG, "flItemEdit: Count = " + editItem.getCount());


                // change the visibility mode of the TextView
                if (flItemEditBox.getVisibility() == View.GONE) {
                    flItemEditBox.setVisibility(View.VISIBLE);
                    flItemEditBox.setBackgroundColor(Color.DKGRAY);
                    expandableItemsList.setVisibility(View.GONE);
                    btnEdit.setVisibility(View.VISIBLE);
                    btnRemove.setVisibility(View.VISIBLE);
                    btnAdd.setVisibility(View.GONE);
                    itemSearchFilter.setVisibility(View.GONE);

                } else {
                    expandableItemsList.setVisibility(View.VISIBLE);
                    flItemEditBox.setVisibility(View.GONE);
                    itemSearchFilter.setVisibility(View.VISIBLE);
                }

                btnClearFields.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editName.setText("");
                        editGroup.setText("");
                        editVendor.setText("");
                        editLocation.setText("");
                        editCount.setText("");

                        //swap visible buttons
                        btnEdit.setVisibility(View.GONE);
                        btnRemove.setVisibility(View.GONE);
                        btnClearFields.setVisibility(View.GONE);
                        btnAdd.setVisibility(View.VISIBLE);
                    }
                });

                btnAdd.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<Group> groups = myHelper.getGroups();
                        ArrayList<Item> items = myHelper.getItems();
                        ArrayList<RestaurantLocation> locations = myHelper.getLocations();
                        ArrayList<Item> locationItems;
                        Item newItem = new Item();

                        //set itemName
                        newItem.setName(String.valueOf(editName.getText()));
                        Log.i(MainActivity.TAG, "MyExpandableListAdapter: btnAdd - newItemName = " + newItem.getName());

                        //set itemGroup
                        for (int i = 0; i < groups.size(); i++) {
                            if (groups.get(i).getName().contains(editGroup.getText())){
                                newItem.setGroup(groups.get(i));
                            }
                        }
                        Log.i(MainActivity.TAG, "MyExpandableListAdapter: btnAdd - newItemGroup =" +
                                " " + newItem.getGroup().getName());

                        //set itemVendor
                        for (int i = 0; i < vendors.size(); i++) {
                            if (vendors.get(i).getName().contains(editVendor.getText().toString())){
                                newItem.setVendor(vendors.get(i));
                            }
                        }
                        Log.i(MainActivity.TAG, "MyExpandableListAdapter: btnAdd - newItemVendor " +
                                "= " + newItem.getVendor().getName());

                        //set itemLocation
                        for (int i = 0; i < locations.size(); i++) {
                            if (locations.get(i).getName().contains(editLocation.getText())) {
                                newItem.setLocation(locations.get(i));
                            }
                        }
                        Log.i(MainActivity.TAG, "MyExpandableListAdapter: btnAdd - " +
                                "newItemLocation = " + newItem.getLocation().getName());

                        //set itemCount
                        newItem.setCount(Integer.parseInt(editCount.getText().toString()));
                        Log.i(MainActivity.TAG, "MyExpandableListAdapter: btnAdd - newItemCount =" +
                                " " + newItem.getCount());

                        //check database for item
                        if (!items.contains(newItem)) {
                            myHelper.addItem(newItem);
                        }

                        //set itemId
                        if (!items.contains(newItem)) {
                            //newItem.setId(items.size());
                            myHelper.addItem(newItem);
                            newItem.setId(items.get(items.size()-1).getId());
                        } else {
                            for (int i = 0; i < items.size(); i++) {
                                if(items.get(i).getName().contains(newItem.getName())) {
                                    newItem.setId(items.get(i).getId());
                                }
                            }
                        }

                        locationItems = myHelper.getLocationItems(newItem.getLocation().getId());

                        if (!locationItems.contains(newItem)) {
                            myHelper.addItemToLocation(newItem);
                        } else {
                            Toast.makeText(activity, "Item already exists!!!",
                                    Toast.LENGTH_LONG).show();
                        }

                        locationItems = myHelper.getLocationItems(newItem.getLocation().getId());

                        if (expandableItemsList.getVisibility() == View.GONE) {
                            flItemEditBox.setVisibility(View.GONE);
                            expandableItemsList.setVisibility(View.VISIBLE);
                            editName.setVisibility(View.VISIBLE);
                            editVendor.setVisibility(View.VISIBLE);
                            editGroup.setVisibility(View.VISIBLE);
                            editLocation.setVisibility(View.VISIBLE);
                            lblName.setVisibility(View.VISIBLE);
                            lblVendor.setVisibility(View.VISIBLE);
                            lblGroup.setVisibility(View.VISIBLE);
                            lblLocation.setVisibility(View.VISIBLE);
                            btnAdd.setVisibility(View.GONE);
                            btnClearFields.setVisibility(View.VISIBLE);
                            btnEdit.setVisibility(View.VISIBLE);
                            btnRemove.setVisibility(View.VISIBLE);
                            itemSearchFilter.setVisibility(View.VISIBLE);
                            flItemEditBox.setBackgroundColor(Color.DKGRAY);
                        }

                        //Group group;
                        //TextView newItemName;
                        //TextView newItemCount;

                        for (int i = 0; i < groups.size(); i++) {
                            Log.i(MainActivity.TAG, "InventoryActivity: Item Group = " + groups.get(i).getName());
                            for (int j = 0; j < locationItems.size(); j++) {
                                if(locationItems.get(j).getGroup().getId() == groups.get(i).getId()) {
                                    groups.get(i).children.add(locationItems.get(j));
                                }
                            }
                        }

                        //groups = myHelper.getGroups();
                        //InventoryActivity.createData(locationItems.get(item.getLocation().getId()).getLocation().getId());
                        MyExpandableListAdapter adapter = new MyExpandableListAdapter((InventoryActivity) activity, groups);
                        expandableItemsList.setAdapter(adapter);

                        Toast.makeText(activity, "New Item added to LocationItems Table.  Item = "
                                        + newItem.getName()
                                        + "     Item ID = " + newItem.getId()
                                        + "     Item Group = " + newItem.getGroup().getName(),
                                Toast.LENGTH_LONG).show();

                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

                    }
                });

                btnEdit.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Item editItem = (Item) getChild(groupPosition, childPosition);

                        Log.i(MainActivity.TAG, "MyExpandableListAdapter - btnEdit (beginning): itemName = " + editItem.getName());
                        Log.i(MainActivity.TAG, "    itemID = " + editItem.getId());
                        Log.i(MainActivity.TAG, "    itemVendor = " + editItem.getVendor().getName());
                        Log.i(MainActivity.TAG, "    itemLocationID = " + editItem.getLocation().getId());
                        Log.i(MainActivity.TAG, "    itemCount = " + editItem.getCount());

                        //Set item name
                        editItem.setName(editName.getText().toString());
                        Log.i(MainActivity.TAG, "MyExpandableListAdapter - btnEdit (setName): item = " + editItem.getName());

                        //set item vendorId
                        for (int i = 0; i < vendors.size(); i++) {
                            if (vendors.get(i).getName().equals(editVendor.getText().toString())) {
                                editItem.setVendor((vendors.get(i)));
                            }
                        }
                        Log.i(MainActivity.TAG, "(setVendor)    editedItemVendor = " + editItem.getVendor().getName());

                        //set item group
                        editItem.setGroup(editItem.getGroup());

                        //set item location
                        for (int i = 0; i < locations.size(); i++) {
                            if (locations.get(i).getName().equals(editItem.getName())) {
                                editItem.setLocation(locations.get(i));
                            }
                        }
                        Log.i(MainActivity.TAG, "(setLocationId)    editedItemLocationID = " + editItem.getLocation().getId());

                        //get and set new item count
                        int newCount;
                        if (String.valueOf(editCount.getText()).equals("")) {
                            newCount = 0;
                        } else {
                            newCount = (Integer.parseInt(String.valueOf(editCount.getText())));
                        }

                        editItem.setCount(newCount);
                        Log.i(MainActivity.TAG, "(setCount)    editedItemCount = " + editItem.getCount());

                        //record item to items table
                        myHelper.editItem(editItem);

                        //record item count to location item count table
                        myHelper.editLocationItemCount(editItem);

                        //set TextViews on layout row
                        itemName.setText(editItem.getName());
                        itemCount.setText(String.valueOf(editItem.getCount()));

                        Log.i(MainActivity.TAG, "MyExpandableListAdapter - btnEdit (end): editedItemName = " + editItem.getName());
                        Log.i(MainActivity.TAG, "    editedItemID = " + editItem.getId());
                        Log.i(MainActivity.TAG, "    editedItemVendor = " + editItem.getVendor().getName());
                        Log.i(MainActivity.TAG, "    editedItemLocationID = " + editItem.getLocation().getId());
                        Log.i(MainActivity.TAG, "    editedItemCount = " + editItem.getCount());

                        //return to default display screen settings
                        if (expandableItemsList.getVisibility() == View.GONE) {
                            flItemEditBox.setVisibility(View.GONE);
                            expandableItemsList.setVisibility(View.VISIBLE);
                            editName.setVisibility(View.VISIBLE);
                            editVendor.setVisibility(View.VISIBLE);
                            editGroup.setVisibility(View.VISIBLE);
                            editLocation.setVisibility(View.VISIBLE);
                            lblName.setVisibility(View.VISIBLE);
                            lblVendor.setVisibility(View.VISIBLE);
                            lblGroup.setVisibility(View.VISIBLE);
                            lblLocation.setVisibility(View.VISIBLE);
                            btnAdd.setVisibility(View.GONE);
                            btnClearFields.setVisibility(View.VISIBLE);
                            btnRemove.setVisibility(View.VISIBLE);
                            itemSearchFilter.setVisibility(View.VISIBLE);
                            flItemEditBox.setBackgroundColor(Color.DKGRAY);
                        }

                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

                        notifyDataSetChanged();

                    }
                });

                btnRemove.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Group group = groups.get(groupPosition);
                        Item child = group.getChildren().get(childPosition);
                        //Item removeItem = (Item) getChild(groupPosition, childPosition);
                        myHelper.removeItemFromLocation(child);
                        /*for (int i = 0; i < myHelper.getLocations().size(); i++) {
                            if (!myHelper.getLocationItems(i).contains(myHelper.getItems().get(child.getId()))) {
                                myHelper.removeItem(child);
                            }
                        }
*/
                        group.children.remove(child);

                        if (expandableItemsList.getVisibility() == View.GONE) {
                            flItemEditBox.setVisibility(View.GONE);
                            expandableItemsList.setVisibility(View.VISIBLE);
                            editName.setVisibility(View.VISIBLE);
                            editVendor.setVisibility(View.VISIBLE);
                            editGroup.setVisibility(View.VISIBLE);
                            editLocation.setVisibility(View.VISIBLE);
                            lblName.setVisibility(View.VISIBLE);
                            lblVendor.setVisibility(View.VISIBLE);
                            lblGroup.setVisibility(View.VISIBLE);
                            lblLocation.setVisibility(View.VISIBLE);
                            btnAdd.setVisibility(View.GONE);
                            btnClearFields.setVisibility(View.VISIBLE);
                            btnRemove.setVisibility(View.VISIBLE);
                            itemSearchFilter.setVisibility(View.VISIBLE);
                            flItemEditBox.setBackgroundColor(Color.DKGRAY);
                        }

                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

                        notifyDataSetChanged();

                    }
                });

                btnCancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (expandableItemsList.getVisibility() == View.GONE) {
                            flItemEditBox.setVisibility(View.GONE);
                            expandableItemsList.setVisibility(View.VISIBLE);
                            editName.setVisibility(View.VISIBLE);
                            editVendor.setVisibility(View.VISIBLE);
                            editGroup.setVisibility(View.VISIBLE);
                            editLocation.setVisibility(View.VISIBLE);
                            lblName.setVisibility(View.VISIBLE);
                            lblVendor.setVisibility(View.VISIBLE);
                            lblGroup.setVisibility(View.VISIBLE);
                            lblLocation.setVisibility(View.VISIBLE);
                            btnAdd.setVisibility(View.GONE);
                            btnClearFields.setVisibility(View.VISIBLE);
                            btnRemove.setVisibility(View.VISIBLE);
                            itemSearchFilter.setVisibility(View.VISIBLE);
                            flItemEditBox.setBackgroundColor(Color.DKGRAY);
                        }
                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

                    }
                });

                notifyDataSetChanged();
                return false;
            }
        });

        itemCount.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i(MainActivity.TAG, "MyExpandableListAdapter - itemCount: itemName = " + item.getName());
                Log.i(MainActivity.TAG, "    itemID = " + item.getId());
                Log.i(MainActivity.TAG, "    itemVendor = " + item.getVendor().getName());
                Log.i(MainActivity.TAG, "    itemLocation = " + item.getLocation().getName());
                Log.i(MainActivity.TAG, "    itemCount = " + item.getCount());

                if (flItemEditBox.getVisibility() == View.GONE) {
                    editName.setText(item.getName());
                    editVendor.setText(item.getVendor().getName());
                    editGroup.setText(item.getGroup().getName());
                    editLocation.setText(item.getLocation().getName());
                    editCount.setText(String.valueOf(item.getCount()));
                    editName.setVisibility(View.GONE);
                    editVendor.setVisibility(View.GONE);
                    editGroup.setVisibility(View.GONE);
                    editLocation.setVisibility(View.GONE);
                    lblName.setVisibility(View.GONE);
                    lblVendor.setVisibility(View.GONE);
                    lblGroup.setVisibility(View.GONE);
                    lblLocation.setVisibility(View.GONE);
                    btnAdd.setVisibility(View.GONE);
                    btnClearFields.setVisibility(View.GONE);
                    btnRemove.setVisibility(View.GONE);
                    flItemEditBox.setVisibility(View.VISIBLE);
                    flItemEditBox.setBackgroundColor(Color.DKGRAY);
                    expandableItemsList.setVisibility(View.GONE);
                    itemSearchFilter.setVisibility(View.GONE);

                    editCount.requestFocus();
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editCount, InputMethodManager.SHOW_IMPLICIT);

                }

                btnEdit.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(MainActivity.TAG, "MyExpandableListAdapter - btnEdit (beginning): itemName = " + item.getName());
                        Log.i(MainActivity.TAG, "    itemID = " + item.getId());
                        Log.i(MainActivity.TAG, "    itemVendor = " + item.getVendor().getName());
                        Log.i(MainActivity.TAG, "    itemLocationName = " + item.getLocation().getName());
                        Log.i(MainActivity.TAG, "    itemCount = " + item.getCount());

                        //get and set new item count
                        int newCount;
                        if (String.valueOf(editCount.getText()).equals("")) {
                            newCount = 0;
                        } else {
                            newCount = (Integer.parseInt(String.valueOf(editCount.getText())));
                        }

                        item.setCount(newCount);
                        Log.i(MainActivity.TAG, "(setCount)    editedItemCount = " + item.getCount());

                        //record item count to location item count table
                        myHelper.editLocationItemCount(item);

                        //set TextViews on layout row
                        itemName.setText(item.getName());
                        itemCount.setText(String.valueOf(item.getCount()));

                        Log.i(MainActivity.TAG, "MyExpandableListAdapter - btnEdit (end): editedItemName = " + item.getName());
                        Log.i(MainActivity.TAG, "    editedItemID = " + item.getId());
                        Log.i(MainActivity.TAG, "    editedItemVendor = " + item.getVendor().getName());
                        Log.i(MainActivity.TAG, "    editedItemLocationName = " + item.getLocation().getName());
                        Log.i(MainActivity.TAG, "    editedItemCount = " + item.getCount());

                        //return to default display screen settings
                        if (expandableItemsList.getVisibility() == View.GONE) {
                            flItemEditBox.setVisibility(View.GONE);
                            expandableItemsList.setVisibility(View.VISIBLE);
                            editName.setVisibility(View.VISIBLE);
                            editVendor.setVisibility(View.VISIBLE);
                            editGroup.setVisibility(View.VISIBLE);
                            editLocation.setVisibility(View.VISIBLE);
                            lblName.setVisibility(View.VISIBLE);
                            lblVendor.setVisibility(View.VISIBLE);
                            lblGroup.setVisibility(View.VISIBLE);
                            lblLocation.setVisibility(View.VISIBLE);
                            btnAdd.setVisibility(View.GONE);
                            btnClearFields.setVisibility(View.VISIBLE);
                            btnRemove.setVisibility(View.VISIBLE);
                            itemSearchFilter.setVisibility(View.VISIBLE);
                            flItemEditBox.setBackgroundColor(Color.DKGRAY);
                        }

                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

                        notifyDataSetChanged();
                        expandableItemsList.deferNotifyDataSetChanged();

                    }
                });

                btnCancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (expandableItemsList.getVisibility() == View.GONE) {
                            flItemEditBox.setVisibility(View.GONE);
                            expandableItemsList.setVisibility(View.VISIBLE);
                            editName.setVisibility(View.VISIBLE);
                            editVendor.setVisibility(View.VISIBLE);
                            editGroup.setVisibility(View.VISIBLE);
                            editLocation.setVisibility(View.VISIBLE);
                            lblName.setVisibility(View.VISIBLE);
                            lblVendor.setVisibility(View.VISIBLE);
                            lblGroup.setVisibility(View.VISIBLE);
                            lblLocation.setVisibility(View.VISIBLE);
                            btnAdd.setVisibility(View.GONE);
                            btnClearFields.setVisibility(View.VISIBLE);
                            btnRemove.setVisibility(View.VISIBLE);
                            itemSearchFilter.setVisibility(View.VISIBLE);
                            flItemEditBox.setBackgroundColor(Color.DKGRAY);
                        }

                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

                    }
                });

                notifyDataSetChanged();
                InventoryActivity.expandableListView.deferNotifyDataSetChanged();

                return false;
            }
        });

        btnPlus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(MainActivity.TAG, "MyExpandableListAdapter - btnPlus: itemName = " + item.getName());
                Log.i(MainActivity.TAG, "    itemID = " + item.getId());
                Log.i(MainActivity.TAG, "    itemVendor = " + item.getVendor().getName());
                Log.i(MainActivity.TAG, "    itemLocationName = " + item.getLocation().getName());
                Log.i(MainActivity.TAG, "    itemCount = " + item.getCount());

                int count = item.getCount();
                count++;
                item.setCount(count);
                myHelper.editLocationItemCount(item);

                Toast.makeText(activity, item.getName() + "    Count = " + item.getCount(),
                        Toast.LENGTH_SHORT).show();

                itemCount.setText(String.valueOf(item.getCount()));
            }
        });

        btnMinus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(MainActivity.TAG, "MyExpandableListAdapter - btnMinus: itemName = " + item.getName());
                Log.i(MainActivity.TAG, "    itemID = " + item.getId());
                Log.i(MainActivity.TAG, "    itemVendor = " + item.getVendor().getName());
                Log.i(MainActivity.TAG, "    itemLocationName = " + item.getLocation().getName());
                Log.i(MainActivity.TAG, "    itemCount = " + item.getCount());

                int count = item.getCount();
                count--;
                if(count < 0){
                    count = 0;
                }
                item.setCount(count);
                myHelper.editLocationItemCount(item);

                Toast.makeText(activity, item.getName() + "    Count = " + item.getCount(),
                        Toast.LENGTH_SHORT).show();

                itemCount.setText(String.valueOf(item.getCount()));
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).children.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }
    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        //collapse the old expanded group, if not the same
        //as new group to expand
        if (InventoryActivity.groups.size() == InventoryActivity.originalGroupList.size()) {
            if (groupPosition != lastExpandedGroupPosition) {
                InventoryActivity.expandableListView.collapseGroup(lastExpandedGroupPosition);

                lastExpandedGroupPosition = groupPosition;

            }
        }
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_group, null);
        }
        final Group group = (Group) getGroup(groupPosition);
        ((CheckedTextView) convertView).setText(group.name);
        ((CheckedTextView) convertView).setChecked(isExpanded);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public ArrayList<Group> filterData(String query){

        query = query.toLowerCase();
        Log.v(MainActivity.TAG, String.valueOf(getGroupCount()));
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
                if(newList.size() > 0){
                    Group nGroup = new Group(group.getName(), newList);
                    groups.add(nGroup);
                }
            }
        }

        Log.v(MainActivity.TAG, "Groups size: " + String.valueOf(getGroupCount()));

        notifyDataSetChanged();

        return groups;

    }
}