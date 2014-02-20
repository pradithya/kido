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


/**
 * Created by aria on 26/1/14.
 */
public class SleepHistoryAdapter extends CursorAdapter
{
    private LayoutInflater inflater;
    private int layout;

    public SleepHistoryAdapter(Context context, Cursor c, int flags)
    {
        super(context, c, flags);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        return inflater.inflate(layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        long id = cursor.getLong(0);
        long activityID = cursor.getLong(1);
        long babyID = cursor.getLong(2);
        String timeStamp = cursor.getString(3);
        String duration = cursor.getString(4);

        TextView textViewTimeStamp = (TextView) view.findViewById(R.id.history_item_timestamp);
        TextView textViewTimeBoundary = (TextView) view.findViewById(R.id.history_item_timeboundary);
        TextView textViewDuration = (TextView) view.findViewById(R.id.history_item_duration);

        textViewTimeStamp.setText(FormatUtils.formatDate(context, timeStamp));
        textViewTimeBoundary.setText(FormatUtils.formatTimeBoundary(context, timeStamp, duration));
        textViewDuration.setText(FormatUtils.formatDuration(context, duration));
    }

    public void setLayout(int newLayout)
    {
        layout = newLayout;
    }
}
