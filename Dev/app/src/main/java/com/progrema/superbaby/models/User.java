package com.progrema.superbaby.models;

import android.content.ContentValues;
import android.content.Context;
import android.os.Parcel;

import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.SecurityUtils;

public class User extends BaseActor {

    public static final Creator CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel parcel) {
            return new User(parcel);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private String password;
    private String securityQuestion;
    private String securityAnswer;

    public User() {
    }

    public User(Parcel parcel) {
        readFromParcel(parcel);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(password);
        parcel.writeString(securityQuestion);
        parcel.writeString(securityAnswer);
    }

    public void readFromParcel(Parcel parcel) {
        super.readFromParcel(parcel);
        password = parcel.readString();
        securityQuestion = parcel.readString();
        securityAnswer = parcel.readString();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String inputPlainText) {
        this.password = SecurityUtils.computeSHA1(inputPlainText);
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String inputPlainText) {
        this.securityAnswer = SecurityUtils.computeSHA1(inputPlainText);
    }

    @Override
    public void insert(Context context) {
        ContentValues values = new ContentValues();
        values.put(BabyLogContract.User.USER_NAME, getName());
        values.put(BabyLogContract.User.PASSWORD, getPassword());
        values.put(BabyLogContract.User.SEC_QUESTION, getSecurityQuestion());
        values.put(BabyLogContract.User.SEC_ANSWER, getSecurityAnswer());
        context.getContentResolver().insert(BabyLogContract.User.CONTENT_URI, values);
    }

    @Override
    public void delete(Context context) {
    }

    @Override
    public void edit(Context context) {

    }
}
