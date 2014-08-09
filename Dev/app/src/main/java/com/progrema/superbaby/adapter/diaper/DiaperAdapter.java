package com.progrema.superbaby.adapter.diaper;

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
import com.progrema.superbaby.adapter.EntryAdapterServices;
import com.progrema.superbaby.models.ActivityDiaper;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.FormatUtils;

public class DiaperAdapter extends CursorAdapter implements EntryAdapterServices {

    private String timestamp;
    private String type;
    private String entryTag;
    private TextView dayHandler;
    private TextView dateHandler;
    private TextView timeHandler;
    private ImageView typeHandler;
    private ImageView menuHandler;

    public DiaperAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return layoutInflater.inflate(R.layout.adapter_diaper, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        storeCursorData(cursor);
        prepareHandler(context, view);
        if (isEntryType(ActivityDiaper.DiaperType.WET.getTitle())) {
            inflateWetEntryLayout(view);
        } else if (isEntryType(ActivityDiaper.DiaperType.DRY.getTitle())) {
            inflateDryEntryLayout(view);
        } else if (isEntryType(ActivityDiaper.DiaperType.MIXED.getTitle())) {
            inflateMixedEntryLayout(view);
        }
    }

    @Override
    public void storeCursorData(Cursor cursor) {
        timestamp = cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TIMESTAMP);
        type = cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TYPE);
        entryTag = cursor.getString(BabyLogContract.Diaper.Query.OFFSET_ACTIVITY_ID);
    }

    @Override
    public void prepareHandler(final Context context, View view) {
        dayHandler = (TextView) view.findViewById(R.id.history_item_day);
        dateHandler = (TextView) view.findViewById(R.id.history_item_date);
        timeHandler = (TextView) view.findViewById(R.id.widget_time);
        typeHandler = (ImageView) view.findViewById(R.id.icon_type);
        menuHandler = (ImageView) view.findViewById(R.id.menu_button);

        dayHandler.setText(FormatUtils.fmtDayOnly(context, timestamp));
        dateHandler.setText(FormatUtils.fmtDateOnly(context, timestamp));
        timeHandler.setText(FormatUtils.fmtTime(context, timestamp));
        menuHandler.setTag(entryTag);
        menuHandler.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final ImageView menuHandler = (ImageView) view.findViewById(R.id.menu_button);
                        PopupMenu popupMenu = new PopupMenu(context, menuHandler);
                        popupMenu.setOnMenuItemClickListener(
                                new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        if (item.getTitle()
                                                .equals(context.getResources()
                                                        .getString(R.string.menu_update))) {
                                            updateEntry(context, menuHandler);
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
                        Log.i("_DBG_MENU", " Tag = " + menuHandler.getTag());
                    }
                }
        );
    }

    @Override
    public void deleteEntry(Context context, View entry) {
        ActivityDiaper activityDiaper = new ActivityDiaper();
        activityDiaper.setActivityId(Long.valueOf((String) entry.getTag()));
        activityDiaper.delete(context);
    }

    @Override
    public void updateEntry(Context context, View entry) {

    }

    private boolean isEntryType(String type) {
        return this.type.equals(type);
    }

    private void inflateWetEntryLayout(View view) {
        typeHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_diaper_wet));
        timeHandler.setTextColor(view.getResources().getColor(R.color.blue));
        dateHandler.setTextColor(view.getResources().getColor(R.color.blue));
    }

    private void inflateDryEntryLayout(View view) {
        typeHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_diaper_dry));
        timeHandler.setTextColor(view.getResources().getColor(R.color.orange));
        dateHandler.setTextColor(view.getResources().getColor(R.color.orange));
    }

    private void inflateMixedEntryLayout(View view) {
        typeHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_diaper_mixed));
        timeHandler.setTextColor(view.getResources().getColor(R.color.purple));
        dateHandler.setTextColor(view.getResources().getColor(R.color.purple));
    }

}
