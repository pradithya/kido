package com.progrema.superbaby.adapter.diaperhistory;

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

/**
 * Created by aria on 20/2/14.
 */
public class DiaperHistoryAdapter extends CursorAdapter
{
    private LayoutInflater inflater;
    private int layout;

    public DiaperHistoryAdapter(Context context, Cursor c, int flags)
    {
        super(context, c, flags);
        inflater = LayoutInflater.from(context);
        layout = R.layout.history_item_diaper;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        return inflater.inflate(layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        String timeStamp = cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TIMESTAMP);
        String type = cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TYPE);

        TextView textViewDate = (TextView) view.findViewById(R.id.history_item_date);
        TextView textViewTime = (TextView) view.findViewById(R.id.history_item_time);
        TextView textViewType = (TextView) view.findViewById(R.id.history_item_type);

        textViewDate.setText(FormatUtils.formatDate(context, timeStamp));
        textViewTime.setText(FormatUtils.formatTime(context, timeStamp));
        textViewType.setText(type);
    }
}
