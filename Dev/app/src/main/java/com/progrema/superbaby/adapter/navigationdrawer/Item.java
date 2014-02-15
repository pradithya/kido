package com.progrema.superbaby.adapter.navigationdrawer;

import android.view.View;

/**
 * Created by iqbalpakeh on 15/2/14.
 */
public abstract class Item
{
    /**
     * private field
     */
    private String text;
    private int layout;

    /**
     * get text
     *
     * @return text
     */
    public String getText()
    {
        return text;
    }

    /**
     * set text
     *
     * @param text view text
     */
    public void setText(String text) 
    {
        this.text = text;
    }

    /**
     * get layout
     *
     * @return item layout
     */
    public int getLayout() 
    {
        return layout;
    }

    /**
     * set layout
     *
     * @param layout item layout
     */
    public void setLayout(int layout) 
    {
        this.layout = layout;
    }

    /**
     * inflate view
     *
     * @param view parent view
     */
    public abstract void inflate(View view);
}
