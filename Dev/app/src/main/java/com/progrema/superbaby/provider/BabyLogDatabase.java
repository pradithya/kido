package com.progrema.superbaby.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;

import com.progrema.superbaby.provider.BabyLogContract.*;

/**
 * Created by iqbalpakeh on 18/1/14.
 */
public class BabyLogDatabase extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "babylog.db";

    // NOTE: carefully update onUpgrade() when bumping database versions to make
    // sure user data is saved.

    private static final int VER_2014_01 = 100; // 1.0
    private static final int DATABASE_VERSION = VER_2014_01;

    private final Context mContext;

    interface Tables {
        String USER = "user";
        String USER_BABY_MAP = "user_baby_map";
        String BABY = "baby";
        String NURSING = "nursing";
        String SLEEP = "sleep";
        String DIAPER = "diaper";
        String VACCINE = "vaccine";
        String FOOD = "food";
        String FOOD_DETAILS = "food_details";
        String FOOD_TYPE = "food_type";
        String MEASUREMENT = "measurement";
        String PHOTO = "photo";
    }

    public BabyLogDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    private interface TriggersName {
        // Deletes from all activities table, when corresponding baby deleted
        String BABY_NURSING_DELETE = "baby_nursing_delete";
        String BABY_SLEEP_DELETE = "baby_sleep_delete";
        String BABY_DIAPER_DELETE = "baby_diaper_delete";
        String BABY_VACCINE_DELETE = "baby_vaccine_delete";
        String BABY_FOOD_DELETE = "baby_food_delete";
        String BABY_USER_DELETE = "baby_user_delete";
        String BABY_MEASUREMENT_DELETE = "baby_measurement_delete";
        String BABY_PHOTO_DELETE = "baby_photo_delete";
    }

    private interface Qualified {
        String BABY_DIAPER = Tables.DIAPER + "." + Diaper.BABY_ID;
        String BABY_SLEEP = Tables.SLEEP + "." + Sleep.BABY_ID;
        String BABY_NURSING = Tables.NURSING + "." + Nursing.BABY_ID;
        String BABY_VACCINE = Tables.VACCINE + "." + Vaccine.BABY_ID;
        String BABY_FOOD = Tables.FOOD + "." + Food.BABY_ID;
        String BABY_USER_MAP = Tables.USER_BABY_MAP + "." + UserBabyMap.BABY_ID;
        String BABY_MEASUREMENT = Tables.MEASUREMENT + "." + Measurement.BABY_ID;
        String BABY_PHOTO = Tables.PHOTO + "." + Photo.BABY_ID;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + Tables.USER + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + UserColumns.USER_ID + " TEXT NOT NULL,"
                + UserColumns.PASSWORD + " TEXT NOT NULL,"
                + UserColumns.SEC_QUESTION + " TEXT NOT NULL,"
                + UserColumns.SEC_ANSWER + " TEXT NOT NULL,"
                + " UNIQUE (" + UserColumns.USER_ID + ") ON CONFLICT FAIL)");

        db.execSQL("CREATE TABLE " + Tables.USER_BABY_MAP + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + UserBabyMapColumns.USER_ID + " TEXT NOT NULL,"
                + UserBabyMapColumns.BABY_ID + " TEXT NOT NULL,"
                + " FOREIGN KEY (" + UserBabyMapColumns.USER_ID + ") REFERENCES " + Tables.USER + " (" + UserColumns.USER_ID + "),"
                + " FOREIGN KEY (" + UserBabyMapColumns.BABY_ID + ") REFERENCES " + Tables.BABY + " (" + BabyColumns.BABY_ID + ")" +")");

        db.execSQL("CREATE TABLE " + Tables.BABY + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BabyColumns.BABY_ID + " TEXT NOT NULL,"
                + BabyColumns.NAME + " TEXT NOT NULL,"
                + BabyColumns.BIRTHDAY + " TEXT NOT NULL,"
                + BabyColumns.SEX + " TEXT NOT NULL,"
                + " UNIQUE (" + BabyColumns.BABY_ID + ") ON CONFLICT FAIL)");

        db.execSQL("CREATE TABLE " + Tables.NURSING + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NursingColumns.ACTIVITY_ID + " TEXT NOT NULL,"
                + NursingColumns.BABY_ID + " TEXT NOT NULL,"
                + NursingColumns.TIMESTAMP + " TEXT NOT NULL,"
                + NursingColumns.DURATION + " TEXT NOT NULL,"
                + NursingColumns.SIDES + " TEXT NOT NULL,"
                + NursingColumns.VOLUME + " TEXT NOT NULL,"
                + " UNIQUE (" + NursingColumns.ACTIVITY_ID + ") ON CONFLICT FAIL,"
                + " FOREIGN KEY (" + NursingColumns.BABY_ID + ") REFERENCES " + Tables.BABY + " (" + BabyColumns.BABY_ID + ")" +")");

        db.execSQL("CREATE TABLE " + Tables.SLEEP + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SleepColumns.ACTIVITY_ID + " TEXT NOT NULL,"
                + SleepColumns.BABY_ID + " TEXT NOT NULL,"
                + SleepColumns.TIMESTAMP + " TEXT NOT NULL,"
                + SleepColumns.DURATION + " TEXT NOT NULL,"
                + " UNIQUE (" + SleepColumns.ACTIVITY_ID + ") ON CONFLICT FAIL,"
                + " FOREIGN KEY (" + SleepColumns.BABY_ID + ") REFERENCES " + Tables.BABY + " (" + BabyColumns.BABY_ID + ")" +")");

        db.execSQL("CREATE TABLE " + Tables.DIAPER + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DiaperColumns.ACTIVITY_ID + " TEXT NOT NULL,"
                + DiaperColumns.BABY_ID + " TEXT NOT NULL,"
                + DiaperColumns.TIMESTAMP + " TEXT NOT NULL,"
                + DiaperColumns.TYPE + " TEXT NOT NULL,"
                + " UNIQUE (" + DiaperColumns.ACTIVITY_ID + ") ON CONFLICT FAIL,"
                + " FOREIGN KEY (" + DiaperColumns.BABY_ID + ") REFERENCES " + Tables.BABY + " (" + BabyColumns.BABY_ID + ")" +")");

        db.execSQL("CREATE TABLE " + Tables.VACCINE + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + VaccineColumns.ACTIVITY_ID + " TEXT NOT NULL,"
                + VaccineColumns.BABY_ID + " TEXT NOT NULL,"
                + VaccineColumns.VACCINE_NAME + " TEXT NOT NULL,"
                + VaccineColumns.LOCATION + " TEXT NOT NULL,"
                + VaccineColumns.NOTES + " TEXT NOT NULL,"
                + VaccineColumns.TIMESTAMP + " TEXT NOT NULL,"
                + VaccineColumns.REMINDER_TIME + " TEXT NOT NULL,"
                + " UNIQUE (" + VaccineColumns.ACTIVITY_ID + ") ON CONFLICT FAIL,"
                + " FOREIGN KEY (" + VaccineColumns.BABY_ID + ") REFERENCES " + Tables.BABY + " (" + BabyColumns.BABY_ID + ")" +")");

        db.execSQL("CREATE TABLE " + Tables.MEASUREMENT + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Measurement.MEASUREMENT_ID + " TEXT NOT NULL,"
                + Measurement.BABY_ID + " TEXT NOT NULL,"
                + Measurement.HEIGHT + " REAL,"
                + Measurement.WEIGHT +  " REAL,"
                + Measurement.TIMESTAMP + " TEXT NOT NULL,"
                + " FOREIGN KEY (" + Measurement.BABY_ID + ") REFERENCES " + Tables.BABY + " (" + BabyColumns.BABY_ID + ")" +")");

        db.execSQL("CREATE TABLE " + Tables.PHOTO + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Photo.PHOTO_ID + " TEXT NOT NULL,"
                + Photo.BABY_ID + " TEXT NOT NULL,"
                + Photo.TIMESTAMP + " TEXT NOT NULL,"
                + Photo.PHOTO_LOCATION + " TEXT NOT NULL,"
                + " UNIQUE (" + Photo.PHOTO_ID + ") ON CONFLICT FAIL,"
                + " FOREIGN KEY (" + Photo.BABY_ID + ") REFERENCES " + Tables.BABY + " (" + BabyColumns.BABY_ID + ")" +")");

        db.execSQL("CREATE TABLE " + Tables.FOOD + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FoodColumns.ACTIVITY_ID + " TEXT NOT NULL,"
                + FoodColumns.BABY_ID + " TEXT NOT NULL,"
                + FoodColumns.TIMESTAMP + " TEXT NOT NULL,"
                + FoodColumns.DURATION + " TEXT NOT NULL,"
                + " UNIQUE (" + FoodColumns.ACTIVITY_ID + ") ON CONFLICT FAIL,"
                + " FOREIGN KEY (" + FoodColumns.BABY_ID + ") REFERENCES " + Tables.BABY + "(" + BabyColumns.BABY_ID + ")" +")");

        db.execSQL("CREATE TABLE " + Tables.FOOD_DETAILS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FoodDetailsColumns.ACTIVITY_ID + " TEXT NOT NULL,"
                + FoodDetailsColumns.FOOD_TYPE + " TEXT NOT NULL,"
                + FoodDetailsColumns.QUANTITY + " TEXT NOT NULL,"
                + " UNIQUE (" + FoodDetailsColumns.ACTIVITY_ID + ") ON CONFLICT FAIL)");

        db.execSQL("CREATE TABLE " + Tables.FOOD_TYPE + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FoodTypeColumns.FOOD_TYPE + " TEXT NOT NULL,"
                + FoodTypeColumns.NAME + " TEXT NOT NULL" + " )");

        //trigger for delete on baby table
        db.execSQL("CREATE TRIGGER " + TriggersName.BABY_DIAPER_DELETE
                + " AFTER DELETE ON " +  Tables.BABY
                + " FOR EACH ROW BEGIN "
                + " DELETE FROM " + Tables.DIAPER
                + " WHERE " + Qualified.BABY_DIAPER + "=old." + Baby.BABY_ID
                + ";" + " END;");

        db.execSQL("CREATE TRIGGER " + TriggersName.BABY_NURSING_DELETE
                + " AFTER DELETE ON " +  Tables.BABY
                + " FOR EACH ROW BEGIN "
                + " DELETE FROM " + Tables.NURSING
                + " WHERE " + Qualified.BABY_NURSING + "=old." + Baby.BABY_ID
                + ";" + " END;");

        db.execSQL("CREATE TRIGGER " + TriggersName.BABY_SLEEP_DELETE
                + " AFTER DELETE ON " +  Tables.BABY
                + " FOR EACH ROW BEGIN "
                + " DELETE FROM " + Tables.SLEEP
                + " WHERE " + Qualified.BABY_SLEEP + "=old." + Baby.BABY_ID
                + ";" + " END;");

        db.execSQL("CREATE TRIGGER " + TriggersName.BABY_FOOD_DELETE
                + " AFTER DELETE ON " +  Tables.BABY
                + " FOR EACH ROW BEGIN "
                + " DELETE FROM " + Tables.FOOD
                + " WHERE " + Qualified.BABY_FOOD + "=old." + Baby.BABY_ID
                + ";" + " END;");

        db.execSQL("CREATE TRIGGER " + TriggersName.BABY_VACCINE_DELETE
                + " AFTER DELETE ON " +  Tables.BABY
                + " FOR EACH ROW BEGIN "
                + " DELETE FROM " + Tables.VACCINE
                + " WHERE " + Qualified.BABY_VACCINE + "=old." + Baby.BABY_ID
                + ";" + " END;");

        db.execSQL("CREATE TRIGGER " + TriggersName.BABY_USER_DELETE
                + " AFTER DELETE ON " +  Tables.BABY
                + " FOR EACH ROW BEGIN "
                + " DELETE FROM " + Tables.USER_BABY_MAP
                + " WHERE " + Qualified.BABY_USER_MAP+ "=old." + Baby.BABY_ID
                + ";" + " END;");

        db.execSQL("CREATE TRIGGER " + TriggersName.BABY_MEASUREMENT_DELETE
                + " AFTER DELETE ON " +  Tables.BABY
                + " FOR EACH ROW BEGIN "
                + " DELETE FROM " + Tables.MEASUREMENT
                + " WHERE " + Qualified.BABY_MEASUREMENT+ "=old." + Baby.BABY_ID
                + ";" + " END;");

        db.execSQL("CREATE TRIGGER " + TriggersName.BABY_PHOTO_DELETE
                + " AFTER DELETE ON " +  Tables.BABY
                + " FOR EACH ROW BEGIN "
                + " DELETE FROM " + Tables.PHOTO
                + " WHERE " + Qualified.BABY_PHOTO+ "=old." + Baby.BABY_ID
                + ";" + " END;");

        /**
         * Foreign Key Constraints cannot be enabled or disabled while there are transactions in progress.
         * Finish all transactions and release all active database connections first.
         */
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
        //            db.setForeignKeyConstraintsEnabled(true);
        //        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {

    }

    public static void deleteDataBase(Context context){
        context.deleteDatabase(DATABASE_NAME);
    }

    @Override
    public SQLiteDatabase getReadableDatabase(){
        SQLiteDatabase db = super.getReadableDatabase();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            db.setForeignKeyConstraintsEnabled(true);
        }
        return db;
    }

    @Override
    public SQLiteDatabase getWritableDatabase(){
        SQLiteDatabase db = super.getWritableDatabase();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            db.setForeignKeyConstraintsEnabled(true);
        }
        return db;
    }

}
