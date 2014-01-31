package com.progrema.superbaby.models;

import android.os.Parcel;

/**
 * Created by iqbalpakeh on 22/1/14.
 * @author aria
 */
public class Nursing extends BaseActivity implements DBServices
{

    private long duration;
    private NursingType type;
    private float volume;

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

    Nursing (Parcel parcel)
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

    public float getVolume()
    {
        return volume;
    }

    public void setVolume(float volume)
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
        super.writeToParcel(parcel,i);
        parcel.writeLong(duration);
        parcel.writeString(type.getTitle());
        parcel.writeFloat(volume);
    }

    @Override
    public void readFromParcel(Parcel parcel)
    {
        super.readFromParcel(parcel);
        duration = parcel.readLong();
        type = NursingType.valueOf(parcel.readString());
        volume = parcel.readFloat();
    }

    @Override
    public void insert()
    {
    }

    @Override
    public void delete()
    {
    }
}
