package com.progrema.superbaby.models;

import android.content.ContentValues;
import android.content.Context;
import android.os.Parcel;

import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.ActiveContext;

public class Sleep extends BaseActivity {
    private long duration;

    /**
     * Empty Constructor
     */
    public Sleep() {
    }

    public Sleep(Parcel parcel) {
        readFromParcel(parcel);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeLong(duration);
    }

    public void readFromParcel(Parcel parcel) {
        super.readFromParcel(parcel);
        duration = parcel.readLong();
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public void insert(Context context) {
        ContentValues values = new ContentValues();
        values.put(BabyLogContract.Sleep.BABY_ID, getBabyID());
        values.put(BabyLogContract.Sleep.TIMESTAMP, getTimeStampInString());
        values.put(BabyLogContract.Sleep.DURATION, getDuration());
        context.getContentResolver().insert(BabyLogContract.Sleep.CONTENT_URI, values);
    }

    @Override
    public void delete(Context context) {
        String [] selectionArgs = {
                String.valueOf(ActiveContext.getActiveBaby(context).getID()),
                String.valueOf(getID())};

        context.getContentResolver().delete(
                BabyLogContract.Sleep.CONTENT_URI,
                "baby_id = ? AND _id = ?",
                selectionArgs);
    }
}
