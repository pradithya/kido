package com.progrema.superbaby.widget.stopwatch;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.Chronometer;

/**
 * Created by iqbalpakeh on 30/1/14.
 */
public class Stopwatch extends Chronometer implements TimerService
{
    private long miliSeconds;
    private long timeWhenStop;

    public Stopwatch(Context context)
    {
        super(context);
        miliSeconds = 0;
        timeWhenStop = 0;
    }

    public Stopwatch(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        miliSeconds = 0;
        timeWhenStop = 0;
    }

    public Stopwatch(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        miliSeconds = 0;
        timeWhenStop = 0;
    }

    @Override
    public long getDuration()
    {
        String value;
        String[] parts;
        long seconds;
        long minutes;
        long hours;

        value = super.getText().toString();
        parts = value.split(":");
        seconds = 0;
        minutes = 0;
        hours = 0;

        // wrong format
        if (parts.length < 2 || parts.length > 3)
        {
            return 0;
        }

        if (parts.length == 2)
        {
            seconds = Integer.parseInt(parts[1]);
            minutes = Integer.parseInt(parts[0]);
        }
        else if (parts.length == 3)
        {
            seconds = Integer.parseInt(parts[2]);
            minutes = Integer.parseInt(parts[1]);
            hours = Integer.parseInt(parts[0]);
        }

        return seconds + (minutes * 60) + (hours * 3600);
    }

    @Override
    public void start()
    {
        super.setBase(SystemClock.elapsedRealtime() + timeWhenStop);
        super.start();
    }

    @Override
    public void stop()
    {
        timeWhenStop = super.getBase() - SystemClock.elapsedRealtime();
        super.stop();
    }

    @Override
    public void reset()
    {
        super.setBase(SystemClock.elapsedRealtime());
        timeWhenStop = 0;
        super.start();
    }
}
