package com.progrema.superbaby.models;

import android.os.Parcel;

/**
 * Created by aria on 25/1/14.
 */
abstract public class BaseActor extends BaseModel
{

    protected String name;
    protected Sex sex;

    public BaseActor()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Sex getSex()
    {
        return sex;
    }

    public void setSex(Sex sex)
    {
        this.sex = sex;
    }

    public enum Sex
    {
        MALE("MALE"),
        FEMALE("FEMALE");

        private String title;
        Sex(String title)
        {
            this.title = title;
        }

        public String getTitle()
        {
            return this.title;
        }
    }

    /**
     * Called from constructor to create this object from parcel
     *
     * @param parcel parcel from which to re-create object
     */
    public void readFromParcel(Parcel parcel)
    {
        // read each field parcel the order that it
        // was written to the parcel
        super.readFromParcel(parcel);
        name = parcel.readString();
        sex = Sex.valueOf(parcel.readString());
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {

        // write each field into the parcel
        super.writeToParcel(parcel,i);
        parcel.writeString(name);
        parcel.writeString(sex.getTitle()); //time in milli second format

    }
}
