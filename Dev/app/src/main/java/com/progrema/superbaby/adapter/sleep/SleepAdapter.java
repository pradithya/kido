package com.progrema.superbaby.adapter.sleep;

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
        LayoutInflater liInflater = LayoutInflater.from(context);
        return liInflater.inflate(R.layout.adapter_sleep, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String sTimestamp = cursor.getString(BabyLogContract.Sleep.Query.OFFSET_TIMESTAMP);
        String sDuration = cursor.getString(BabyLogContract.Sleep.Query.OFFSET_DURATION);

        TextView tvTimestamp = (TextView) view.findViewById(R.id.history_item_timestamp);
        TextView tvDuration = (TextView) view.findViewById(R.id.history_item_duration);
        TextView tvTimeBoundary = (TextView) view.findViewById(R.id.history_item_time_boundary);
        TextView tvTime = (TextView) view.findViewById(R.id.information_time);
        ImageView ivType = (ImageView) view.findViewById(R.id.icon_type);

        tvTimestamp.setText(FormatUtils.fmtDate(context, sTimestamp));
        tvDuration.setText(FormatUtils.fmtDuration(context, sDuration));
        tvTimeBoundary.setText(FormatUtils.fmtTimeBoundary(context, sTimestamp, sDuration));
        tvTime.setText(FormatUtils.fmtTime(context, sTimestamp));

        if (FormatUtils.isNight(Long.parseLong(sTimestamp))) {
            ivType.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_sleep_night));
            tvDuration.setTextColor(view.getResources().getColor(R.color.blue));
            tvTime.setTextColor(view.getResources().getColor(R.color.blue));
            tvTimeBoundary.setTextColor(view.getResources().getColor(R.color.blue));
        } else {
            ivType.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_sleep_nap));
            tvDuration.setTextColor(view.getResources().getColor(R.color.orange));
            tvTime.setTextColor(view.getResources().getColor(R.color.orange));
            tvTimeBoundary.setTextColor(view.getResources().getColor(R.color.orange));
        }

    }
}
