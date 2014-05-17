package com.progrema.superbaby.adapter.timeline;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.progrema.superbaby.R;
import com.progrema.superbaby.models.Nursing;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.FormatUtils;

public class TimelineAdapter extends CursorAdapter {
    public TimelineAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.adapter_timeline, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String timeStamp = cursor.getString(BabyLogContract.Activity.Query.OFFSET_TIMESTAMP);
        String activityType = cursor.getString(BabyLogContract.Activity.Query.OFFSET_ACTIVITY_TYPE);
        String sleepDuration = cursor.getString(BabyLogContract.Activity.Query.OFFSET_SLEEP_DURATION);
        String diaperType = cursor.getString(BabyLogContract.Activity.Query.OFFSET_DIAPER_TYPE);
        String nursingSides = cursor.getString(BabyLogContract.Activity.Query.OFFSET_NURSING_SIDES);
        String nursingDuration = cursor.getString(BabyLogContract.Activity.Query.OFFSET_NURSING_DURATION);
        String nursingVolume = cursor.getString(BabyLogContract.Activity.Query.OFFSET_NURSING_VOLUME);

        // TextView for common info
        TextView textViewActivityType = (TextView) view.findViewById(R.id.history_item_act_type);
        TextView textViewDate = (TextView) view.findViewById(R.id.history_item_day);
        TextView textViewTime = (TextView) view.findViewById(R.id.history_item_time);

        // TextView for extra information
        TextView extra_0 = (TextView) view.findViewById(R.id.history_item_extra0);
        TextView extra_1 = (TextView) view.findViewById(R.id.history_item_extra1);
        TextView extra_2 = (TextView) view.findViewById(R.id.history_item_extra2);

        // Set data on common information
        textViewActivityType.setText(activityType);
        textViewDate.setText(FormatUtils.fmtDate(context, timeStamp));
        textViewTime.setText(FormatUtils.fmtTime(context, timeStamp));

        // Set all info visibility to "GONE" by default
        extra_0.setVisibility(View.GONE);
        extra_1.setVisibility(View.GONE);
        extra_2.setVisibility(View.GONE);

        if (activityType.equals(BabyLogContract.Activity.TYPE_SLEEP)) {
            extra_0.setVisibility(View.VISIBLE);
            extra_1.setVisibility(View.VISIBLE);
            extra_0.setText(FormatUtils.fmtTimeBoundary(context, timeStamp, sleepDuration));
            extra_1.setText(FormatUtils.fmtDuration(context, sleepDuration));
        } else if (activityType.equals(BabyLogContract.Activity.TYPE_DIAPER)) {
            extra_0.setVisibility(View.VISIBLE);
            extra_0.setText(diaperType);
        } else if (activityType.equals(BabyLogContract.Activity.TYPE_NURSING)) {
            extra_0.setVisibility(View.VISIBLE);
            extra_1.setVisibility(View.VISIBLE);
            extra_0.setText(nursingSides);
            extra_1.setText(FormatUtils.fmtDuration(context, nursingDuration));

            if (nursingSides.equals(Nursing.NursingType.FORMULA.getTitle())) {
                extra_2.setVisibility(View.VISIBLE);
                extra_2.setText(nursingVolume);
            }
        }
    }
}
