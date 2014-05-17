package com.progrema.superbaby.adapter.navigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(getLayout(), viewGroup, false);

        TextView baby;
        baby = (TextView) view.findViewById(R.id.baby_name_type_view);
        baby.setText(getText());

        RadioButton activeFlag;
        activeFlag = (RadioButton) view.findViewById(R.id.active_baby_flag);
        if (ActiveContext.getActiveBaby(context).getName().equals(getText())) {
            activeFlag.setChecked(true);
        } else {
            activeFlag.setChecked(false);
        }

        return view;
    }
}
