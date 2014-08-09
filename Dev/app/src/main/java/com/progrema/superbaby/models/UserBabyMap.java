package com.progrema.superbaby.models;

import android.content.ContentValues;
import android.content.Context;
import android.os.Parcel;

import com.progrema.superbaby.provider.BabyLogContract;

public class UserBabyMap extends BaseModel {

    private long userId;
    private long babyId;

    public UserBabyMap() {
    }

    public UserBabyMap(Parcel parcel) {
        readFromParcel(parcel);
    }

    public void readFromParcel(Parcel parcel) {
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
        super.writeToParcel(parcel, i);
        parcel.writeLong(userId);
        parcel.writeLong(babyId);
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getBabyId() {
        return babyId;
    }

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

    @Override
    public void edit(Context context) {

    }
}
