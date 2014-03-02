package com.progrema.superbaby.util;

import android.content.Context;

import com.progrema.superbaby.R;
import com.squareup.phrase.Phrase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for input/output formatting
 * Created by aria on 26/1/14.
 */
public class FormatUtils
{
    public final static long SECOND_MILLIS = 1000;
    public final static long MINUTE_MILLIS = SECOND_MILLIS * 60;
    public final static long HOUR_MILLIS = MINUTE_MILLIS * 60;
    public final static long DAY_MILLIS = HOUR_MILLIS * 24;

    private static final String[] DAY_OF_WEEK = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private static final String[] MONTH_OF_YEAR = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    /**
     * @param startTime in timemillis format
     * @param duration  in timemillis format
     * @return "HH:MM - HH:MM" format
     */
    public static String formatTimeBoundary(Context context, String startTime, String duration)
    {
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
    public static String formatDuration(Context context, String duration)
    {
        long dur = Long.parseLong(duration); // in miliseccond
        long hour = TimeUnit.MILLISECONDS.toHours(dur);
        long min;
        long sec;
        if (hour != 0)
        {
            min = TimeUnit.MILLISECONDS.toMinutes(dur % TimeUnit.HOURS.toMillis(hour));
        }
        else
        {
            min = TimeUnit.MILLISECONDS.toMinutes(dur);
        }

        if ((min != 0) && (hour != 0))
        {
            sec = TimeUnit.MILLISECONDS.toSeconds(dur % TimeUnit.MINUTES.toMillis(min));
        }
        else if ((min == 0) && (hour != 0))
        {
            sec = TimeUnit.MILLISECONDS.toSeconds(dur % TimeUnit.HOURS.toMillis(hour));
        }
        else
        {
            sec = TimeUnit.MILLISECONDS.toSeconds(dur);
        }

        CharSequence formatted = Phrase.from(context.getResources().getString(R.string.duration_format))
                .put("hour", String.valueOf(hour))
                .put("minute", String.valueOf(min))
                .put("second", String.valueOf(sec))
                .format();

        return String.valueOf(formatted);
    }

    public static String formatNursingPerDay(Context context, String side, String values, String unit)
    {
        CharSequence formatted = Phrase.from(context.getResources().getString(R.string.nursing_per_day))
                .put("side", side)
                .put("values", values)
                .put("unit", unit)
                .format();
        return String.valueOf(formatted);
    }

    public static String formatNursingLastSide(Context context, String side)
    {
        CharSequence formatted = Phrase.from(context.getResources().getString(R.string.nursing_last_side))
                .put("side", side)
                .format();
        return String.valueOf(formatted);
    }

    public static String formatNursingPercentage(Context context, String side, String percentage)
    {
        CharSequence formatted = Phrase.from(context.getResources().getString(R.string.nursing_percentage_format))
                .put("side", side)
                .put("percentage", percentage)
                .format();
        return String.valueOf(formatted);
    }

    public static String formatAge(Context context, long dob, long now)
    {
        long duration = Math.abs(now - dob);
        long days = duration / DAY_MILLIS;

        // TODO: to show correct month and year different

        CharSequence formatted = Phrase.from(context.getResources().getString(R.string.age_format))
                .put("day", String.valueOf(days))
                .format();
        return String.valueOf(formatted);
    }

    public static String formatLastActivity(Context context, String activity, String time)
    {
        CharSequence formatted = Phrase.from(context.getResources().getString(R.string.last_activity_format))
                .put("activity", activity)
                .put("time", time)
                .format();
        return String.valueOf(formatted);
    }

    public static String formatDate(Context context, String timeStamp)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(timeStamp));
        String day = DAY_OF_WEEK[cal.get(Calendar.DAY_OF_WEEK) - 1];
        String date = String.valueOf(cal.get(Calendar.DATE));
        String month = MONTH_OF_YEAR[cal.get(Calendar.MONTH)];
        String year = String.valueOf(cal.get(Calendar.YEAR));

        CharSequence formatted = Phrase.from(context.getResources().getString(R.string.date_format))
                .put("day", day)
                .put("date", date)
                .put("month", month)
                .put("year", year)
                .format();
        return String.valueOf(formatted);
    }

    public static String formatTime(Context context, String timeStamp)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(timeStamp));
        return new SimpleDateFormat("HH:mm a").format(cal.getTime());
    }

    public static boolean isValidNumber(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch (NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
}
