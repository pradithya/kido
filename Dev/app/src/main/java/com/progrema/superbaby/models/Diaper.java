package com.progrema.superbaby.models;

import android.os.Parcel;

import java.util.Calendar;

/**
 * Created by iqbalpakeh on 22/1/14.
 * @author aria
 * @author iqbalpakeh
 */
public class Diaper extends BaseModel{

    private String activityID;
    private String timeStamp;
    private DiaperType type;

    public enum DiaperType {
        POO("POO"),
        PEE("PEE"),
        MIXED("MIXED");

        private String title;
        DiaperType(String title){
            this.title = title;
        }

        public String getTitle(){
            return this.title;
        }
    }

    /**
     * Called from constructor to create this object from parcel
     *
     * @param parcel parcel from which to re-create object
     */
    public void readFromParcel(Parcel parcel){
        activityID = parcel.readString();
        timeStamp = parcel.readString();
        type = DiaperType.valueOf(parcel.readString());
    }

    public String getActivityID(){
        return this.activityID;
    }

    public String getTimeStampInString(){
        return timeStamp;
    }

    public Calendar getTimeStampInCalendar(){
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(Long.parseLong(timeStamp));
        return time;
    }

    public DiaperType getType(){
        return type;
    }

    public void setTimeStamp(Calendar newTime){
        timeStamp = String.valueOf(newTime.getTimeInMillis());
    }

    public void setTimeStamp(String newTime){
        this.timeStamp = newTime;
    }

    public void setType(DiaperType newType){
        this.type = newType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(activityID);
        parcel.writeString(timeStamp);
        parcel.writeString(type.getTitle());
    }


}
