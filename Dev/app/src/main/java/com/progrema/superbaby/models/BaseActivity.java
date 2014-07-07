package com.progrema.superbaby.models;

import android.os.Parcel;

import java.util.Calendar;

abstract public class BaseActivity extends BaseModel {

    protected long babyID;
    protected String timeStamp;

    public BaseActivity() {
    }

    public BaseActivity(Parcel parcel) {
        readFromParcel(parcel);
    }

    public BaseActivity(long babyID, String newTime) {
        this.babyID = babyID;
        this.timeStamp = newTime;
    }

    public BaseActivity(long babyID, Calendar newTime) {
        this.babyID = babyID;
        this.timeStamp = String.valueOf(newTime.getTimeInMillis());
    }

    public long getBabyID() {
        return babyID;
    }

    public void setBabyID(long babyID) {
        this.babyID = babyID;
    }

    public void setTimeStamp(Calendar newTime) {
        timeStamp = String.valueOf(newTime.getTimeInMillis());
    }

    public void setTimeStamp(String newTime) {
        this.timeStamp = newTime;
    }

    public String getTimeStampInString() {
        return timeStamp;
    }

    public Calendar getTimeStampInCalendar() {
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(Long.parseLong(timeStamp));
        return time;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeLong(babyID);
        parcel.writeString(timeStamp);
    }

    public void readFromParcel(Parcel parcel) {
        super.readFromParcel(parcel);
        babyID = parcel.readLong();
        timeStamp = parcel.readString();
    }
}
