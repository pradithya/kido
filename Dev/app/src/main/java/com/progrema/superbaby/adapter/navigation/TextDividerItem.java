package com.progrema.superbaby.adapter.navigation;

import com.progrema.superbaby.R;

/**
 * Created by aria on 20/2/14.
 */
public class TextDividerItem extends TextItem {



    public TextDividerItem(String text){
        this.setText(text);
        this.setLayout(R.layout.navigation_drawer_section_item);
    }
}
