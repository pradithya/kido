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
        return inflater.inflate(R.layout.history_item_diaper, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String timeStamp = cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TIMESTAMP);
        String type = cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TYPE);

        TextView textViewDay = (TextView) view.findViewById(R.id.history_item_day);
        TextView textViewDate = (TextView) view.findViewById(R.id.history_item_date);
        TextView textViewTime = (TextView) view.findViewById(R.id.history_item_time);
        ImageView imageViewType = (ImageView) view.findViewById(R.id.icon_type);

        textViewDay.setText(FormatUtils.formatDayOnly(context, timeStamp));
        textViewDate.setText(FormatUtils.formatDateOnly(context, timeStamp));
        textViewTime.setText(FormatUtils.formatTime(context, timeStamp));

        if (type.equals(Diaper.DiaperType.WET.getTitle()))
            imageViewType.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_diaper_wet));
        else if (type.equals(Diaper.DiaperType.DRY.getTitle()))
            imageViewType.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_diaper_dry));
        if (type.equals(Diaper.DiaperType.MIXED.getTitle()))
            imageViewType.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_diaper_mixed));
    }
}
