package com.progrema.superbaby.models;

import android.content.Context;
import android.os.Parcel;

import com.progrema.superbaby.util.SecurityUtils;

/**
 * Created by iqbalpakeh on 21/1/14.
 * @author aria
 * @author iqbalpakeh
 */
public class User extends BaseActor implements DBServices {

    /**
     * User private data
     */
    private String password;
    private String securityQuestion;
    private String securityAnswer;

    /**
     * Standard basic constructor for non-parcel
     * object creation
     *
     */
    public User()
    {
        /** Empty constructor */
    }

    /**
     * Constructor to use when re-constructing object
     * from a parcel
     *
     * @param parcel parcel instance
     */
    public User(Parcel parcel)
    {
        readFromParcel(parcel);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        // Write each field into parcel
        super.writeToParcel(parcel,i);
        parcel.writeString(password);
        parcel.writeString(securityQuestion);
        parcel.writeString(securityAnswer);
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
        password = parcel.readString();
        securityQuestion = parcel.readString();
        securityAnswer = parcel.readString();
    }


    public static final Creator CREATOR = new Creator<User>()
    {
        @Override
        public User createFromParcel(Parcel parcel)
        {
            return new User(parcel);
        }

        @Override
        public User[] newArray(int size)
        {
            return new User[size];
        }
    };

    /**
     * used in changing password
     * @param inputPlainText
     */
    public void setPassword(String inputPlainText)
    {
        this.password = SecurityUtils.computeSHA1(password);
    }

    /**
     * used in changing password
     * @return
     */
    public String getSecurityQuestion()
    {
        return securityQuestion;
    }

    /**
     * changing security question
     */
    public void setSecurityQuestion(String securityQuestion)
    {
        this.securityQuestion = securityQuestion;
    }

    /**
     * used everytime user log in
     * @param inputPlainText
     * @return
     */
    public boolean verifyPassword(String inputPlainText)
    {
        return (SecurityUtils.computeSHA1(inputPlainText).compareTo(password) == 0);
    }

    /**
     *
     * @param inputPlainText
     * @return
     */
    public boolean verifySecurityQuestion(String inputPlainText)
    {
        return (SecurityUtils.computeSHA1(inputPlainText).compareTo(securityAnswer) == 0);
    }

    @Override
    public void insert(Context context)
    {
    }

    @Override
    public void delete(Context context)
    {
    }
}
