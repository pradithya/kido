package com.progrema.superbaby.adapter.diaperhistory;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.progrema.superbaby.R;
import com.progrema.superbaby.models.Diaper;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.FormatUtils;

public class DiaperHistoryAdapter extends CursorAdapter {
    public DiaperHistoryAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.adapter_history_item_diaper, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String sTimestamp = cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TIMESTAMP);
        String sType = cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TYPE);

        TextView tvDay = (TextView) view.findViewById(R.id.history_item_day);
        TextView tvDate = (TextView) view.findViewById(R.id.history_item_date);
        TextView tvTime = (TextView) view.findViewById(R.id.history_item_time);
        ImageView ivType = (ImageView) view.findViewById(R.id.icon_type);

        tvDay.setText(FormatUtils.fmtDayOnly(context, sTimestamp));
        tvDate.setText(FormatUtils.fmtDateOnly(context, sTimestamp));
        tvTime.setText(FormatUtils.fmtTime(context, sTimestamp));

        if (sType.equals(Diaper.DiaperType.WET.getTitle()))
            ivType.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_diaper_wet));
        else if (sType.equals(Diaper.DiaperType.DRY.getTitle()))
            ivType.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_diaper_dry));
        if (sType.equals(Diaper.DiaperType.MIXED.getTitle()))
            ivType.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_diaper_mixed));
    }
}
