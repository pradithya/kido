package com.progrema.superbaby.models;

import android.os.Parcel;

/**
 * Created by iqbalpakeh on 20/1/14.
 */
public class Baby extends BaseModel {

    /**
     * Baby private properties
     */
    private int babyId;
    private String name;
    private String birthday;
    private String sex;
    private String height;
    private String weight;
    private String photo; //TODO: generate the URI from the path

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

    /**
     * This field is needed by Android to create new objects
     * individually or as arrays
     */
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

    /**
     * Get babyId property
     *
     * @return babyId
     */
    public int getBabyId() {
        return babyId;
    }

    /**
     * Set babyId property
     *
     * @param babyId baby id
     */
    public void setBabyId(int babyId) {
        this.babyId = babyId;
    }

    /**
     * Get name property
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Set name property
     *
     * @param name baby name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get birthday property
     *
     * @return baby birthday
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * Set birthday property
     *
     * @param birthday baby birthday
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * Get sex property
     *
     * @return baby sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * Set sex property
     *
     * @param sex baby sex
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * Get height property
     *
     * @return height baby height
     */
    public String getHeight() {
        return height;
    }

    /**
     * Set height property
     *
     * @param height baby property
     */
    public void setHeight(String height) {
        this.height = height;
    }

    /**
     * Get weight property
     *
     * @return baby weight
     */
    public String getWeight() {
        return weight;
    }

    /**
     * Set weight property
     *
     * @param weight baby weight
     */
    public void setWeight(String weight) {
        this.weight = weight;
    }

    /**
     * Get photo property
     *
     * @return baby photo
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * Set photo property
     *
     * @param photo baby photo
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
