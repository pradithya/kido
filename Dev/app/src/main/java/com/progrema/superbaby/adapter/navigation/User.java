package com.progrema.superbaby.adapter.navigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.progrema.superbaby.R;

/**
 * Created by iqbalpakeh on 15/2/14.
 */
public class User extends Item
{
    public User(String text)
    {
        this.setText(text);
        this.setLayout(R.layout.navigation_drawer_user_item);
    }

    /**
     * inflate view
     *
     * @param context application context
     * @param view view to inflate
     * @param viewGroup parent view
     * @return inflated view
     */
    @Override
    public View inflate(Context context, View view, ViewGroup viewGroup)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(getLayout(), viewGroup, false);

        TextView userNameTextView;
        userNameTextView = (TextView) view.findViewById(R.id.user_name_type_view);
        userNameTextView.setText(getText());

        return view;
    }
}
