package com.progrema.superbaby.adapter.sleephistory;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.progrema.superbaby.R;
import com.progrema.superbaby.provider.BabyLogContract;
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
        String vTimestamp = cursor.getString(BabyLogContract.Sleep.Query.OFFSET_TIMESTAMP);
        String vDuration = cursor.getString(BabyLogContract.Sleep.Query.OFFSET_DURATION);

        TextView tvTimestamp = (TextView) view.findViewById(R.id.history_item_timestamp);
        TextView tvTimeBoundary = (TextView) view.findViewById(R.id.history_item_time_boundary);
        TextView tvDuration = (TextView) view.findViewById(R.id.history_item_duration);
        TextView tvTime = (TextView) view.findViewById(R.id.history_item_time);

        tvTimestamp.setText(FormatUtils.fmtDate(context, vTimestamp));
        tvTimeBoundary.setText(FormatUtils.fmtTimeBoundary(context, vTimestamp, vDuration));
        tvDuration.setText(FormatUtils.fmtDuration(context, vDuration));
        tvTime.setText(FormatUtils.fmtTime(context, vTimestamp));
    }
}
