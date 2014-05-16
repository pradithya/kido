package com.progrema.superbaby.adapter.navigation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

public abstract class Item {

    /**
     * protected fields
     */
    protected int layout;
    protected int cTextColor;
    protected Drawable dThumbnail;

    /**
     * private field
     */
    private String text;

    /**
     * get text
     *
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * set text
     *
     * @param text view text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * get layout
     *
     * @return item layout
     */
    public int getLayout() {
        return layout;
    }

    /**
     * set layout
     *
     * @param layout item layout
     */
    public void setLayout(int layout) {
        this.layout = layout;
    }

    /**
     * set thumbnail
     *
     * @return
     */
    public Drawable getThumbnail() {
        return dThumbnail;
    }

    /**
     * get thumbnail
     *
     * @param dThumbnail
     */
    public void setThumbnail(Drawable dThumbnail) {
        this.dThumbnail = dThumbnail;
    }

    /**
     * set text color
     *
     * @return
     */
    public int getTextColor() {
        return cTextColor;
    }

    /**
     * set text color
     *
     * @param cTextColor
     */
    public void setTextColor(int cTextColor) {
        this.cTextColor = cTextColor;
    }

    /**
     * inflate view
     *
     * @param context   application context
     * @param view      view to inflate
     * @param viewGroup parent view
     * @return inflated view
     */
    public abstract View inflate(Context context, View view, ViewGroup viewGroup);
}
