package com.progrema.superbaby.adapter.navigation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class NavigationAdapter extends ArrayAdapter<Item> {
    /**
     * private field
     */
    private ArrayList<Item> items;
    private Context context;

    /**
     * adapter constructor
     *
     * @param context application context
     * @param items   items holding list of Item object
     */
    public NavigationAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
        this.items = items;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return items.get(position).inflate(context, convertView, parent);
    }
}
