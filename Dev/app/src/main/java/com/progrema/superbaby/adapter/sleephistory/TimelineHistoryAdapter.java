package com.progrema.superbaby.adapter.sleephistory;

import android.content.Context;
import android.database.Cursor;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.progrema.superbaby.R;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.FormatUtils;

import org.w3c.dom.Text;

/**
 * Created by aria on 21/2/14.
 */
public class TimelineHistoryAdapter extends CursorAdapter {

    private LayoutInflater inflater;
    private int layout;

    public TimelineHistoryAdapter(Context context, Cursor c, int flags)
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
        long id = cursor.getLong(BabyLogContract.Activity.Query.OFFSET_ID);
        String timeStamp = cursor.getString(BabyLogContract.Activity.Query.OFFSET_TIMESTAMP);
        String activityType = cursor.getString(BabyLogContract.Activity.Query.OFFSET_ACTIVITY_TYPE);
        String sleepDuration = cursor.getString(BabyLogContract.Activity.Query.OFFSET_SLEEP_DURATION);
        String diaperType = cursor.getString(BabyLogContract.Activity.Query.OFFSET_DIAPER_TYPE);

        TextView textViewActivityType = (TextView) view.findViewById(R.id.history_item_act_type);
        TextView textViewDate = (TextView) view.findViewById(R.id.history_item_date);
        TextView textViewTime = (TextView) view.findViewById(R.id.history_item_time);
        TextView textViewTimeBoundary = (TextView) view.findViewById(R.id.history_item_timeboundary);
        TextView textViewDuration = (TextView) view.findViewById(R.id.history_item_duration);
        TextView textViewDiaperType = (TextView) view.findViewById(R.id.history_item_diaper_type);

        textViewActivityType.setText(activityType);
        textViewDate.setText(FormatUtils.formatDate(context, timeStamp));
        textViewTime.setText(FormatUtils.formatTime(context, timeStamp));

        if (activityType.equals(BabyLogContract.Activity.TYPE_SLEEP)){
            textViewDiaperType.setVisibility(View.GONE);
            textViewTimeBoundary.setVisibility(View.VISIBLE);
            textViewDuration.setVisibility(View.VISIBLE);
            textViewTimeBoundary.setText(FormatUtils.formatTimeBoundary(context, timeStamp, sleepDuration));
            textViewDuration.setText(FormatUtils.formatDuration(context, sleepDuration));

        }else if (activityType.equals(BabyLogContract.Activity.TYPE_DIAPER)){
            textViewDiaperType.setVisibility(View.VISIBLE);
            textViewTimeBoundary.setVisibility(View.GONE);
            textViewDuration.setVisibility(View.GONE);
            textViewDiaperType.setText(diaperType);
        }
    }

    public void setLayout(int newLayout)
    {
        layout = newLayout;
    }
}
