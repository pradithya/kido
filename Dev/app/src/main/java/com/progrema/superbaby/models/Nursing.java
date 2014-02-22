package com.progrema.superbaby.models;

import android.content.ContentValues;
import android.content.Context;
import android.os.Parcel;

import com.progrema.superbaby.provider.BabyLogContract;

/**
 * Created by iqbalpakeh on 22/1/14.
 *
 * @author aria
 */
public class Nursing extends BaseActivity implements IDBServices
{

    private long duration;
    private NursingType type;
    private long volume;
    public final static String NURSING_TYPE_KEY = "type";
    public final static String FORMULA_VOLUME_KEY = "volume";

    public enum NursingType
    {
        LEFT("LEFT"),
        RIGHT("RIGHT"),
        FORMULA("FORMULA");

        private String title;

        NursingType(String title)
        {
            this.title = title;
        }

        public String getTitle()
        {
            return this.title;
        }
    }

    public Nursing()
    {

    }

    public Nursing(Parcel parcel)
    {
        readFromParcel(parcel);
    }

    public long getDuration()
    {
        return duration;
    }

    public void setDuration(long duration)
    {
        this.duration = duration;
    }

    public NursingType getType()
    {
        return type;
    }

    public void setType(NursingType type)
    {
        this.type = type;
    }

    public long getVolume()
    {
        return volume;
    }

    public void setVolume(long volume)
    {
        this.volume = volume;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        super.writeToParcel(parcel, i);
        parcel.writeLong(duration);
        parcel.writeString(type.getTitle());
        parcel.writeLong(volume);
    }

    @Override
    public void readFromParcel(Parcel parcel)
    {
        super.readFromParcel(parcel);
        duration = parcel.readLong();
        type = NursingType.valueOf(parcel.readString());
        volume = parcel.readLong();
    }

    @Override
    public void insert(Context context)
    {
        ContentValues values = new ContentValues();
        values.put(BabyLogContract.Nursing.BABY_ID, getBabyID());
        values.put(BabyLogContract.Nursing.TIMESTAMP, getTimeStampInString());
        values.put(BabyLogContract.Nursing.SIDES, getType().getTitle());
        values.put(BabyLogContract.Nursing.DURATION, getDuration());
        values.put(BabyLogContract.Nursing.VOLUME, getVolume());
        context.getContentResolver().insert(BabyLogContract.Nursing.CONTENT_URI, values);
    }

    @Override
    public void delete(Context context)
    {
    }
}
