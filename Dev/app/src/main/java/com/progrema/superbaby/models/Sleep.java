package com.progrema.superbaby.models;

import android.content.ContentValues;
import android.content.Context;
import android.os.Parcel;
import com.progrema.superbaby.provider.BabyLogContract;

public class Sleep extends BaseActivity implements DBServices
{
    private long duration;

    public Sleep(Context context)
    {
        this.context = context;
    }

    void Sleep(Context context, Parcel parcel)
    {
        this.context = context;
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

    @Override
    public void insert()
    {
        ContentValues values = new ContentValues();
        //TODO: how to get the value of ActivityId??
        values.put(BabyLogContract.Sleep.BABY_ID, getBabyID());
        values.put(BabyLogContract.Sleep.TIMESTAMP, getTimeStampInString());
        values.put(BabyLogContract.Sleep.DURATION, getDuration());
        context.getContentResolver().insert(BabyLogContract.Sleep.CONTENT_URI, values);
    }

    @Override
    public void delete()
    {
    }
}
