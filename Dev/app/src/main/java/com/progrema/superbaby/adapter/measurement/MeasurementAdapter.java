package com.progrema.superbaby.adapter.measurement;

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

public class MeasurementAdapter extends CursorAdapter {
    public MeasurementAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater liInflater = LayoutInflater.from(context);
        return liInflater.inflate(R.layout.adapter_measurement, parent, false);
    }

    @Override
    public void bindView(View vView, Context cContext, Cursor cCursor) {
        String sTimestamp = cCursor.getString(BabyLogContract.Measurement.Query.OFFSET_TIMESTAMP);
        String sHeight = cCursor.getString(BabyLogContract.Measurement.Query.OFFSET_HEIGHT);
        String sWeight = cCursor.getString(BabyLogContract.Measurement.Query.OFFSET_WEIGHT);

        TextView tvDate = (TextView) vView.findViewById(R.id.history_item_day);
        TextView tvTime = (TextView) vView.findViewById(R.id.history_item_time);
        TextView tvHeight = (TextView) vView.findViewById(R.id.history_item_height);
        TextView tvWeight = (TextView) vView.findViewById(R.id.history_item_weight);

        tvDate.setText(FormatUtils.fmtDate(cContext, sTimestamp));
        tvTime.setText(FormatUtils.fmtTime(cContext, sTimestamp));
        tvHeight.setText(sHeight + " cm");
        tvWeight.setText(sWeight + " kg");
    }
}
