package com.poweredbyrita.countryskillets;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;


public class VendorsActivity extends Activity {
    ListView vendorListView;
    CustomAdapterVendor arrayAdapterVendor;
    private ArrayList<Vendor> vendors;
    private MyDatabaseHelper myHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendors);

        myHelper = new MyDatabaseHelper(this);
        SQLiteDatabase db = myHelper.getWritableDatabase();

        vendors = myHelper.getVendors();

        populateVendorsList();
    }

    private void populateVendorsList() {
        vendorListView = (ListView) findViewById(R.id.vendorsList);
        arrayAdapterVendor = new CustomAdapterVendor(this, R.layout.layout_row_vendors, vendors);
        vendorListView.setAdapter(arrayAdapterVendor);
    }


}
