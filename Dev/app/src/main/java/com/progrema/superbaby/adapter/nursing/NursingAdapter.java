package com.progrema.superbaby.adapter.nursing;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.progrema.superbaby.R;
import com.progrema.superbaby.models.Nursing;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.FormatUtils;

public class NursingAdapter extends CursorAdapter {
    public NursingAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.adapter_nursing, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String sTimestamp = cursor.getString(BabyLogContract.Nursing.Query.OFFSET_TIMESTAMP);
        String sType = cursor.getString(BabyLogContract.Nursing.Query.OFFSET_SIDES);
        String sDuration = cursor.getString(BabyLogContract.Nursing.Query.OFFSET_DURATION);
        String sVolume = cursor.getString(BabyLogContract.Nursing.Query.OFFSET_VOLUME);

        TextView tvDate = (TextView) view.findViewById(R.id.history_item_day);
        TextView tvTime = (TextView) view.findViewById(R.id.history_item_time);
        TextView tvDuration = (TextView) view.findViewById(R.id.history_item_duration);
        TextView tvVolume = (TextView) view.findViewById(R.id.history_item_volume);
        ImageView ivType = (ImageView) view.findViewById(R.id.icon_type);

        tvVolume.setVisibility(View.GONE);
        tvDate.setText(FormatUtils.fmtDate(context, sTimestamp));
        tvTime.setText(FormatUtils.fmtTime(context, sTimestamp));
        tvDuration.setText(FormatUtils.fmtDuration(context, sDuration));

        if (sType.compareTo(Nursing.NursingType.FORMULA.getTitle()) == 0) {
            tvVolume.setVisibility(View.VISIBLE);
            tvVolume.setText(sVolume + "mL");
            tvVolume.setTextColor(view.getResources().getColor(R.color.red));
            tvTime.setTextColor(view.getResources().getColor(R.color.red));
            tvDuration.setTextColor(view.getResources().getColor(R.color.red));
            ivType.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_nursing_formula));
        } else if (sType.compareTo(Nursing.NursingType.RIGHT.getTitle()) == 0) {
            tvDuration.setTextColor(view.getResources().getColor(R.color.green));
            tvTime.setTextColor(view.getResources().getColor(R.color.green));
            ivType.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_nursing_right));
        } else if(sType.compareTo(Nursing.NursingType.LEFT.getTitle()) == 0) {
            tvDuration.setTextColor(view.getResources().getColor(R.color.orange));
            tvTime.setTextColor(view.getResources().getColor(R.color.orange));
            ivType.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_nursing_left));
        }
    }
}
