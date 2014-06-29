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
import com.progrema.superbaby.models.Diaper;
import com.progrema.superbaby.models.Nursing;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.FormatUtils;

public class TimelineAdapter extends CursorAdapter {

    private String timestamp;
    private String activityType;
    private String sleepDuration;
    private String diaperType;
    private String nursingSide;
    private String nursingDuration;
    private String nursingVolume;
    private String heightMeasurement;
    private String weightMeasurement;
    private String entryTag;
    private TextView timeHandler;
    private TextView firstHandler;
    private TextView secondHandler;
    private TextView thirdHandler;
    private ImageView iconHandler;
    private ImageView menuHandler;

    public TimelineAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return layoutInflater.inflate(R.layout.adapter_timeline, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        storeCursorData(cursor);
        prepareWidgetHandler(context, view);
        if (isActivityType(BabyLogContract.Activity.TYPE_SLEEP)) {
            inflateSleepActivityLayout(context, view);
        } else if (isActivityType(BabyLogContract.Activity.TYPE_DIAPER)) {
            inflateDiaperActivityLayout(context, view);
        } else if (isActivityType(BabyLogContract.Activity.TYPE_NURSING)) {
            inflateNursingActivityLayout(context, view);
        } else if (isActivityType(BabyLogContract.Activity.TYPE_MEASUREMENT)) {
            inflateMeasurementActivityLayout(context, view);
        }
    }

    private void storeCursorData(Cursor cursor) {
        timestamp = cursor.getString(BabyLogContract.Activity.Query.OFFSET_TIMESTAMP);
        activityType = cursor.getString(BabyLogContract.Activity.Query.OFFSET_ACTIVITY_TYPE);
        sleepDuration = cursor.getString(BabyLogContract.Activity.Query.OFFSET_SLEEP_DURATION);
        diaperType = cursor.getString(BabyLogContract.Activity.Query.OFFSET_DIAPER_TYPE);
        nursingSide = cursor.getString(BabyLogContract.Activity.Query.OFFSET_NURSING_SIDES);
        nursingDuration = cursor.getString(BabyLogContract.Activity.Query.OFFSET_NURSING_DURATION);
        nursingVolume = cursor.getString(BabyLogContract.Activity.Query.OFFSET_NURSING_VOLUME);
        heightMeasurement = cursor.getString(BabyLogContract.Activity.Query.OFFSET_MEASUREMENT_HEIGHT);
        weightMeasurement = cursor.getString(BabyLogContract.Activity.Query.OFFSET_MEASUREMENT_WEIGHT);
        entryTag = cursor.getString(BabyLogContract.Activity.Query.OFFSET_ID);
    }

    private void prepareWidgetHandler(final Context context, View view) {
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
                                            entryEdit(context, menuHandler);
                                        } else if (item.getTitle().equals("Delete")) {
                                            entryDelete(context, menuHandler);
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

    private boolean isActivityType(String type) {
        return activityType.equals(type);
    }

    private void inflateSleepActivityLayout(Context context, View view) {
        firstHandler.setText(FormatUtils.fmtDate(context, timestamp));
        secondHandler.setText(FormatUtils.fmtTimeBoundary(context, timestamp, sleepDuration));
        thirdHandler.setVisibility(View.VISIBLE);
        thirdHandler.setText(FormatUtils.fmtDuration(context, sleepDuration));
        if (FormatUtils.isNight(Long.parseLong(timestamp))) {
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

    private void inflateDiaperActivityLayout(Context context, View view) {
        firstHandler.setText(FormatUtils.fmtDayOnly(context, timestamp));
        secondHandler.setText(FormatUtils.fmtDateOnly(context, timestamp));
        if (diaperType.equals(Diaper.DiaperType.WET.getTitle())) {
            iconHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_diaper_wet));
            secondHandler.setTextColor(view.getResources().getColor(R.color.blue));
            timeHandler.setTextColor(view.getResources().getColor(R.color.blue));
        } else if (diaperType.equals(Diaper.DiaperType.DRY.getTitle())) {
            iconHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_diaper_dry));
            secondHandler.setTextColor(view.getResources().getColor(R.color.orange));
            timeHandler.setTextColor(view.getResources().getColor(R.color.orange));
        } else if (diaperType.equals(Diaper.DiaperType.MIXED.getTitle())) {
            iconHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_diaper_mixed));
            secondHandler.setTextColor(view.getResources().getColor(R.color.purple));
            timeHandler.setTextColor(view.getResources().getColor(R.color.purple));
        }
    }

    private void inflateNursingActivityLayout(Context context, View view) {
        firstHandler.setText(FormatUtils.fmtDate(context, timestamp));
        secondHandler.setText(FormatUtils.fmtDuration(context, nursingDuration));
        if (nursingSide.compareTo(Nursing.NursingType.FORMULA.getTitle()) == 0) {
            thirdHandler.setVisibility(View.VISIBLE);
            thirdHandler.setText(nursingVolume + "mL");
            thirdHandler.setTextColor(view.getResources().getColor(R.color.red));
            timeHandler.setTextColor(view.getResources().getColor(R.color.red));
            secondHandler.setTextColor(view.getResources().getColor(R.color.red));
            iconHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_nursing_formula));
        } else if (nursingSide.compareTo(Nursing.NursingType.RIGHT.getTitle()) == 0) {
            secondHandler.setTextColor(view.getResources().getColor(R.color.green));
            timeHandler.setTextColor(view.getResources().getColor(R.color.green));
            iconHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_nursing_right));
        } else if (nursingSide.compareTo(Nursing.NursingType.LEFT.getTitle()) == 0) {
            secondHandler.setTextColor(view.getResources().getColor(R.color.orange));
            timeHandler.setTextColor(view.getResources().getColor(R.color.orange));
            iconHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_nursing_left));
        }
    }

    private void inflateMeasurementActivityLayout(Context context, View view) {
        iconHandler.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_measurement_entry));
        firstHandler.setText(FormatUtils.fmtDate(context, timestamp));
        secondHandler.setText(heightMeasurement + " cm");
        secondHandler.setTextColor(view.getResources().getColor(R.color.orange));
        thirdHandler.setVisibility(View.VISIBLE);
        thirdHandler.setText(weightMeasurement + " kg");
        thirdHandler.setTextColor(view.getResources().getColor(R.color.orange));
        timeHandler.setTextColor(view.getResources().getColor(R.color.orange));
    }

    private void entryDelete(Context context, View entry) {
    }

    private void entryEdit(Context context, View entry) {
    }
}
