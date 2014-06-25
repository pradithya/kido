package com.progrema.superbaby.adapter.sleep;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.progrema.superbaby.R;
import com.progrema.superbaby.models.Sleep;
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
    public void bindView(View view, final Context context, Cursor cursor) {
        String sTimestamp = cursor.getString(BabyLogContract.Sleep.Query.OFFSET_TIMESTAMP);
        String sDuration = cursor.getString(BabyLogContract.Sleep.Query.OFFSET_DURATION);

        TextView tvTimestamp = (TextView) view.findViewById(R.id.history_item_timestamp);
        TextView tvDuration = (TextView) view.findViewById(R.id.history_item_duration);
        TextView tvTimeBoundary = (TextView) view.findViewById(R.id.history_item_time_boundary);
        TextView tvTime = (TextView) view.findViewById(R.id.information_time);
        ImageView ivType = (ImageView) view.findViewById(R.id.icon_type);

        ImageView ivMenuButton = (ImageView) view.findViewById(R.id.menu_button);
        ivMenuButton.setTag(cursor.getString(BabyLogContract.Sleep.Query.OFFSET_ID));
        ivMenuButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ImageView ivMenuButton = (ImageView) v.findViewById(R.id.menu_button);
                        PopupMenu popup = new PopupMenu(context, ivMenuButton);
                        popup.setOnMenuItemClickListener(
                                new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        if (item.getTitle().equals("Edit")) {
                                            handleEdit(context, ivMenuButton);
                                        } else if (item.getTitle().equals("Delete")) {
                                            handleDelete(context, ivMenuButton);
                                        }
                                        return false;
                                    }
                                }
                        );
                        MenuInflater miInflater = ((Activity) context).getMenuInflater();
                        miInflater.inflate(R.menu.entry, popup.getMenu());
                        popup.show();
                        Log.i("_DBG_MENU", " Tag = " + ivMenuButton.getTag());
                    }
                }
        );

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

    private void handleDelete(Context context, View vEntry) {
        Sleep sSleep = new Sleep();
        sSleep.setID(Long.valueOf((String) vEntry.getTag()));
        sSleep.delete(context);
    }

    private void handleEdit(Context context, View vEntry) {
    }
}
