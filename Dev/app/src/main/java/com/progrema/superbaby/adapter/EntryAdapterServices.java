package com.progrema.superbaby.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;

public interface EntryAdapterServices {
    public void storeCursorData(Cursor cursor);

    public void prepareHandler(Context context, View view);

    public void deleteEntry(Context context, View entry);

    public void editEntry(View entry);
}
