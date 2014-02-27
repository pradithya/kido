package com.progrema.superbaby.models;

import android.content.ContentValues;
import android.content.Context;

import com.progrema.superbaby.provider.BabyLogContract;

/**
 * Created by iqbalpakeh on 27/2/14.
 */
public class Measurement extends BaseActivity implements IDBServices
{
    private float height;
    private float weight;
    public final static String HEIGHT_KEY = "height";
    public final static String WEIGHT_KEY = "weight";

    public float getHeight()
    {
        return height;
    }

    public void setHeight(float height)
    {
        this.height = height;
    }

    public float getWeight()
    {
        return weight;
    }

    public void setWeight(float weight)
    {
        this.weight = weight;
    }

    @Override
    public void insert(Context context)
    {
        ContentValues values = new ContentValues();
        values.put(BabyLogContract.Measurement.BABY_ID, getBabyID());
        values.put(BabyLogContract.Measurement.TIMESTAMP, getTimeStampInString());
        values.put(BabyLogContract.Measurement.HEIGHT, getHeight());
        values.put(BabyLogContract.Measurement.WEIGHT, getWeight());
        context.getContentResolver().insert(BabyLogContract.Measurement.CONTENT_URI, values);
    }

    @Override
    public void delete(Context context)
    {

    }
}
