package com.progrema.superbaby.adapter.actionbar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.progrema.superbaby.R;

public class ActionBarDropDownAdapter extends ArrayAdapter<String> {

    private Context context;
    private String[] texts;
    private String title;
    private int viewId;

    public ActionBarDropDownAdapter(Context context,
                                    int textViewResourceId, String[] objects, String title) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.texts = objects;
        this.viewId = textViewResourceId;
        this.title = title;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        DropDownViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
            holder = new DropDownViewHolder();
            holder.title = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (DropDownViewHolder) convertView.getTag();
        }
        holder.title.setText(texts[position]);
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        LayoutInflater layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = layoutInflater.inflate(viewId, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.subtitle = (TextView) convertView.findViewById(R.id.subtitle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(title);
        viewHolder.subtitle.setText(texts[position].toUpperCase());
        return convertView;
    }

    public void setTitleAndNotify(String title) {
        this.title = title;
        this.notifyDataSetChanged();
    }

    private class ViewHolder {
        private TextView title;
        private TextView subtitle;
    }

    public class DropDownViewHolder {
        private TextView title;
    }
}
