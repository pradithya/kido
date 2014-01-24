package com.progrema.superbaby.models;

import android.os.Parcel;

/**
 * Created by iqbalpakeh on 22/1/14.
 */
public class Sleep extends BaseModel{

    /**
     * Sleep properties
     */
    private int activityId;
    private int babyId;
    private String timestamp;
    private String duration;

    /**
     * Standard basic constructor for non-parcel
     * object creation
     *
     */
    public Sleep(){
        super();
    }

    /**
     * Constructor to use when re-constructing object
     * from a parcel
     *
     * @param parcel parcel instance
     */
    public Sleep(Parcel parcel){
        readFromParcel(parcel);
    }

    /**
     * Called from constructor to create this object from parcel
     *
     * @param parcel parcel from which to re-create object
     */
    private void readFromParcel(Parcel parcel){

        // read each field parcel the order that it
        // was written to the parcel
        activityId = parcel.readInt();
        babyId = parcel.readInt();
        timestamp = parcel.readString();
        duration = parcel.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        // write each field into the parcel
        parcel.writeInt(activityId);
        parcel.writeInt(activityId);
        parcel.writeString(timestamp);
        parcel.writeString(duration);

    }

    /**
     * This field is needed by Android to create new objects
     * individually or as arrays
     */
    public static final Creator CREATOR = new Creator<Sleep>(){
        @Override
        public Sleep createFromParcel(Parcel parcel) {
            return new Sleep(parcel);
        }

        @Override
        public Sleep[] newArray(int size) {
            return new Sleep[size];
        }
    };

    /**
     * Get Activity Id property
     *
     * @return activityId
     */
    public int getActivityId() {
        return activityId;
    }

    /**
     * Set activity id property
     *
     * @param activityId activity id property
     */
    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    /**
     * Get baby id property
     *
     * @return babyId
     */
    public int getBabyId() {
        return babyId;
    }

    /**
     * Set baby id property
     *
     * @param babyId baby id property
     */
    public void setBabyId(int babyId) {
        this.babyId = babyId;
    }

    /**
     * Get timestamp property
     *
     * @return timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Set time stamp property
     *
     * @param timestamp
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Get duration property
     *
     * @return duration
     */
    public String getDuration() {
        return duration;
    }

    /**
     * Set duration property
     *
     * @param duration duration property
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }
}
