package com.progrema.superbaby.models;

import android.os.Parcel;
import android.os.Parcelable;

abstract public class BaseModel implements Parcelable, ProviderServices {
    protected long ID;

    public BaseModel() {
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(ID);
    }

    public void readFromParcel(Parcel parcel) {
        ID = parcel.readLong();
    }
}
