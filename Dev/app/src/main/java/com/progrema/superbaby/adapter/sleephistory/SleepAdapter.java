package com.progrema.superbaby.adapter.sleephistory;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.progrema.superbaby.R;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.FormatUtils;

public class SleepAdapter extends CursorAdapter {
    public SleepAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater li_inflater = LayoutInflater.from(context);
        return li_inflater.inflate(R.layout.adapter_sleep, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String s_timestamp = cursor.getString(BabyLogContract.Sleep.Query.OFFSET_TIMESTAMP);
        String s_duration = cursor.getString(BabyLogContract.Sleep.Query.OFFSET_DURATION);

        TextView tv_timestamp = (TextView) view.findViewById(R.id.history_item_timestamp);
        TextView tv_timeBoundary = (TextView) view.findViewById(R.id.history_item_time_boundary);
        //TextView tv_duration = (TextView) view.findViewById(R.id.history_item_duration);
        TextView tv_time = (TextView) view.findViewById(R.id.history_item_time);
        ImageView iv_type = (ImageView) view.findViewById(R.id.icon_type);

        tv_timestamp.setText(FormatUtils.fmtDate(context, s_timestamp));
        tv_timeBoundary.setText(FormatUtils.fmtTimeBoundary(context, s_timestamp, s_duration));
        //tv_duration.setText(FormatUtils.fmtDuration(context, s_duration));
        tv_time.setText(FormatUtils.fmtTime(context, s_timestamp));

        if (FormatUtils.isNight(Long.parseLong(s_timestamp))) {
            iv_type.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_sleep_night));
        } else {
            iv_type.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_sleep_nap));
        }

    }
}
