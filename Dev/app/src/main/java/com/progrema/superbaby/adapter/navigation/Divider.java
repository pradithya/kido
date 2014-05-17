package com.progrema.superbaby.adapter.navigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.progrema.superbaby.R;

public class Divider extends Item {

    public Divider(String text) {
        this.setText(text);
        this.setLayout(R.layout.navigation_drawer_section_item);
    }

    @Override
    public View inflate(Context context, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(getLayout(), viewGroup, false);

        TextView actionTextView;
        actionTextView = (TextView) view.findViewById(R.id.text_title);
        actionTextView.setText(getText());
        actionTextView.setEnabled(false);

        return view;
    }
}
