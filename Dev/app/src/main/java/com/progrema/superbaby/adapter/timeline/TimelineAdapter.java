package com.progrema.superbaby.adapter.timeline;

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
import com.progrema.superbaby.models.ActivityMeasurement;
import com.progrema.superbaby.models.ActivityNursing;
import com.progrema.superbaby.models.ActivitySleep;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.FormatUtils;

public class TimelineAdapter extends CursorAdapter implements EntryAdapterServices {

    private String timestamp;
    private String activityType;
    private String sleepDuration;
    private String diaperType;
    private String nursingSide;
    private String nursingDuration;
    private String nursingVolume;
    private String heightMeasurement;
    private String weightMeasurement;
    private Cursor entryTag;
    private TextView timeHandler;
    private TextView firstHandler;
    private TextView secondHandler;
    private TextView thirdHandler;
    private ImageView iconHandler;
    private ImageView menuHandler;
    private Callbacks callbacks;

    public TimelineAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return layoutInflater.inflate(R.layout.adapter_timeline, parent, false);
    }

    public void setCallbacks(Callbacks listener) {
        callbacks = listener;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        storeCursorData(cursor);
        prepareHandler(context, view);
        if (isEntryType(BabyLogContract.Activity.TYPE_SLEEP)) {
            inflateSleepEntryLayout(context, view);
        } else if (isEntryType(BabyLogContract.Activity.TYPE_DIAPER)) {
            inflateDiaperEntryLayout(context, view);
        } else if (isEntryType(BabyLogContract.Activity.TYPE_NURSING)) {
            inflateNursingEntryLayout(context, view);
        } else if (isEntryType(BabyLogContract.Activity.TYPE_MEASUREMENT)) {
            inflateMeasurementEntryLayout(context, view);
        }
    }

    @Override
    public void storeCursorData(Cursor cursor) {
        timestamp = cursor.getString(BabyLogContract.Activity.Query.OFFSET_TIMESTAMP);
        activityType = cursor.getString(BabyLogContract.Activity.Query.OFFSET_ACTIVITY_TYPE);
        sleepDuration = cursor.getString(BabyLogContract.Activity.Query.OFFSET_SLEEP_DURATION);
        diaperType = cursor.getString(BabyLogContract.Activity.Query.OFFSET_DIAPER_TYPE);
        nursingSide = cursor.getString(BabyLogContract.Activity.Query.OFFSET_NURSING_SIDES);
        nursingDuration = cursor.getString(BabyLogContract.Activity.Query.OFFSET_NURSING_DURATION);
        nursingVolume = cursor.getString(BabyLogContract.Activity.Query.OFFSET_NURSING_VOLUME);
        heightMeasurement = cursor.getString(BabyLogContract.Activity.Query.OFFSET_MEASUREMENT_HEIGHT);
        weightMeasurement = cursor.getString(BabyLogContract.Activity.Query.OFFSET_MEASUREMENT_WEIGHT);
        entryTag = cursor;
    }

    @Override
    public void prepareHandler(final Context context, View view) {
        timeHandler = (TextView) view.findViewById(R.id.widget_time);
        firstHandler = (TextView) view.findViewById(R.id.widget_first);
        secondHandler = (TextView) view.findViewById(R.id.widget_second);
        thirdHandler = (TextView) view.findViewById(R.id.widget_third);
        iconHandler = (ImageView) view.findViewById(R.id.icon_type);
        menuHandler = (ImageView) view.findViewById(R.id.menu_button);
        timeHandler.setText(FormatUtils.fmtTime(context, timestamp));
        thirdHandler.setVisibility(View.GONE);
        menuHandler.setTag(entryTag);
        menuHandler.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ImageView menuHandler = (ImageView) v.findViewById(R.id.menu_button);
                        PopupMenu popup = new PopupMenu(context, menuHandler);
                        popup.setOnMenuItemClickListener(
                                new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        if (item.getTitle().equals("Edit")) {
                                            editEntry(menuHandler);
                                        } else if (item.getTitle().equals("Delete")) {
                                            deleteEntry(context, menuHandler);
                                        }
                                        return false;
                                    }
                                }
                        );
                        MenuInflater menuInflater = ((Activity) context).getMenuInflater();
                        menuInflater.inflate(R.menu.entry, popup.getMenu());
                        popup.show();
                        Log.i("_DBG_MENU", " Tag = " + menuHandler.getTag());
                    }
                }
        );
    }

    @Override
    public void deleteEntry(Context context, View entry) {
        Cursor cursor = (Cursor) entry.getTag();
        String id = cursor.getString(BabyLogContract.Activity.Query.OFFSET_ID);
        String type = cursor.getString(BabyLogContract.Activity.Query.OFFSET_ACTIVITY_TYPE);
        if (type.equals(BabyLogContract.Activity.TYPE_SLEEP)) {
            deleteSleepEntry(context, id);
        } else if (type.equals(BabyLogContract.Activity.TYPE_DIAPER)) {
            deleteDiaperEntry(context, id);
        } else if (type.equals(BabyLogContract.Activity.TYPE_NURSING)) {
            deleteNursingEntry(context, id);
        } else if (type.equals(BabyLogContract.Activity.TYPE_MEASUREMENT)) {
            deleteMeasurementEntry(context, id);
        }
    }

    private void deleteSleepEntry(Context context, String id) {
        ActivitySleep activitySleep = new ActivitySleep();
        activitySleep.setActivityId(Long.valueOf(id));
        activitySleep.delete(context);
    }

    private void deleteDiaperEntry(Context context, String id) {
        ActivityDiaper activityDiaper = new ActivityDiaper();
        activityDiaper.setActivityId(Long.valueOf(id));
        activityDiaper.delete(context);
    }

    private void deleteNursingEntry(Context context, String id) {
        ActivityNursing activityNursing = new ActivityNursing();
        activityNursing.setActivityId(Long.valueOf(id));
        activityNursing.delete(context);
    }

    private void deleteMeasurementEntry(Context context, String id) {
        ActivityMeasurement activityMeasurement = new ActivityMeasurement();
        activityMeasurement.setActivityId(Long.valueOf(id));
        activityMeasurement.delete(context);
    }

    @Override
    public void editEntry(View entry) {
        callbacks.onTimelineEntryEditSelected(entry);
    }

    public boolean isEntryType(String type) {
        return activityType.equals(type);
    }

    private void inflateSleepEntryLayout(Context context, View view) {
        firstHandler.setText(FormatUtils.fmtDate(context, timestamp));
        secondHandler.setText(FormatUtils.fmtTimeBoundary(context, timestamp, sleepDuration));
        thirdHandler.setVisibility(View.VISIBLE);
        thirdHandler.setText(FormatUtils.fmtDuration(context, sleepDuration));
        if (FormatUtils.isNightTime(Long.parseLong(timestamp))) {
            iconHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_sleep_night));
            timeHandler.setTextColor(view.getResources().getColor(R.color.blue));
            secondHandler.setTextColor(view.getResources().getColor(R.color.blue));
            thirdHandler.setTextColor(view.getResources().getColor(R.color.blue));
        } else {
            iconHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_sleep_nap));
            timeHandler.setTextColor(view.getResources().getColor(R.color.orange));
            secondHandler.setTextColor(view.getResources().getColor(R.color.orange));
            thirdHandler.setTextColor(view.getResources().getColor(R.color.orange));
        }
    }

    private void inflateDiaperEntryLayout(Context context, View view) {
        firstHandler.setText(FormatUtils.fmtDayOnly(context, timestamp));
        secondHandler.setText(FormatUtils.fmtDateOnly(context, timestamp));
        if (diaperType.equals(ActivityDiaper.DiaperType.WET.getTitle())) {
            iconHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_diaper_wet));
            secondHandler.setTextColor(view.getResources().getColor(R.color.blue));
            timeHandler.setTextColor(view.getResources().getColor(R.color.blue));
        } else if (diaperType.equals(ActivityDiaper.DiaperType.DRY.getTitle())) {
            iconHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_diaper_dry));
            secondHandler.setTextColor(view.getResources().getColor(R.color.orange));
            timeHandler.setTextColor(view.getResources().getColor(R.color.orange));
        } else if (diaperType.equals(ActivityDiaper.DiaperType.MIXED.getTitle())) {
            iconHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_diaper_mixed));
            secondHandler.setTextColor(view.getResources().getColor(R.color.purple));
            timeHandler.setTextColor(view.getResources().getColor(R.color.purple));
        }
    }

    private void inflateNursingEntryLayout(Context context, View view) {
        firstHandler.setText(FormatUtils.fmtDate(context, timestamp));
        secondHandler.setText(FormatUtils.fmtDuration(context, nursingDuration));
        if (nursingSide.compareTo(ActivityNursing.NursingType.FORMULA.getTitle()) == 0) {
            thirdHandler.setVisibility(View.VISIBLE);
            thirdHandler.setText(nursingVolume + "mL");
            thirdHandler.setTextColor(view.getResources().getColor(R.color.red));
            timeHandler.setTextColor(view.getResources().getColor(R.color.red));
            secondHandler.setTextColor(view.getResources().getColor(R.color.red));
            iconHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_nursing_formula));
        } else if (nursingSide.compareTo(ActivityNursing.NursingType.RIGHT.getTitle()) == 0) {
            secondHandler.setTextColor(view.getResources().getColor(R.color.green));
            timeHandler.setTextColor(view.getResources().getColor(R.color.green));
            iconHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_nursing_right));
        } else if (nursingSide.compareTo(ActivityNursing.NursingType.LEFT.getTitle()) == 0) {
            secondHandler.setTextColor(view.getResources().getColor(R.color.orange));
            timeHandler.setTextColor(view.getResources().getColor(R.color.orange));
            iconHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_nursing_left));
        }
    }

    private void inflateMeasurementEntryLayout(Context context, View view) {
        iconHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_measurement_entry));
        firstHandler.setText(FormatUtils.fmtDate(context, timestamp));
        secondHandler.setText(heightMeasurement + " cm");
        secondHandler.setTextColor(view.getResources().getColor(R.color.orange));
        thirdHandler.setVisibility(View.VISIBLE);
        thirdHandler.setText(weightMeasurement + " kg");
        thirdHandler.setTextColor(view.getResources().getColor(R.color.orange));
        timeHandler.setTextColor(view.getResources().getColor(R.color.orange));
    }

    public static interface Callbacks {
        public void onTimelineEntryEditSelected(View entry);
    }

}
