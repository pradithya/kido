package com.progrema.superbaby.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by iqbalpakeh on 20/1/14.
 */
abstract public class BaseModel implements Parcelable{

    protected long ID;

    public BaseModel(){

    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public void writeToParcel(Parcel parcel, int i){
        parcel.writeLong(ID);
    }

    public void readFromParcel(Parcel parcel){
        ID = parcel.readLong();
    }
}
