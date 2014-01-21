package com.progrema.superbaby.models;

import android.os.Parcelable;

/**
 * Created by iqbalpakeh on 20/1/14.
 */
abstract public class BaseModel implements Parcelable{

    /**
     * logical flag containing object deletion status
     */
    protected boolean isDeleted;

    public BaseModel(){
        isDeleted = false;
    }

    public void deleteObject(boolean input){
        isDeleted = input;
    }

    public boolean isObjectDelete(){
        return isDeleted;
    }

}
