package com.progrema.superbaby.models;

import android.content.ContentValues;
import android.content.Context;
import android.os.Parcel;

import com.progrema.superbaby.provider.BabyLogContract;

/**
 * Created by iqbalpakeh on 22/1/14.
 */
public class UserBabyMap extends BaseModel implements IDBServices
{
    private long userId;
    private long babyId;

    public UserBabyMap()
    {
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
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
    public void insert(Context context)
    {
        ContentValues values = new ContentValues();
        values.put(BabyLogContract.UserBabyMap.BABY_ID, getBabyId());
        values.put(BabyLogContract.UserBabyMap.USER_ID, getUserId());
        context.getContentResolver().insert(BabyLogContract.UserBabyMap.CONTENT_URI, values);
    }

    @Override
    public void delete(Context context)
    {
    }
}
