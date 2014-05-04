package com.progrema.superbaby.models;

import android.content.ContentValues;
import android.content.Context;
import android.os.Parcel;

import com.progrema.superbaby.provider.BabyLogContract;

public class UserBabyMap extends BaseModel {
    /**
     * User-Baby map private data
     */
    private long userId;
    private long babyId;

    /**
     * Standard basic constructor for non-parcel
     * object creation
     */
    public UserBabyMap() {
    }

    /**
     * Called from constructor to create this object from parcel
     *
     * @param parcel parcel from which to re-create object
     */
    public UserBabyMap(Parcel parcel) {
        readFromParcel(parcel);
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
        userId = parcel.readLong();
        babyId = parcel.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        // write each field into the parcel
        super.writeToParcel(parcel, i);
        parcel.writeLong(userId);
        parcel.writeLong(babyId);
    }

    /**
     * get user id
     *
     * @return user id
     */
    public long getUserId() {
        return userId;
    }

    /**
     * set user id
     *
     * @param userId user id
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * get baby id
     *
     * @return baby id
     */
    public long getBabyId() {
        return babyId;
    }

    /**
     * set baby id
     *
     * @param babyId baby id
     */
    public void setBabyId(long babyId) {
        this.babyId = babyId;
    }

    @Override
    public void insert(Context context) {
        ContentValues values = new ContentValues();
        values.put(BabyLogContract.UserBabyMap.BABY_ID, getBabyId());
        values.put(BabyLogContract.UserBabyMap.USER_ID, getUserId());
        context.getContentResolver().insert(BabyLogContract.UserBabyMap.CONTENT_URI, values);
    }

    @Override
    public void delete(Context context) {
    }
}
