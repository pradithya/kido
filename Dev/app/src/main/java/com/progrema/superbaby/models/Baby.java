package com.progrema.superbaby.models;

import android.os.Parcel;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by iqbalpakeh on 20/1/14.
 */
public class Baby extends BaseActor {

    /**
     * Baby privates data
     */
    private Calendar birthday;

    /**
     * Standard basic constructor for non-parcel
     * object creation
     *
     */
    public Baby(){
    }

    /**
     * Constructor to use when re-constructing object
     * from a parcel
     *
     * @param parcel parcel instance
     */
    public Baby(Parcel parcel){
        readFromParcel(parcel);
    }

    /**
     * Called from constructor to create this object from parcel
     *
     * @param parcel parcel from which to re-create object
     */
    public void readFromParcel(Parcel parcel){

        // read each field parcel the order that it
        // was written to the parcel
        super.readFromParcel(parcel);
        birthday = Calendar.getInstance();
        birthday.setTimeInMillis(Long.valueOf(parcel.readString()));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        // write each field into the parcel
        super.writeToParcel(parcel,i);
        parcel.writeString(String.valueOf(birthday.getTimeInMillis())); //in timeinmillis format

    }

    public static final Creator CREATOR = new Creator<Baby>(){
        @Override
        public Baby createFromParcel(Parcel parcel) {
            return new Baby(parcel);
        }

        @Override
        public Baby[] newArray(int size) {
            return new Baby[size];
        }
    };

    public String getBirthdayInString() {
        return birthday.toString();
    }

    public Calendar getBirthdayInCalendar() {
       return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = Calendar.getInstance();
        this.birthday.setTimeInMillis(Long.valueOf(birthday));
    }

    public void setBirthday(Date dateOfBirth){
        birthday.setTime(dateOfBirth);
    }

}