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
public class CustomAdapterLocation extends ArrayAdapter<RestaurantLocation> {
    Context context;
    int layoutResourceId;
    ArrayList<RestaurantLocation> locations;

    public CustomAdapterLocation(Context context, int layoutResourceId, ArrayList<RestaurantLocation> locations){
        super(context, layoutResourceId, locations);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.locations = locations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        LocationHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new LocationHolder();
            holder.imgEntryLocationImage = row.findViewById(R.id.llrLocationImage);
            holder.textEntryLocationName = (TextView)row.findViewById(R.id.llrLocationName);

            row.setTag(holder);
        } else {
            holder = (LocationHolder)row.getTag();
        }

        RestaurantLocation location = locations.get(position);

        holder.imgEntryLocationImage.setBackgroundResource(location.getImageId());
        holder.textEntryLocationName.setText(location.getName());
/*
        Log.i(MainActivity.TAG, "CustomAdapterLocation - Location Name: " + location.getName() +
                "Location " + "locationID: " + location.getId());
*/        return row;
    }

    static class LocationHolder
    {
        View imgEntryLocationImage;
        TextView textEntryLocationName;

    }

}
