package com.progrema.superbaby.adapter.navigationdrawer;

import android.view.View;
import android.widget.TextView;

import com.progrema.superbaby.R;

/**
 * Created by iqbalpakeh on 15/2/14.
 */
public class SectionItem extends Item
{
    public SectionItem(String text)
    {
        this.setText(text);
        this.setLayout(R.layout.navigation_drawer_section_item);
    }

    /**
     * inflate view
     *
     * @param view parent view
     */
    @Override
    public void inflate(View view)
    {
        TextView sectionTextView;
        sectionTextView = (TextView) view.findViewById(R.id.section_type_view);
        sectionTextView.setText(getText());
    }
}
