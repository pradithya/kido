package com.progrema.superbaby.models;

import android.os.Parcel;

/**
 * Created by aria on 25/1/14.
 */
abstract public class BaseActor extends BaseModel {
    /**
     * Protected field of BaseActor class
     */
    protected String name;
    protected Sex sex;

    /**
     * BaseActor constructor
     */
    public BaseActor() {
    }

    /**
     * get actor name
     *
     * @return actor's name
     */
    public String getName() {
        return name;
    }

    /**
     * set actor name
     *
     * @param name actor's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get actor's sex type
     *
     * @return actor's sex type
     */
    public Sex getSex() {
        return sex;
    }

    /**
     * Set actor's sex type
     *
     * @param sex
     */
    public void setSex(Sex sex) {
        this.sex = sex;
    }

    /**
     * Called from constructor to create this object from parcel
     *
     * @param parcel parcel from which to re-create object
     */
    public void readFromParcel(Parcel parcel) {
        // read each field parcel the order that it
        // was written to the parcel
        super.readFromParcel(parcel);
        name = parcel.readString();
        sex = Sex.valueOf(parcel.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        // write each field into the parcel
        super.writeToParcel(parcel, i);
        parcel.writeString(name);
        parcel.writeString(sex.getTitle()); //time in milli second format

    }

    /**
     * Sex Type
     */
    public enum Sex {
        MALE("Boy"),
        FEMALE("Girl");

        private String title;

        Sex(String title) {
            this.title = title;
        }

        public String getTitle() {
            return this.title;
        }
    }
}
