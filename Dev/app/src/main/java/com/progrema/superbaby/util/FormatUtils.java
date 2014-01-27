package com.progrema.superbaby.util;

import android.content.Context;
import com.progrema.superbaby.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for formatting output
 * Created by aria on 26/1/14.
 */
public class FormatUtils
{


    private static final String[] DAY_OF_WEEK = {"MON","TUE","WED","THU","FRI","SAT","SUN"};
    private static final String[] MONTH_OF_YEAR = {"Jan","Feb","Mar","Apr","May","Jun",
            "Jul","Aug","Sep","Oct","Nov","Dec"};
    /**
     *
     * @param startTime in timemillis format
     * @param duration in timemillis format
     * @return "HH:MM - HH:MM" format
     */
    public static String formatTimeboundary (String startTime, String duration)
    {
        String retVal = "";
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(startTime));
        String start = new SimpleDateFormat("HH:mm").format(cal.getTime());

        cal.setTimeInMillis(Long.parseLong(startTime) + Long.parseLong(duration));
        String end = new SimpleDateFormat("HH:mm").format(cal.getTime());

        retVal = retVal.concat(start + " - " + end);

        return retVal;
    }

    /**
     *
     * @param duration
     * @return "XX h YY m" format
     */
    public static String formatDuration (Context context, String duration)
    {
        String retVal = "";
        long dur = Long.parseLong(duration); // in miliseccond
        long hour = TimeUnit.MILLISECONDS.toHours(dur);
        long min;
        if (hour != 0)
        {
             min = TimeUnit.MILLISECONDS.toMinutes(dur % TimeUnit.HOURS.toMillis(hour));
        }
        else
        {
             min = TimeUnit.MILLISECONDS.toMinutes(dur);
        }

        retVal = retVal.concat (String.valueOf(hour) + " "
                + context.getResources().getString(R.string.hour_abreviation) + " "
                + String.valueOf(min) + " "
                + context.getResources().getString(R.string.minute_abreviation));

        return retVal;
    }

    public static String formatTimeStamp(String timeStamp)
    {
        String retVal = "";
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(timeStamp));
        String day = DAY_OF_WEEK[cal.get(Calendar.DAY_OF_WEEK)];
        String date = String.valueOf(cal.get(Calendar.DATE));
        String month = MONTH_OF_YEAR[cal.get(Calendar.MONTH)];
        String year = String.valueOf(cal.get(Calendar.YEAR));

        retVal = retVal.concat(day + ", " + date + " " + month + " " + year);

        return retVal;
    }
}
