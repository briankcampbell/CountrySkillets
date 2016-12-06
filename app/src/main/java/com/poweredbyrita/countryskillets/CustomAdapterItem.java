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
public class CustomAdapterItem extends ArrayAdapter<Item> {
    Context context;
    int layoutResourceId;
    ArrayList<Item> data = null;

    public CustomAdapterItem(Context context, int layoutResourceId, ArrayList<Item> data){
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        ItemHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ItemHolder();
            holder.textEntryItemName = (TextView)row.findViewById(R.id.lriItem);
            holder.textEntryItemCount = (TextView)row.findViewById(R.id.lriCount);
            row.setTag(holder);
        }
        else
        {
            holder = (ItemHolder)row.getTag();
        }

        Item item = data.get(position);
        int count = item.getCount();

        holder.textEntryItemName.setText(item.getName());
        holder.textEntryItemCount.setText(String.valueOf(count));

        return row;
    }

    static class ItemHolder
    {
        TextView textEntryItemName;
        TextView textEntryItemCount;

        View imgEntryLocationImage;
        TextView textEntryLocationName;

        TextView textEntryItemVendorName;
        TextView textEntryItemVendorPhone;
    }

}
