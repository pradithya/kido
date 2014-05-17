package com.progrema.superbaby.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.progrema.superbaby.R;

/**
 * Created by aria on 17/5/14.
 */
public class ActionBarDropDownAdapter extends ArrayAdapter<String>{


    // CUSTOM SPINNER ADAPTER
    private Context mContext;
    private String[] texts;
    private int viewId;
    private String title;
    public ActionBarDropDownAdapter(Context context, int textViewResourceId,
                            String[] objects, String title) {
        super(context, textViewResourceId, objects);

        mContext = context;
        texts = objects;
        viewId = textViewResourceId;
        this.title = title;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        DropDownViewHolder holder = null;

        if (convertView == null)
        {
            LayoutInflater inflater =
                    ( LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);

            holder = new DropDownViewHolder();
            holder.mTitle = (TextView) convertView.findViewById(android.R.id.text1);

            convertView.setTag(holder);
        }
        else
        {
            holder = (DropDownViewHolder) convertView.getTag();
        }

        // Should have some sort of data set to go off of, we'll assume
        // there is a some array called mData.
        holder.mTitle.setText(texts[position]);

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView,ViewGroup parent) {
        // TODO Auto-generated method stub
        // return super.getView(position, convertView, parent);


        LayoutInflater inflater =
                ( LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(viewId, null);
            holder = new ViewHolder();
            holder.txt01 = (TextView) convertView.findViewById(R.id.title);
            holder.txt02 = (TextView) convertView.findViewById(R.id.subtitle);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt01.setText(title);
        holder.txt02.setText(texts[position].toUpperCase());

        return convertView;
    }

    public void setTitle(String title)
    {
        this.title = title;
        this.notifyDataSetChanged();
    }

    class ViewHolder {
        TextView txt01;
        TextView txt02;
    }

    public class DropDownViewHolder {
        TextView mTitle;
    }


}
