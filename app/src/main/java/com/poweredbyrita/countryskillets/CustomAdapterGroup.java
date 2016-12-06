package com.poweredbyrita.countryskillets;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Biarre on 8/1/2015.
 */
public class CustomAdapterGroup {
    Context context;
    int layoutResourceId;
    ArrayList<String> categories;

    public CustomAdapterGroup(Context context, int layoutResourceId, ArrayList<String> categories) {
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.categories = categories;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        GroupHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new GroupHolder();
            holder.radioEntryGroupName = (TextView)row.findViewById(R.id.lrcGroupName);

            row.setTag(holder);
        }
        else
        {
            holder = (GroupHolder)row.getTag();
        }

        String group = categories.get(position);

        holder.radioEntryGroupName.setText(group);

        Log.i(MainActivity.TAG, "Group Name: " + group);
        return row;
    }

    static class GroupHolder
    {
        TextView radioEntryGroupName;

    }

}
