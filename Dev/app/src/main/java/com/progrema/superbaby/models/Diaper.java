package com.progrema.superbaby.models;

import android.content.ContentValues;
import android.content.Context;
import android.os.Parcel;

import com.progrema.superbaby.provider.BabyLogContract;

/**
 * Created by iqbalpakeh on 22/1/14.
 *
 * @author aria
 * @author iqbalpakeh
 */
public class Diaper extends BaseActivity
{
    public final static String DIAPER_TYPE_KEY = "diaper_type";
    private DiaperType type;

    /**
     * enumeration for diaper type
     * use this instead of directly referring to the string value
     * to get the string value use .getTitle method
     * to convert a  string to one of the enumeration enlisted here, use : DiaperType.valueOf(String s)
     */
    public enum DiaperType
    {
        DRY("DRY"),
        WET("WET"),
        MIXED("MIXED");

        private String title;

        DiaperType(String title)
        {
            this.title = title;
        }

        public String getTitle()
        {
            return this.title;
        }
    }

    public DiaperType getType()
    {
        return type;
    }

    public void setType(DiaperType newType)
    {
        this.type = newType;
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
        parcel.writeString(type.getTitle());
    }

    /**
     * Called from constructor to create this object from parcel
     *
     * @param parcel parcel from which to re-create object
     */
    @Override
    public void readFromParcel(Parcel parcel)
    {
        super.readFromParcel(parcel);
        type = DiaperType.valueOf(parcel.readString());
    }

    @Override
    public void insert(Context context)
    {
        ContentValues values = new ContentValues();
        values.put(BabyLogContract.Diaper.BABY_ID, getBabyID());
        values.put(BabyLogContract.Diaper.TIMESTAMP, getTimeStampInString());
        values.put(BabyLogContract.Diaper.TYPE, getType().getTitle());
        context.getContentResolver().insert(BabyLogContract.Diaper.CONTENT_URI, values);
    }

    @Override
    public void delete(Context context)
    {
    }
}
