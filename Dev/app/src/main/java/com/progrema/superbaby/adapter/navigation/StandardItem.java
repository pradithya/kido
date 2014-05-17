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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(getLayout(), viewGroup, false);

        TextView tvAction;
        tvAction = (TextView) view.findViewById(R.id.text_title);
        tvAction.setText(getText());
        tvAction.setTextColor(getTextColor());

        ImageView ivThumbnail;
        ivThumbnail = (ImageView) view.findViewById(R.id.thumbnail_section);
        ivThumbnail.setImageDrawable(getThumbnail());

        return view;
    }
}
