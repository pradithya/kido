package com.progrema.superbaby.models;

import android.content.Context;

/**
 * Created by iqbalpakeh on 31/1/14.
 */
public interface DBServices
{
    /**
     * Database insert operation
     */
    public void insert(Context context);

    /**
     * Database delete operation
     */
    public void delete(Context context);
}
