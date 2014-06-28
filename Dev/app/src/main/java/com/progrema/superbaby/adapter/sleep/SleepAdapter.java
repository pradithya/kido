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
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return layoutInflater.inflate(R.layout.adapter_sleep, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        String timestamp = cursor.getString(BabyLogContract.Sleep.Query.OFFSET_TIMESTAMP);
        String duration = cursor.getString(BabyLogContract.Sleep.Query.OFFSET_DURATION);

        TextView timestampHandler = (TextView) view.findViewById(R.id.history_item_timestamp);
        TextView durationHandler = (TextView) view.findViewById(R.id.history_item_duration);
        TextView timeBoundaryHandler = (TextView) view.findViewById(R.id.history_item_time_boundary);
        TextView timeHandler = (TextView) view.findViewById(R.id.widget_time);
        ImageView typeHandler = (ImageView) view.findViewById(R.id.icon_type);
        ImageView menuHandler = (ImageView) view.findViewById(R.id.menu_button);

        timestampHandler.setText(FormatUtils.fmtDate(context, timestamp));
        durationHandler.setText(FormatUtils.fmtDuration(context, duration));
        timeBoundaryHandler.setText(FormatUtils.fmtTimeBoundary(context, timestamp, duration));
        timeHandler.setText(FormatUtils.fmtTime(context, timestamp));
        menuHandler.setTag(cursor.getString(BabyLogContract.Sleep.Query.OFFSET_ID));
        menuHandler.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ImageView menuHandler = (ImageView) v.findViewById(R.id.menu_button);
                        PopupMenu popupMenu = new PopupMenu(context, menuHandler);
                        popupMenu.setOnMenuItemClickListener(
                                new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        if (item.getTitle().equals("Edit")) {
                                            editEntry(context, menuHandler);
                                        } else if (item.getTitle().equals("Delete")) {
                                            deleteEntry(context, menuHandler);
                                        }
                                        return false;
                                    }
                                }
                        );
                        MenuInflater menuInflater = ((Activity) context).getMenuInflater();
                        menuInflater.inflate(R.menu.entry, popupMenu.getMenu());
                        popupMenu.show();
                        Log.i("_DBG_MENU", " Tag = " + menuHandler.getTag());
                    }
                }
        );

        if (FormatUtils.isNight(Long.parseLong(timestamp))) {
            typeHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_sleep_night));
            durationHandler.setTextColor(view.getResources().getColor(R.color.blue));
            timeHandler.setTextColor(view.getResources().getColor(R.color.blue));
            timeBoundaryHandler.setTextColor(view.getResources().getColor(R.color.blue));
        } else {
            typeHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_sleep_nap));
            durationHandler.setTextColor(view.getResources().getColor(R.color.orange));
            timeHandler.setTextColor(view.getResources().getColor(R.color.orange));
            timeBoundaryHandler.setTextColor(view.getResources().getColor(R.color.orange));
        }
    }

    private void deleteEntry(Context context, View vEntry) {
        Sleep sleep = new Sleep();
        sleep.setID(Long.valueOf((String) vEntry.getTag()));
        sleep.delete(context);
    }

    private void editEntry(Context context, View vEntry) {
    }
}
