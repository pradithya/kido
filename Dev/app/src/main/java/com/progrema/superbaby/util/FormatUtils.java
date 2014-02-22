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


    private static final String[] DAY_OF_WEEK = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
    private static final String[] MONTH_OF_YEAR = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    /**
     * @param startTime in timemillis format
     * @param duration  in timemillis format
     * @return "HH:MM - HH:MM" format
     */
    public static String formatTimeBoundary(Context context, String startTime, String duration)
    {
        String retVal = "";
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(startTime));
        String start = new SimpleDateFormat("HH:mm a").format(cal.getTime());


        cal.setTimeInMillis(Long.parseLong(startTime) + Long.parseLong(duration));
        String end = new SimpleDateFormat("HH:mm a").format(cal.getTime());

        CharSequence formatted = Phrase.from(context.getResources().getString(R.string.time_span_format))
                .put("start", start)
                .put("end", end)
                .format();

        retVal = String.valueOf(formatted);
        return retVal;
    }

    /**
     * @param duration
     * @return "XX h YY m" format
     */
    public static String formatDuration(Context context, String duration)
    {
        String retVal = "";
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

        retVal = String.valueOf(formatted);
        return retVal;
    }

    public static String formatDate(Context context, String timeStamp)
    {
        String retVal = "";
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

        retVal = String.valueOf(formatted);
        return retVal;
    }

    public static String formatTime(Context context, String timeStamp)
    {
        String retVal = "";
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(timeStamp));
        retVal = new SimpleDateFormat("HH:mm a").format(cal.getTime());

        return retVal;
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
