package com.progrema.superbaby.models;

import android.os.Parcel;

/**
 * Created by iqbalpakeh on 20/1/14.
 */
public class Baby extends BaseModel {

    /**
     * Baby privates datas
     */
    private int babyId;
    private String name;
    private String birthday;
    private String sex;
    private String height;
    private String weight;
    private String photo;

    /**
     * Standard basic constructor for non-parcel
     * object creation
     *
     */
    public Baby(){
        super();
    }

    /**
     * Constructor to use when re-constructing object
     * from a parcel
     *
     * @param parcel parcel instance
     */
    public Baby(Parcel parcel){
        readFromParcel(parcel);
    }

    /**
     * Called from constructor to create this object from parcel
     *
     * @param parcel parcel from which to re-create object
     */
    private void readFromParcel(Parcel parcel){

        // read each field parcel the order that it
        // was written to the parcel
        babyId = parcel.readInt();
        name = parcel.readString();
        birthday = parcel.readString();
        sex = parcel.readString();
        height = parcel.readString();
        weight = parcel.readString();
        photo = parcel.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        // write each field into the parcel
        parcel.writeInt(babyId);
        parcel.writeString(name);
        parcel.writeString(birthday);
        parcel.writeString(sex);
        parcel.writeString(height);
        parcel.writeString(weight);
        parcel.writeString(photo);

    }

    public static final Creator CREATOR = new Creator<Baby>(){
        @Override
        public Baby createFromParcel(Parcel parcel) {
            return new Baby(parcel);
        }

        @Override
        public Baby[] newArray(int size) {
            return new Baby[size];
        }
    };

    public int getBabyId() {
        return babyId;
    }

    public void setBabyId(int babyId) {
        this.babyId = babyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
