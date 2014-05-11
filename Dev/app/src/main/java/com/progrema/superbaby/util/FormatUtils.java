package com.progrema.superbaby.util;

import android.content.Context;
import android.text.format.DateUtils;

import com.progrema.superbaby.R;
import com.squareup.phrase.Phrase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for input/output formatting
 * Created by aria on 26/1/14.
 */
public class FormatUtils {
    public final static long SECOND_MILLIS = 1000;
    public final static long MINUTE_MILLIS = SECOND_MILLIS * 60;
    public final static long HOUR_MILLIS = MINUTE_MILLIS * 60;
    public final static long DAY_MILLIS = HOUR_MILLIS * 24;

    private static final String[] DAY_OF_WEEK_SHORT = {"Sun", "Mon", "Tue", "Wed",
            "Thur", "Fri", "Sat"};
    private static final String[] MONTH_OF_YEAR = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private static final String[] DAY_OF_WEEK_COMPLETE = {"Sunday", "Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday"};

    public static boolean isDay(Context context, String timestamp) {
        // TODO: get better algorithm to check day and night condition

        // get time reference at 18.00
        Calendar today1800 = Calendar.getInstance();
        today1800.set(Calendar.HOUR_OF_DAY, 18);
        today1800.set(Calendar.MINUTE, 0);
        today1800.set(Calendar.SECOND, 0);
        today1800.set(Calendar.MILLISECOND, 0);

        // get time reference at 06.00

        return false;
    }

    /**
     * @param startTime in timemillis format
     * @param duration  in timemillis format
     * @return "HH:MM - HH:MM" format
     */
    public static String fmtTimeBoundary(Context context, String startTime, String duration) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(startTime));
        String start = new SimpleDateFormat("HH:mm a").format(cal.getTime());

        cal.setTimeInMillis(Long.parseLong(startTime) + Long.parseLong(duration));
        String end = new SimpleDateFormat("HH:mm a").format(cal.getTime());

        CharSequence formatted = Phrase.from(context.getResources().getString(R.string.time_span_format))
                .put("start", start)
                .put("end", end)
                .format();

        return String.valueOf(formatted);
    }

    /**
     * @param duration
     * @return "XX h YY m" format
     */
    public static String fmtDuration(Context context, String duration) {
        long dur = Long.parseLong(duration); // in miliseccond
        long hour = TimeUnit.MILLISECONDS.toHours(dur);
        long min;
        long sec;

        if (hour != 0) {
            min = TimeUnit.MILLISECONDS.toMinutes(dur % TimeUnit.HOURS.toMillis(hour));
        } else {
            min = TimeUnit.MILLISECONDS.toMinutes(dur);
        }

        if ((min != 0) && (hour != 0)) {
            sec = TimeUnit.MILLISECONDS.toSeconds((dur % TimeUnit.HOURS.toMillis(hour))
                    % TimeUnit.MINUTES.toMillis(min));
        } else if ((min == 0) && (hour != 0)) {
            sec = TimeUnit.MILLISECONDS.toSeconds(dur % TimeUnit.HOURS.toMillis(hour));
        } else {
            sec = TimeUnit.MILLISECONDS.toSeconds(dur);
        }

        CharSequence formatted = Phrase.from(context.getResources().getString(R.string.duration_format))
                .put("hour", String.valueOf(hour))
                .put("minute", String.valueOf(min))
                .put("second", String.valueOf(sec))
                .format();

        return String.valueOf(formatted);
    }

    public static String fmtSleepNightPct(Context context, String values) {
        CharSequence formatted = Phrase.from(context.getResources()
                .getString(R.string.night_percentage))
                .put("percentage", values)
                .format();
        return String.valueOf(formatted);
    }

    public static String fmtSleepNapPct(Context context, String values) {

        CharSequence formatted = Phrase.from(context.getResources()
                .getString(R.string.nap_percentage))
                .put("percentage", values)
                .format();
        return String.valueOf(formatted);
    }

    public static String fmtSleepNightDrt(Context context, String duration) {
        long dur = Long.parseLong(duration); // in miliseccond
        long hour = TimeUnit.MILLISECONDS.toHours(dur);
        long min;
        long sec;

        if (hour != 0) {
            min = TimeUnit.MILLISECONDS.toMinutes(dur % TimeUnit.HOURS.toMillis(hour));
        } else {
            min = TimeUnit.MILLISECONDS.toMinutes(dur);
        }

        if ((min != 0) && (hour != 0)) {
            sec = TimeUnit.MILLISECONDS.toSeconds((dur % TimeUnit.HOURS.toMillis(hour))
                    % TimeUnit.MINUTES.toMillis(min));
        } else if ((min == 0) && (hour != 0)) {
            sec = TimeUnit.MILLISECONDS.toSeconds(dur % TimeUnit.HOURS.toMillis(hour));
        } else {
            sec = TimeUnit.MILLISECONDS.toSeconds(dur);
        }

        CharSequence formatted = Phrase.from(context.getResources()
                .getString(R.string.night_duration))
                .put("hour", String.valueOf(hour))
                .put("minute", String.valueOf(min))
                .put("second", String.valueOf(sec))
                .format();
        return String.valueOf(formatted);
    }

    public static String fmtSleepNapDrt(Context context, String duration) {
        long dur = Long.parseLong(duration); // in miliseccond
        long hour = TimeUnit.MILLISECONDS.toHours(dur);
        long min;
        long sec;

        if (hour != 0) {
            min = TimeUnit.MILLISECONDS.toMinutes(dur % TimeUnit.HOURS.toMillis(hour));
        } else {
            min = TimeUnit.MILLISECONDS.toMinutes(dur);
        }

        if ((min != 0) && (hour != 0)) {
            sec = TimeUnit.MILLISECONDS.toSeconds((dur % TimeUnit.HOURS.toMillis(hour))
                    % TimeUnit.MINUTES.toMillis(min));
        } else if ((min == 0) && (hour != 0)) {
            sec = TimeUnit.MILLISECONDS.toSeconds(dur % TimeUnit.HOURS.toMillis(hour));
        } else {
            sec = TimeUnit.MILLISECONDS.toSeconds(dur);
        }

        CharSequence formatted = Phrase.from(context.getResources()
                .getString(R.string.nap_duration))
                .put("hour", String.valueOf(hour))
                .put("minute", String.valueOf(min))
                .put("second", String.valueOf(sec))
                .format();
        return String.valueOf(formatted);
    }

    public static String fmtSleepPct(Context context, String values) {
        CharSequence formatted = Phrase.from(context.getResources()
                .getString(R.string.sleep_percentage))
                .put("percentage", values)
                .format();
        return String.valueOf(formatted);
    }

    public static String fmtActivePct(Context context, String values) {
        CharSequence formatted = Phrase.from(context.getResources()
                .getString(R.string.active_percentage))
                .put("percentage", values)
                .format();
        return String.valueOf(formatted);
    }

    public static String fmtSleepDrt(Context context, String duration) {
        long dur = Long.parseLong(duration); // in miliseccond
        long hour = TimeUnit.MILLISECONDS.toHours(dur);
        long min;
        long sec;

        if (hour != 0) {
            min = TimeUnit.MILLISECONDS.toMinutes(dur % TimeUnit.HOURS.toMillis(hour));
        } else {
            min = TimeUnit.MILLISECONDS.toMinutes(dur);
        }

        if ((min != 0) && (hour != 0)) {
            sec = TimeUnit.MILLISECONDS.toSeconds((dur % TimeUnit.HOURS.toMillis(hour))
                    % TimeUnit.MINUTES.toMillis(min));
        } else if ((min == 0) && (hour != 0)) {
            sec = TimeUnit.MILLISECONDS.toSeconds(dur % TimeUnit.HOURS.toMillis(hour));
        } else {
            sec = TimeUnit.MILLISECONDS.toSeconds(dur);
        }
        CharSequence formatted = Phrase.from(context.getResources()
                .getString(R.string.sleep_duration))
                .put("hour", String.valueOf(hour))
                .put("minute", String.valueOf(min))
                .put("second", String.valueOf(sec))
                .format();
        return String.valueOf(formatted);
    }

    public static String fmtActiveDrt(Context context, String duration) {
        long dur = Long.parseLong(duration); // in miliseccond
        long hour = TimeUnit.MILLISECONDS.toHours(dur);
        long min;
        long sec;

        if (hour != 0) {
            min = TimeUnit.MILLISECONDS.toMinutes(dur % TimeUnit.HOURS.toMillis(hour));
        } else {
            min = TimeUnit.MILLISECONDS.toMinutes(dur);
        }

        if ((min != 0) && (hour != 0)) {
            sec = TimeUnit.MILLISECONDS.toSeconds((dur % TimeUnit.HOURS.toMillis(hour))
                    % TimeUnit.MINUTES.toMillis(min));
        } else if ((min == 0) && (hour != 0)) {
            sec = TimeUnit.MILLISECONDS.toSeconds(dur % TimeUnit.HOURS.toMillis(hour));
        } else {
            sec = TimeUnit.MILLISECONDS.toSeconds(dur);
        }

        CharSequence formatted = Phrase.from(context.getResources()
                .getString(R.string.active_duration))
                .put("hour", String.valueOf(hour))
                .put("minute", String.valueOf(min))
                .put("second", String.valueOf(sec))
                .format();
        return String.valueOf(formatted);
    }

    public static String fmtNursingPerDay(Context context, String side, String values, String unit) {
        CharSequence formatted = Phrase.from(context.getResources().getString(R.string.nursing_per_day))
                .put("side", side)
                .put("values", values)
                .put("unit", unit)
                .format();
        return String.valueOf(formatted);
    }

    public static String fmtNursingLastSide(Context context, String side) {
        CharSequence formatted = Phrase.from(context.getResources().getString(R.string.nursing_last_side))
                .put("side", side)
                .format();
        return String.valueOf(formatted);
    }

    public static String fmtNursingPct(Context context, String side, String percentage) {
        CharSequence formatted = Phrase.from(context.getResources()
                .getString(R.string.nursing_percentage_format))
                .put("side", side)
                .put("percentage", percentage)
                .format();
        return String.valueOf(formatted);
    }

    public static String fmtDiaperTotalToday(Context context, String values) {
        CharSequence formatted = Phrase.from(context.getResources()
                .getString(R.string.activity_today))
                .put("value", values)
                .format();
        return String.valueOf(formatted);
    }

    public static String fmtDiaperLastActivity(Context context, String numberToday){
        String value = DateUtils.getRelativeTimeSpanString(Long.parseLong(numberToday)).toString();
        CharSequence formatted = Phrase.from(context.getResources()
                .getString(R.string.activity_last))
                .put("value", value)
                .format();
        return String.valueOf(formatted);
    }

    public static String fmtAge(Context context, long dob, long now) {
        long duration = Math.abs(now - dob);
        long days = duration / DAY_MILLIS;

        // TODO: to show correct month and year different

        CharSequence formatted = Phrase.from(context.getResources().getString(R.string.age_format))
                .put("day", String.valueOf(days))
                .format();
        return String.valueOf(formatted);
    }

    public static String fmtLastActivity(Context context, String activity, String time) {
        CharSequence formatted = Phrase.from(context.getResources().getString(R.string.last_activity_format))
                .put("activity", activity)
                .put("time", time)
                .format();
        return String.valueOf(formatted);
    }

    public static String fmtDate(Context context, String timeStamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(timeStamp));
        String day = DAY_OF_WEEK_SHORT[cal.get(Calendar.DAY_OF_WEEK) - 1];
        String date = String.valueOf(cal.get(Calendar.DATE));
        String month = MONTH_OF_YEAR[cal.get(Calendar.MONTH)];
        String year = String.valueOf(cal.get(Calendar.YEAR));

        CharSequence formatted = Phrase.from(context.getResources()
                .getString(R.string.date_complete_format))
                .put("day", day)
                .put("date", date)
                .put("month", month)
                .put("year", year)
                .format();
        return String.valueOf(formatted);
    }

    public static String fmtDayOnly(Context context, String timeStamp){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(timeStamp));
        String day = DAY_OF_WEEK_COMPLETE[cal.get(Calendar.DAY_OF_WEEK) - 1];
        CharSequence formatted = Phrase.from(context.getResources()
                .getString(R.string.day_only_format))
                .put("day", day)
                .format();
        return String.valueOf(formatted);
    }

    public static String fmtDateOnly(Context context, String timeStamp){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(timeStamp));
        String date = String.valueOf(cal.get(Calendar.DATE));
        String month = MONTH_OF_YEAR[cal.get(Calendar.MONTH)];
        String year = String.valueOf(cal.get(Calendar.YEAR));

        CharSequence formatted = Phrase.from(context.getResources()
                .getString(R.string.date_simple_format))
                .put("date", date)
                .put("month", month)
                .put("year", year)
                .format();
        return String.valueOf(formatted);
    }

    public static String fmtTime(Context context, String timeStamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(timeStamp));
        return new SimpleDateFormat("HH:mm").format(cal.getTime());
    }

    public static boolean isValidNumber(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
