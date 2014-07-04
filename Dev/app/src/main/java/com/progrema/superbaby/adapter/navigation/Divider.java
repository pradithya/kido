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
        LayoutInflater layoutInflater = getLayoutInflater(context);
        view = layoutInflater.inflate(getLayout(), viewGroup, false);
        inflateText(view);
        return view;
    }

    private void inflateText(View view) {
        TextView actionTextView;
        actionTextView = (TextView) view.findViewById(R.id.text_title);
        actionTextView.setText(getText());
        actionTextView.setEnabled(false);
    }

    private LayoutInflater getLayoutInflater(Context context) {
        return (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
}
