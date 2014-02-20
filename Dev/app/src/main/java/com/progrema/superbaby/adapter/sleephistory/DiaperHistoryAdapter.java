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

/**
 * Created by aria on 20/2/14.
 */
public class DiaperHistoryAdapter extends CursorAdapter{
    private LayoutInflater inflater;
    private int layout;


    public DiaperHistoryAdapter(Context context, Cursor c, int flags)
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
        String timeStamp = cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TIMESTAMP);
        String type = cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TYPE);

        TextView textViewTimeStamp = (TextView) view.findViewById(R.id.history_item_timestamp);
        TextView textViewTtype = (TextView) view.findViewById(R.id.history_item_type);

        textViewTimeStamp.setText(FormatUtils.formatTimeStamp(context, timeStamp));
        textViewTtype.setText(type);
    }

    public void setLayout(int newLayout)
    {
        layout = newLayout;
    }
}
