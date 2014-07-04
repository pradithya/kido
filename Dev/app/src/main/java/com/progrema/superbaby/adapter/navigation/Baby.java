package com.progrema.superbaby.adapter.navigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.progrema.superbaby.R;
import com.progrema.superbaby.util.ActiveContext;

public class Baby extends Item {

    public Baby(String text) {
        this.setText(text);
        this.setLayout(R.layout.navigation_drawer_baby_item);
    }

    @Override
    public View inflate(Context context, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = getLayoutInflater(context);
        view = layoutInflater.inflate(getLayout(), viewGroup, false);
        inflateName(view);
        inflateThumbnail(view);
        inflateStatusFlag(context, view);
        return view;
    }

    private void inflateName(View view) {
        TextView name;
        name = (TextView) view.findViewById(R.id.baby_name_type_view);
        name.setText(getText());
    }

    private void inflateThumbnail(View view) {
        ImageView thumbnail;
        thumbnail = (ImageView) view.findViewById(R.id.thumbnail_section);
        thumbnail.setImageDrawable(getThumbnail());
    }

    private void inflateStatusFlag(Context context, View view) {
        RadioButton statusFlag;
        statusFlag = (RadioButton) view.findViewById(R.id.active_baby_flag);
        if (ActiveContext.getActiveBaby(context).getName().equals(getText())) {
            statusFlag.setChecked(true);
        } else {
            statusFlag.setChecked(false);
        }
    }

    private LayoutInflater getLayoutInflater(Context context) {
        return (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
}
