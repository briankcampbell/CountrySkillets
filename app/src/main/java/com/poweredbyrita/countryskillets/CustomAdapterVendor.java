package com.poweredbyrita.countryskillets;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Biarre on 7/10/2015.
 */
public class CustomAdapterVendor extends ArrayAdapter<Vendor> {
    Context context;
    int layoutResourceId;
    ArrayList<Vendor> vendors = null;
    public VendorsActivity vendorsActivity;
    public Activity activity = vendorsActivity;
    
    public CustomAdapterVendor(Context context, int layoutResourceId, ArrayList<Vendor> vendors){
        super(context, layoutResourceId, vendors);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.vendors = vendors;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        LocationHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new LocationHolder();
            holder.textEntryVendorName = (TextView)row.findViewById(R.id.vlrName);
            holder.textEntryVendorPhone = (TextView)row.findViewById(R.id.vlrPhone);

            row.setTag(holder);
        }
        else
        {
            holder = (LocationHolder)row.getTag();
        }

        final Vendor vendor = vendors.get(position);

        String strPhone = String.valueOf(vendor.getPhone());

        holder.textEntryVendorName.setText(vendor.getName());
        holder.textEntryVendorPhone.setText(strPhone);
/*
        //setOnclickListeners
        final MyDatabaseHelper myHelper = new MyDatabaseHelper(context);

        //final TextView vendorName = (TextView) convertView.findViewById(R.id.vlrName);
        //final TextView vendorPhone = (TextView) convertView.findViewById(R.id.vlrPhone);

        holder.textEntryVendorName.setText(vendor.getName());
        holder.textEntryVendorPhone.setText(String.valueOf(vendor.getPhone()));

        final View flistEditVendorBox = activity.findViewById(R.id.flistEditVendorBox);
        final View btnClear = activity.findViewById(R.id.flistEditVendorClearBtn);
        final View btnAdd = activity.findViewById(R.id.flistEditVendorAddBtn);
        final View btnEdit = activity.findViewById(R.id.flistEditVendorEditBtn);
        final View btnRemove = activity.findViewById(R.id.flistEditVendorRemoveBtn);
        final View btnCancel = activity.findViewById(R.id.flistEditVendorCancelBtn);
        final EditText editVendorName = (EditText) activity.findViewById(R.id.flistEditVendorName);
        final EditText editVendorPhone = (EditText) activity.findViewById(R.id.flistEditVendorPhone);
        final ListView vendorsList = (ListView) activity.findViewById(R.id.vendorsList);

        convertView.setOnLongClickListener(new View.OnLongClickListener() {


            @Override
            public boolean onLongClick(View v) {
                editVendorName.setText(vendor.getName());
                editVendorPhone.setText(String.valueOf(vendor.getPhone()));

                Log.i(MainActivity.TAG, "flistEditVendor: VendorId = " + vendor.getId());
                Log.i(MainActivity.TAG, "flistEditVendor: Name = " + vendor.getName());
                Log.i(MainActivity.TAG, "flistEditVendor: Phone = " + vendor.getPhone());

                // change the visibility mode of the TextView
                if (flistEditVendorBox.getVisibility() == View.GONE) {
                    flistEditVendorBox.setVisibility(View.VISIBLE);
                    flistEditVendorBox.setBackgroundColor(Color.DKGRAY);
                    vendorsList.setVisibility(View.GONE);


                } else {
                    vendorsList.setVisibility(View.VISIBLE);
                    flistEditVendorBox.setVisibility(View.GONE);
                }

                return false;
            }
        });
*/
        return row;
    }

    static class LocationHolder
    {
        TextView textEntryVendorName;
        TextView textEntryVendorPhone;

    }

}
