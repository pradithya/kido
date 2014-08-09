package com.progrema.superbaby.models;

import android.content.ContentValues;
import android.content.Context;

import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.ActiveContext;

public class ActivityMeasurement extends BaseActivity {

    public final static String HEIGHT_KEY = "height";
    public final static String WEIGHT_KEY = "weight";
    private float height;
    private float weight;

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    @Override
    public void insert(Context context) {
        ContentValues values = new ContentValues();
        values.put(BabyLogContract.Measurement.BABY_ID, getBabyID());
        values.put(BabyLogContract.Measurement.TIMESTAMP, getTimeStampInString());
        values.put(BabyLogContract.Measurement.HEIGHT, getHeight());
        values.put(BabyLogContract.Measurement.WEIGHT, getWeight());
        context.getContentResolver().insert(BabyLogContract.Measurement.CONTENT_URI, values);
    }

    @Override
    public void delete(Context context) {
        String [] selectionArgs = {
                String.valueOf(ActiveContext.getActiveBaby(context).getActivityId()),
                String.valueOf(getActivityId())};
        context.getContentResolver().delete(
                BabyLogContract.Measurement.CONTENT_URI,
                "baby_id = ? AND activity_id = ?",
                selectionArgs);
    }

    @Override
    public void update(Context context) {

    }
}
