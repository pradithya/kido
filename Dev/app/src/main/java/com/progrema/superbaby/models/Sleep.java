package com.progrema.superbaby.models;

import android.os.Parcel;

/**
 * Created by iqbalpakeh on 22/1/14.
 * @author aria
 * @author iqbalpakeh
 */
public class Sleep extends BaseActivity
{

    private long duration;

    void Sleep(Parcel parcel)
    {
        readFromParcel(parcel);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        super.writeToParcel(parcel,i);
        parcel.writeLong(duration);
    }

    public void readFromParcel(Parcel parcel)
    {
        super.readFromParcel(parcel);
        duration = parcel.readLong();
    }

    public long getDuration()
    {
        return duration;
    }

    public void setDuration(long duration)
    {
        this.duration = duration;
    }
}
