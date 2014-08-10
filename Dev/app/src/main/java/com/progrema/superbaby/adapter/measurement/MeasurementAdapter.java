package com.progrema.superbaby.adapter.measurement;

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
import com.progrema.superbaby.models.ActivityMeasurement;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.FormatUtils;

public class MeasurementAdapter extends CursorAdapter implements EntryAdapterServices {

    private String timestamp;
    private String height;
    private String weight;
    private String entryTag;
    private TextView dateHandler;
    private TextView timeHandler;
    private TextView heightHandler;
    private TextView weightHandler;
    private ImageView menuHandler;
    private Callbacks callbacks;

    public MeasurementAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public void setCallbacks(Callbacks listener) {
        callbacks = listener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return layoutInflater.inflate(R.layout.adapter_measurement, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        storeCursorData(cursor);
        prepareHandler(context, view);
    }

    @Override
    public void storeCursorData(Cursor cursor) {
        timestamp = cursor.getString(BabyLogContract.Measurement.Query.OFFSET_TIMESTAMP);
        height = cursor.getString(BabyLogContract.Measurement.Query.OFFSET_HEIGHT);
        weight = cursor.getString(BabyLogContract.Measurement.Query.OFFSET_WEIGHT);
        entryTag = cursor.getString(BabyLogContract.Measurement.Query.OFFSET_ACTIVITY_ID);
    }

    @Override
    public void prepareHandler(final Context context, View view) {
        dateHandler = (TextView) view.findViewById(R.id.history_item_day);
        timeHandler = (TextView) view.findViewById(R.id.widget_time);
        heightHandler = (TextView) view.findViewById(R.id.history_item_height);
        weightHandler = (TextView) view.findViewById(R.id.history_item_weight);
        menuHandler = (ImageView) view.findViewById(R.id.menu_button);

        dateHandler.setText(FormatUtils.fmtDate(context, timestamp));
        timeHandler.setText(FormatUtils.fmtTime(context, timestamp));
        heightHandler.setText(height + " cm");
        weightHandler.setText(weight + " kg");
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
                                        if (item.getTitle().equals(
                                                context.getResources().getString(R.string.menu_edit))) {
                                            editEntry(menuHandler);
                                        } else if (item.getTitle().equals(
                                                context.getResources().getString(R.string.menu_delete))) {
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
        ActivityMeasurement activityMeasurement = new ActivityMeasurement();
        activityMeasurement.setActivityId(Long.valueOf((String) entry.getTag()));
        activityMeasurement.delete(context);
    }

    @Override
    public void editEntry(View entry) {
        callbacks.onMeasurementEntryEditSelected(entry);
    }

    public static interface Callbacks {
        public void onMeasurementEntryEditSelected(View entry);
    }

}
