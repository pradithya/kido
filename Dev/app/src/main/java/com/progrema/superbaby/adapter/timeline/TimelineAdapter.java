package com.progrema.superbaby.adapter.timeline;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.progrema.superbaby.R;
import com.progrema.superbaby.models.Diaper;
import com.progrema.superbaby.models.Nursing;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.FormatUtils;

public class TimelineAdapter extends CursorAdapter {

    public TimelineAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater liInflater = LayoutInflater.from(context);
        return liInflater.inflate(R.layout.adapter_timeline, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String sTimeStamp = cursor.getString(BabyLogContract.Activity.Query.OFFSET_TIMESTAMP);
        String sActivityType = cursor.getString(BabyLogContract.Activity.Query.OFFSET_ACTIVITY_TYPE);
        String sSleepDuration = cursor.getString(BabyLogContract.Activity.Query.OFFSET_SLEEP_DURATION);
        String sDiaperType = cursor.getString(BabyLogContract.Activity.Query.OFFSET_DIAPER_TYPE);
        String sNursingSides = cursor.getString(BabyLogContract.Activity.Query.OFFSET_NURSING_SIDES);
        String sNursingDuration = cursor.getString(BabyLogContract.Activity.Query.OFFSET_NURSING_DURATION);
        String sNursingVolume = cursor.getString(BabyLogContract.Activity.Query.OFFSET_NURSING_VOLUME);
        String sMeasurementHeight = cursor.getString(BabyLogContract.Activity.Query.OFFSET_MEASUREMENT_HEIGHT);
        String sMeasurementWeight = cursor.getString(BabyLogContract.Activity.Query.OFFSET_MEASUREMENT_WEIGHT);

        TextView tvInformationTime = (TextView) view.findViewById(R.id.information_time);
        TextView tvInformationOne = (TextView) view.findViewById(R.id.information_one);
        TextView tvInformationTwo = (TextView) view.findViewById(R.id.information_two);
        TextView tvInformationThree = (TextView) view.findViewById(R.id.information_three);
        ImageView ivType = (ImageView) view.findViewById(R.id.icon_type);

        // Time is always show in every type
        tvInformationTime.setText(FormatUtils.fmtTime(context, sTimeStamp));

        if (sActivityType.equals(BabyLogContract.Activity.TYPE_SLEEP)) {
            /**
             * Sleep type handler
             */
            tvInformationOne.setText(FormatUtils.fmtDate(context, sTimeStamp));
            tvInformationTwo.setText(FormatUtils.fmtTimeBoundary(context, sTimeStamp, sSleepDuration));
            tvInformationThree.setText(FormatUtils.fmtDuration(context, sSleepDuration));
            if (FormatUtils.isNight(Long.parseLong(sTimeStamp))) {
                ivType.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_sleep_night));
                tvInformationTime.setTextColor(view.getResources().getColor(R.color.blue));
                tvInformationTwo.setTextColor(view.getResources().getColor(R.color.blue));
                tvInformationThree.setTextColor(view.getResources().getColor(R.color.blue));
            } else {
                ivType.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_sleep_nap));
                tvInformationTime.setTextColor(view.getResources().getColor(R.color.orange));
                tvInformationTwo.setTextColor(view.getResources().getColor(R.color.orange));
                tvInformationThree.setTextColor(view.getResources().getColor(R.color.orange));}

        } else if (sActivityType.equals(BabyLogContract.Activity.TYPE_DIAPER)) {
            /**
             * Diaper type handler
             */
            tvInformationOne.setText(FormatUtils.fmtDayOnly(context, sTimeStamp));
            tvInformationTwo.setText(FormatUtils.fmtDateOnly(context, sTimeStamp));
            if (sDiaperType.equals(Diaper.DiaperType.WET.getTitle())) {
                ivType.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_diaper_wet));
                tvInformationTwo.setTextColor(view.getResources().getColor(R.color.blue));
                tvInformationTime.setTextColor(view.getResources().getColor(R.color.blue));
            } else if (sDiaperType.equals(Diaper.DiaperType.DRY.getTitle())) {
                ivType.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_diaper_dry));
                tvInformationTwo.setTextColor(view.getResources().getColor(R.color.orange));
                tvInformationTime.setTextColor(view.getResources().getColor(R.color.orange));
            } else if (sDiaperType.equals(Diaper.DiaperType.MIXED.getTitle())) {
                ivType.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_diaper_mixed));
                tvInformationTwo.setTextColor(view.getResources().getColor(R.color.purple));
                tvInformationTime.setTextColor(view.getResources().getColor(R.color.purple));}

        } else if (sActivityType.equals(BabyLogContract.Activity.TYPE_NURSING)) {
            /**
             * Nursing type handler
             */
            tvInformationOne.setText(FormatUtils.fmtDate(context, sTimeStamp));
            tvInformationTwo.setText(FormatUtils.fmtDuration(context, sNursingDuration));
            if (sNursingSides.compareTo(Nursing.NursingType.FORMULA.getTitle()) == 0) {
                tvInformationThree.setText(sNursingVolume + "mL");
                tvInformationThree.setTextColor(view.getResources().getColor(R.color.red));
                tvInformationTime.setTextColor(view.getResources().getColor(R.color.red));
                tvInformationTwo.setTextColor(view.getResources().getColor(R.color.red));
                ivType.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_nursing_formula));
            } else if (sNursingSides.compareTo(Nursing.NursingType.RIGHT.getTitle()) == 0) {
                tvInformationTwo.setTextColor(view.getResources().getColor(R.color.green));
                tvInformationTime.setTextColor(view.getResources().getColor(R.color.green));
                ivType.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_nursing_right));
            } else if (sNursingSides.compareTo(Nursing.NursingType.LEFT.getTitle()) == 0) {
                tvInformationTwo.setTextColor(view.getResources().getColor(R.color.orange));
                tvInformationTime.setTextColor(view.getResources().getColor(R.color.orange));
                ivType.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_nursing_left));}

        } else if (sActivityType.equals(BabyLogContract.Activity.TYPE_MEASUREMENT)) {
            /**
             * Measurement type handler
             */
            ivType.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_measurement_entry));
            tvInformationOne.setText(FormatUtils.fmtDate(context, sTimeStamp));
            tvInformationTwo.setText(sMeasurementHeight + " cm");
            tvInformationTwo.setTextColor(view.getResources().getColor(R.color.orange));
            tvInformationThree.setText(sMeasurementWeight + " kg");
            tvInformationThree.setTextColor(view.getResources().getColor(R.color.orange));
            tvInformationTime.setTextColor(view.getResources().getColor(R.color.orange));
        }
    }
}
