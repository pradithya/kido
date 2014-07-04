package com.progrema.superbaby.adapter.navigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.progrema.superbaby.R;

public class StandardItem extends Item {

    public StandardItem(String text) {
        this.setText(text);
        this.setLayout(R.layout.navigation_drawer_text_container);
    }

    @Override
    public View inflate(Context context, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = getLayoutInflater(context);
        view = layoutInflater.inflate(getLayout(), viewGroup, false);
        inflateName(view);
        inflateThumbnail(view);
        return view;
    }

    private LayoutInflater getLayoutInflater(Context context) {
        return (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private void inflateName(View view) {
        TextView itemName;
        itemName = (TextView) view.findViewById(R.id.text_title);
        itemName.setText(getText());
        itemName.setTextColor(getTextColor());
    }

    private void inflateThumbnail(View view) {
        ImageView thumbnail;
        thumbnail = (ImageView) view.findViewById(R.id.thumbnail_section);
        thumbnail.setImageDrawable(getThumbnail());
    }
}
