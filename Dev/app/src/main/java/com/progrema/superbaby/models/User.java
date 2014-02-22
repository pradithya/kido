package com.progrema.superbaby.models;

import android.content.ContentValues;
import android.content.Context;
import android.os.Parcel;

import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.SecurityUtils;

public class User extends BaseActor implements IDBServices
{
    /**
     * User private data
     */
    private String password;
    private String securityQuestion;
    private String securityAnswer;

    /**
     * Empty Constructor
     */
    public User()
    {
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
        super.writeToParcel(parcel, i);
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
     *
     * @param inputPlainText user's password in plaintext
     */
    public void setPassword(String inputPlainText)
    {
        this.password = SecurityUtils.computeSHA1(inputPlainText);
    }

    /**
     * used in changing password
     *
     * @return password
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * used in changing password
     *
     * @return security question
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
     * to get security answer
     *
     * @return security answer
     */
    public String getSecurityAnswer()
    {
        return securityAnswer;
    }

    /**
     * set security answer
     *
     * @param inputPlainText user's answer
     */
    public void setSecurityAnswer(String inputPlainText)
    {
        this.securityAnswer = SecurityUtils.computeSHA1(inputPlainText);
    }

    @Override
    public void insert(Context context)
    {
        ContentValues values = new ContentValues();
        values.put(BabyLogContract.User.USER_NAME, getName());
        values.put(BabyLogContract.User.PASSWORD, getPassword());
        values.put(BabyLogContract.User.SEC_QUESTION, getSecurityQuestion());
        values.put(BabyLogContract.User.SEC_ANSWER, getSecurityAnswer());
        context.getContentResolver().insert(BabyLogContract.User.CONTENT_URI, values);
    }

    @Override
    public void delete(Context context)
    {
    }
}
