package com.progrema.superbaby.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;

import com.progrema.superbaby.provider.BabyLogContract.ActivityColumns;
import com.progrema.superbaby.provider.BabyLogContract.BabyColumns;
import com.progrema.superbaby.provider.BabyLogContract.Diaper;
import com.progrema.superbaby.provider.BabyLogContract.DiaperColumns;
import com.progrema.superbaby.provider.BabyLogContract.Measurement;
import com.progrema.superbaby.provider.BabyLogContract.Nursing;
import com.progrema.superbaby.provider.BabyLogContract.NursingColumns;
import com.progrema.superbaby.provider.BabyLogContract.Photo;
import com.progrema.superbaby.provider.BabyLogContract.Sleep;
import com.progrema.superbaby.provider.BabyLogContract.SleepColumns;
import com.progrema.superbaby.provider.BabyLogContract.UserBabyMap;
import com.progrema.superbaby.provider.BabyLogContract.UserBabyMapColumns;
import com.progrema.superbaby.provider.BabyLogContract.UserColumns;

/**
 * Created by iqbalpakeh on 18/1/14.
 */
public class BabyLogDatabase extends SQLiteOpenHelper
{

    private static final String DATABASE_NAME = "babylog.db";

    /**
     * NOTE:
     * carefully update onUpgrade() when bumping database versions to make
     * sure user data is saved.
     */

    private static final int VER_2014_01 = 100; // 1.0
    private static final int DATABASE_VERSION = VER_2014_01;

    private final Context mContext;

    interface Tables
    {
        String USER = "user";
        String USER_BABY_MAP = "user_baby_map";
        String BABY = "baby";
        String ACTIVITY = "activity";
        String NURSING = "nursing";
        String SLEEP = "sleep";
        String DIAPER = "diaper";
        String MEASUREMENT = "measurement";
        String PHOTO = "photo";
    }

    public BabyLogDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    private interface TriggersName
    {
        // Deletes from all activities table, when corresponding baby deleted
        String BABY_NURSING_DELETE = "baby_nursing_delete";
        String BABY_SLEEP_DELETE = "baby_sleep_delete";
        String BABY_DIAPER_DELETE = "baby_diaper_delete";
        String BABY_FOOD_DELETE = "baby_food_delete";
        String BABY_USER_DELETE = "baby_user_delete";
        String BABY_MEASUREMENT_DELETE = "baby_measurement_delete";
        String BABY_PHOTO_DELETE = "baby_photo_delete";
    }

    private interface Qualified
    {
        String BABY_DIAPER = Tables.DIAPER + "." + Diaper.BABY_ID;
        String BABY_SLEEP = Tables.SLEEP + "." + Sleep.BABY_ID;
        String BABY_NURSING = Tables.NURSING + "." + Nursing.BABY_ID;
        String BABY_USER_MAP = Tables.USER_BABY_MAP + "." + UserBabyMap.BABY_ID;
        String BABY_MEASUREMENT = Tables.MEASUREMENT + "." + Measurement.BABY_ID;
        String BABY_PHOTO = Tables.PHOTO + "." + Photo.BABY_ID;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + Tables.USER + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + UserColumns.USER_NAME + " TEXT NOT NULL,"
                + UserColumns.PASSWORD + " TEXT NOT NULL,"
                + UserColumns.SEC_QUESTION + " TEXT NOT NULL,"
                + UserColumns.SEC_ANSWER + " TEXT NOT NULL,"
                + " UNIQUE (" + UserColumns.USER_NAME + ") ON CONFLICT FAIL)");

        db.execSQL("CREATE TABLE " + Tables.USER_BABY_MAP + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + UserBabyMapColumns.USER_ID + " INTEGER,"
                + UserBabyMapColumns.BABY_ID + " INTEGER,"
                + " FOREIGN KEY (" + UserBabyMapColumns.USER_ID + ") REFERENCES " + Tables.USER + " (" + BaseColumns._ID + "),"
                + " FOREIGN KEY (" + UserBabyMapColumns.BABY_ID + ") REFERENCES " + Tables.BABY + " (" + BaseColumns._ID + ")" + ")");

        db.execSQL("CREATE TABLE " + Tables.BABY + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BabyColumns.NAME + " TEXT NOT NULL,"
                + BabyColumns.BIRTHDAY + " TEXT NOT NULL,"
                + BabyColumns.SEX + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + Tables.ACTIVITY + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ActivityColumns.BABY_ID + " TEXT NOT NULL,"
                + ActivityColumns.ACTIVITY_TYPE + " TEXT NOT NULL,"
                + " FOREIGN KEY (" + ActivityColumns.BABY_ID + ") REFERENCES " + Tables.BABY + " (" + BaseColumns._ID + ")" + ")");

        db.execSQL("CREATE TABLE " + Tables.NURSING + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NursingColumns.ACTIVITY_ID + " TEXT NOT NULL,"
                + NursingColumns.BABY_ID + " TEXT NOT NULL,"
                + NursingColumns.TIMESTAMP + " TEXT NOT NULL,"
                + NursingColumns.DURATION + " TEXT NOT NULL,"
                + NursingColumns.SIDES + " TEXT NOT NULL,"
                + NursingColumns.VOLUME + " TEXT NOT NULL,"
                + " FOREIGN KEY (" + NursingColumns.ACTIVITY_ID + ") REFERENCES " + Tables.ACTIVITY + " (" + BaseColumns._ID + "),"
                + " FOREIGN KEY (" + NursingColumns.BABY_ID + ") REFERENCES " + Tables.BABY + " (" + BaseColumns._ID + ")" + ")");

        db.execSQL("CREATE TABLE " + Tables.SLEEP + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SleepColumns.ACTIVITY_ID + " TEXT NOT NULL,"
                + SleepColumns.BABY_ID + " TEXT NOT NULL,"
                + SleepColumns.TIMESTAMP + " TEXT NOT NULL,"
                + SleepColumns.DURATION + " TEXT NOT NULL,"
                + " FOREIGN KEY (" + SleepColumns.ACTIVITY_ID + ") REFERENCES " + Tables.ACTIVITY + " (" + BaseColumns._ID + "),"
                + " FOREIGN KEY (" + SleepColumns.BABY_ID + ") REFERENCES " + Tables.BABY + " (" + BaseColumns._ID + ")" + ")");

        db.execSQL("CREATE TABLE " + Tables.DIAPER + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DiaperColumns.ACTIVITY_ID + " TEXT NOT NULL,"
                + DiaperColumns.BABY_ID + " TEXT NOT NULL,"
                + DiaperColumns.TIMESTAMP + " TEXT NOT NULL,"
                + DiaperColumns.TYPE + " TEXT NOT NULL,"
                + " FOREIGN KEY (" + DiaperColumns.ACTIVITY_ID + ") REFERENCES " + Tables.ACTIVITY + " (" + BaseColumns._ID + "),"
                + " FOREIGN KEY (" + DiaperColumns.BABY_ID + ") REFERENCES " + Tables.BABY + " (" + BaseColumns._ID + ")" + ")");

        db.execSQL("CREATE TABLE " + Tables.MEASUREMENT + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Measurement.MEASUREMENT_ID + " TEXT NOT NULL,"
                + Measurement.BABY_ID + " TEXT NOT NULL,"
                + Measurement.HEIGHT + " REAL,"
                + Measurement.WEIGHT + " REAL,"
                + " FOREIGN KEY (" + Measurement.MEASUREMENT_ID + ") REFERENCES " + Tables.ACTIVITY + " (" + BaseColumns._ID + "),"
                + " FOREIGN KEY (" + Measurement.BABY_ID + ") REFERENCES " + Tables.BABY + " (" + BaseColumns._ID + ")" + ")");

        db.execSQL("CREATE TABLE " + Tables.PHOTO + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Photo.PHOTO_ID + " TEXT NOT NULL,"
                + Photo.BABY_ID + " TEXT NOT NULL,"
                + Photo.TIMESTAMP + " TEXT NOT NULL,"
                + Photo.PHOTO_LOCATION + " TEXT NOT NULL,"
                + " FOREIGN KEY (" + Photo.PHOTO_ID + ") REFERENCES " + Tables.ACTIVITY + " (" + BaseColumns._ID + "),"
                + " FOREIGN KEY (" + Photo.BABY_ID + ") REFERENCES " + Tables.BABY + " (" + BaseColumns._ID + ")" + ")");

        //trigger for delete on baby table
        db.execSQL("CREATE TRIGGER " + TriggersName.BABY_DIAPER_DELETE
                + " AFTER DELETE ON " + Tables.BABY
                + " FOR EACH ROW BEGIN "
                + " DELETE FROM " + Tables.DIAPER
                + " WHERE " + Qualified.BABY_DIAPER + "=old." + Diaper.BABY_ID
                + ";" + " END;");

        db.execSQL("CREATE TRIGGER " + TriggersName.BABY_NURSING_DELETE
                + " AFTER DELETE ON " + Tables.BABY
                + " FOR EACH ROW BEGIN "
                + " DELETE FROM " + Tables.NURSING
                + " WHERE " + Qualified.BABY_NURSING + "=old." + Nursing.BABY_ID
                + ";" + " END;");

        db.execSQL("CREATE TRIGGER " + TriggersName.BABY_SLEEP_DELETE
                + " AFTER DELETE ON " + Tables.BABY
                + " FOR EACH ROW BEGIN "
                + " DELETE FROM " + Tables.SLEEP
                + " WHERE " + Qualified.BABY_SLEEP + "=old." + Sleep.BABY_ID
                + ";" + " END;");

        db.execSQL("CREATE TRIGGER " + TriggersName.BABY_USER_DELETE
                + " AFTER DELETE ON " + Tables.BABY
                + " FOR EACH ROW BEGIN "
                + " DELETE FROM " + Tables.USER_BABY_MAP
                + " WHERE " + Qualified.BABY_USER_MAP + "=old." + UserBabyMap.BABY_ID
                + ";" + " END;");

        db.execSQL("CREATE TRIGGER " + TriggersName.BABY_MEASUREMENT_DELETE
                + " AFTER DELETE ON " + Tables.BABY
                + " FOR EACH ROW BEGIN "
                + " DELETE FROM " + Tables.MEASUREMENT
                + " WHERE " + Qualified.BABY_MEASUREMENT + "=old." + Measurement.BABY_ID
                + ";" + " END;");

        db.execSQL("CREATE TRIGGER " + TriggersName.BABY_PHOTO_DELETE
                + " AFTER DELETE ON " + Tables.BABY
                + " FOR EACH ROW BEGIN "
                + " DELETE FROM " + Tables.PHOTO
                + " WHERE " + Qualified.BABY_PHOTO + "=old." + Photo.BABY_ID
                + ";" + " END;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2)
    {
    }

    public static void deleteDataBase(Context context)
    {
        context.deleteDatabase(DATABASE_NAME);
    }

    @Override
    public SQLiteDatabase getReadableDatabase()
    {
        SQLiteDatabase db = super.getReadableDatabase();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            db.setForeignKeyConstraintsEnabled(false);
        }
        return db;
    }

    @Override
    public SQLiteDatabase getWritableDatabase()
    {
        SQLiteDatabase db = super.getWritableDatabase();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            db.setForeignKeyConstraintsEnabled(false);
        }
        return db;
    }

}
