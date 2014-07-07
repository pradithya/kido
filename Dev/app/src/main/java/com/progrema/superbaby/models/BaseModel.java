package com.progrema.superbaby.models;

import android.os.Parcel;
import android.os.Parcelable;

abstract public class BaseModel implements Parcelable, ProviderServices {

    protected long activityId;

    public BaseModel() {
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(activityId);
    }

    public void readFromParcel(Parcel parcel) {
        activityId = parcel.readLong();
    }
}
