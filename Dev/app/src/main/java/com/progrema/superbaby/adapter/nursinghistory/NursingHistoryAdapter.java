package com.progrema.superbaby.adapter.nursinghistory;

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

public class NursingHistoryAdapter extends CursorAdapter {
    public NursingHistoryAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.adapter_history_nursing, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String s_timestamp = cursor.getString(BabyLogContract.Nursing.Query.OFFSET_TIMESTAMP);
        String s_type = cursor.getString(BabyLogContract.Nursing.Query.OFFSET_SIDES);
        String s_duration = cursor.getString(BabyLogContract.Nursing.Query.OFFSET_DURATION);
        String s_volume = cursor.getString(BabyLogContract.Nursing.Query.OFFSET_VOLUME);

        TextView tv_date = (TextView) view.findViewById(R.id.history_item_day);
        TextView tv_time = (TextView) view.findViewById(R.id.history_item_time);
        TextView tv_duration = (TextView) view.findViewById(R.id.history_item_duration);
        TextView tv_volume = (TextView) view.findViewById(R.id.history_item_volume);
        ImageView iv_type = (ImageView) view.findViewById(R.id.icon_type);

        tv_volume.setVisibility(View.GONE);
        tv_date.setText(FormatUtils.fmtDate(context, s_timestamp));
        tv_time.setText(FormatUtils.fmtTime(context, s_timestamp));
        tv_duration.setText(FormatUtils.fmtDuration(context, s_duration));

        if (s_type.compareTo(Nursing.NursingType.FORMULA.getTitle()) == 0) {
            tv_volume.setVisibility(View.VISIBLE);
            tv_volume.setText(s_volume + " mL");
            iv_type.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_nursing_formula));
        } else if (s_type.compareTo(Nursing.NursingType.RIGHT.getTitle()) == 0) {
            iv_type.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_nursing_right));
        } else if(s_type.compareTo(Nursing.NursingType.LEFT.getTitle()) == 0) {
            iv_type.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_nursing_left));
        }
    }
}
