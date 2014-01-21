package com.progrema.superbaby.models;

import android.os.Parcel;

/**
 * Created by iqbalpakeh on 21/1/14.
 */
public class User extends BaseModel {

    /**
     * User private datas
     */
    private String userId;
    private String password; //TODO: implement password encryption

    /**
     * Standard basic constructor for non-parcel
     * object creation
     *
     */
    public User(){
        super();
    }

    /**
     * Constructor to use when re-constructing object
     * from a parcel
     *
     * @param parcel parcel instance
     */
    public User(Parcel parcel){
        readFromParcel(parcel);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        // Write each field into parcel
        parcel.writeString(userId);
        parcel.writeString(password);

    }

    /**
     * Called from constructor to create this object from parcel
     *
     * @param parcel parcel from which to re-create object
     */
    private void readFromParcel(Parcel parcel){

        // read each field parcel the order that it
        // was written to the parcel
        userId = parcel.readString();
        password = parcel.readString();

    }


    public static final Creator CREATOR = new Creator<User>(){
        @Override
        public User createFromParcel(Parcel parcel) {
            return new User(parcel);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
