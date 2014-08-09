package com.progrema.superbaby.models;

import android.content.ContentValues;
import android.content.Context;
import android.os.Parcel;

import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.ActiveContext;
import com.progrema.superbaby.util.FormatUtils;

import java.util.Calendar;
import java.util.Date;

public class Baby extends BaseActor {

    public static final Creator CREATOR = new Creator<Baby>() {
        @Override
        public Baby createFromParcel(Parcel parcel) {
            return new Baby(parcel);
        }
        @Override
        public Baby[] newArray(int size) {
            return new Baby[size];
        }
    };

    private Calendar birthday;

    public Baby() {
    }

    public Baby(Parcel parcel) {
        readFromParcel(parcel);
    }

    public void readFromParcel(Parcel parcel) {
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
        super.writeToParcel(parcel, i);
        parcel.writeString(String.valueOf(birthday.getTimeInMillis()));
    }

    public String getBirthdayInString() {
        return String.valueOf(birthday.getTimeInMillis());
    }

    public String getAgeInReadableFormat(Context context) {
        return FormatUtils.fmtAge(context,
                birthday.getTimeInMillis(), Calendar.getInstance().getTimeInMillis());
    }

    public String getBirthdayInReadableFormat(Context context) {
        return FormatUtils.fmtDate(context, getBirthdayInString());
    }

    public Calendar getBirthdayInCalendar() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = Calendar.getInstance();
        this.birthday.setTimeInMillis(Long.valueOf(birthday));
    }

    public void setBirthday(Date dateOfBirth) {
        birthday.setTime(dateOfBirth);
    }

    @Override
    public void insert(Context context) {
        User user = ActiveContext.getActiveUser(context);
        ContentValues values = new ContentValues();
        values.put(BabyLogContract.Baby.NAME, getName());
        values.put(BabyLogContract.Baby.BIRTHDAY, getBirthdayInString());
        values.put(BabyLogContract.Baby.SEX, getSex().getTitle());
        values.put(BabyLogContract.UserBabyMap.USER_ID, user.getActivityId());
        context.getContentResolver().insert(BabyLogContract.Baby.CONTENT_URI, values);
    }

    @Override
    public void delete(Context context) {
    }

    @Override
    public void edit(Context context) {

    }
}
