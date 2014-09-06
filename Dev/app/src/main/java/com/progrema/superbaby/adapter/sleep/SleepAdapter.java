package com.progrema.superbaby.adapter.sleep;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
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
import com.progrema.superbaby.adapter.EntryAdapterServices;
import com.progrema.superbaby.models.ActivitySleep;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.FormatUtils;

public class SleepAdapter extends CursorAdapter implements EntryAdapterServices {

    private String timestamp;
    private String duration;
    private String entryTag;
    private TextView timestampHandler;
    private TextView durationHandler;
    private TextView timeBoundaryHandler;
    private TextView timeHandler;
    private ImageView typeHandler;
    private ImageView menuHandler;
    private Callback callback;

    public SleepAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public void setCallback(Callback listener) {
        callback = listener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return layoutInflater.inflate(R.layout.adapter_sleep, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        storeCursorData(cursor);
        prepareHandler(context, view);
        if (FormatUtils.isNightTime(Long.parseLong(timestamp))) {
            inflateNightSleepEntryLayout(view);
        } else {
            inflateNapEntryLayout(view);
        }
    }

    @Override
    public void storeCursorData(Cursor cursor) {
        timestamp = cursor.getString(BabyLogContract.Sleep.Query.OFFSET_TIMESTAMP);
        duration = cursor.getString(BabyLogContract.Sleep.Query.OFFSET_DURATION);
        entryTag = cursor.getString(BabyLogContract.Sleep.Query.OFFSET_ACTIVITY_ID);
    }

    @Override
    public void prepareHandler(final Context context, View view) {
        timestampHandler = (TextView) view.findViewById(R.id.history_item_timestamp);
        durationHandler = (TextView) view.findViewById(R.id.history_item_duration);
        timeBoundaryHandler = (TextView) view.findViewById(R.id.history_item_time_boundary);
        timeHandler = (TextView) view.findViewById(R.id.widget_time);
        typeHandler = (ImageView) view.findViewById(R.id.icon_type);
        menuHandler = (ImageView) view.findViewById(R.id.menu_button);

        timestampHandler.setText(FormatUtils.fmtDate(context, timestamp));
        durationHandler.setText(FormatUtils.fmtDuration(context, duration));
        timeBoundaryHandler.setText(FormatUtils.fmtTimeBoundary(context, timestamp, duration));
        timeHandler.setText(FormatUtils.fmtTime(context, timestamp));
        menuHandler.setTag(entryTag);
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
                                        if (item.getTitle()
                                                .equals(context.getResources()
                                                        .getString(R.string.menu_edit))) {
                                            editEntry(menuHandler);
                                        } else if (item.getTitle()
                                                .equals(context.getResources()
                                                        .getString(R.string.menu_delete))) {
                                            deleteEntry(context, menuHandler);
                                        }
                                        return false;
                                    }
                                }
                        );
                        MenuInflater menuInflater = ((Activity) context).getMenuInflater();
                        menuInflater.inflate(R.menu.entry, popupMenu.getMenu());
                        popupMenu.show();
                        //Log.i("_DBG_MENU", " Tag = " + menuHandler.getTag());
                    }
                }
        );
    }

    @Override
    public void deleteEntry(Context context, View entry) {
        ActivitySleep activitySleep = new ActivitySleep();
        activitySleep.setActivityId(Long.valueOf((String) entry.getTag()));
        activitySleep.delete(context);
    }

    @Override
    public void editEntry(View entry) {
        callback.onNursingSleepEditSelected(entry);
    }

    private void inflateNightSleepEntryLayout(View view) {
        typeHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_sleep_night));
        durationHandler.setTextColor(view.getResources().getColor(R.color.blue));
        timeHandler.setTextColor(view.getResources().getColor(R.color.blue));
        timeBoundaryHandler.setTextColor(view.getResources().getColor(R.color.blue));
    }

    private void inflateNapEntryLayout(View view) {
        typeHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_sleep_nap));
        durationHandler.setTextColor(view.getResources().getColor(R.color.orange));
        timeHandler.setTextColor(view.getResources().getColor(R.color.orange));
        timeBoundaryHandler.setTextColor(view.getResources().getColor(R.color.orange));
    }

    public static interface Callback {
        public void onNursingSleepEditSelected(View entry);
    }

}
