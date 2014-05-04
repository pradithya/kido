package com.progrema.superbaby.models;

import android.content.Context;

public interface IDBServices {
    /**
     * Database insert operation
     */
    public void insert(Context context);

    /**
     * Database delete operation
     */
    public void delete(Context context);
}
