package com.progrema.superbaby.adapter.navigation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

public abstract class Item {

    protected int layout;
    protected int cTextColor;
    protected Drawable dThumbnail;
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public Drawable getThumbnail() {
        return dThumbnail;
    }

    public void setThumbnail(Drawable dThumbnail) {
        this.dThumbnail = dThumbnail;
    }

    public int getTextColor() {
        return cTextColor;
    }

    public void setTextColor(int cTextColor) {
        this.cTextColor = cTextColor;
    }

    public abstract View inflate(Context context, View view, ViewGroup viewGroup);
}
