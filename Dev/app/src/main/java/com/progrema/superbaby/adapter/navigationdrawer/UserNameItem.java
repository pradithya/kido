package com.progrema.superbaby.adapter.navigationdrawer;

import android.view.View;
import android.widget.TextView;

import com.progrema.superbaby.R;

/**
 * Created by iqbalpakeh on 15/2/14.
 */
public class UserNameItem extends Item
{
    public UserNameItem(String text)
    {
        this.setText(text);
        this.setLayout(R.layout.navigation_drawer_user_item);
    }

    /**
     * inflate view
     *
     * @param view parent view
     */
    @Override
    public void inflate(View view)
    {
        TextView userNameTextView;
        userNameTextView = (TextView) view.findViewById(R.id.user_name_type_view);
        userNameTextView.setText(getText());
    }
}
