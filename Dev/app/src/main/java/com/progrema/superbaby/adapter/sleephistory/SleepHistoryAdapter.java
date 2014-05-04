package com.progrema.superbaby.adapter.sleephistory;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.progrema.superbaby.R;
import com.progrema.superbaby.util.FormatUtils;

public class SleepHistoryAdapter extends CursorAdapter {
    public SleepHistoryAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.history_item_sleep, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String timeStamp = cursor.getString(3);
        String duration = cursor.getString(4);

        TextView textViewTimeStamp = (TextView) view.findViewById(R.id.history_item_timestamp);
        TextView textViewTimeBoundary = (TextView) view.findViewById(R.id.history_item_timeboundary);
        TextView textViewDuration = (TextView) view.findViewById(R.id.history_item_duration);

        textViewTimeStamp.setText(FormatUtils.formatDate(context, timeStamp));
        textViewTimeBoundary.setText(FormatUtils.formatTimeBoundary(context, timeStamp, duration));
        textViewDuration.setText(FormatUtils.formatDuration(context, duration));
    }
}
