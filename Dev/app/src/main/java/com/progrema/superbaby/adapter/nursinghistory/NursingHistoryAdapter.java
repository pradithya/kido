package com.progrema.superbaby.adapter.nursinghistory;

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


/**
 * Created by aria on 26/1/14.
 */
public class NursingHistoryAdapter extends CursorAdapter {
    public NursingHistoryAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.history_item_nursing, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String timeStamp = cursor.getString(BabyLogContract.Nursing.Query.OFFSET_TIMESTAMP);
        String sides = cursor.getString(BabyLogContract.Nursing.Query.OFFSET_SIDES);
        String duration = cursor.getString(BabyLogContract.Nursing.Query.OFFSET_DURATION);
        String volume = cursor.getString(BabyLogContract.Nursing.Query.OFFSET_VOLUME);

        TextView textViewDate = (TextView) view.findViewById(R.id.history_item_day);
        TextView textViewTime = (TextView) view.findViewById(R.id.history_item_time);
        TextView textViewSides = (TextView) view.findViewById(R.id.history_item_side);
        TextView textViewDuration = (TextView) view.findViewById(R.id.history_item_duration);
        TextView textViewVolume = (TextView) view.findViewById(R.id.history_item_volume);
        textViewVolume.setVisibility(View.GONE);

        textViewDate.setText(FormatUtils.formatDate(context, timeStamp));
        textViewTime.setText(FormatUtils.formatTime(context, timeStamp));
        textViewSides.setText(sides);
        textViewDuration.setText(FormatUtils.formatDuration(context, duration));

        if (sides.compareTo(Nursing.NursingType.FORMULA.getTitle()) == 0) {
            textViewVolume.setVisibility(View.VISIBLE);
            textViewVolume.setText(volume);
        }
    }
}
