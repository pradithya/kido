package com.progrema.superbaby.adapter.navigationdrawer;

import android.view.View;
import android.widget.TextView;

import com.progrema.superbaby.R;

/**
 * Created by iqbalpakeh on 15/2/14.
 */
public class BabyNameItem extends Item
{
    public BabyNameItem(String text)
    {
        this.setText(text);
        this.setLayout(R.layout.navigation_drawer_baby_item);
    }

    /**
     * inflate view
     *
     * @param view parent view
     */
    @Override
    public void inflate(View view)
    {
        TextView babyNameTextView;
        babyNameTextView = (TextView) view.findViewById(R.id.baby_name_type_view);
        babyNameTextView.setText(getText());
    }
}
