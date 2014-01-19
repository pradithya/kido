package com.progrema.superbaby.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
        String MILK = "milk";
        String SLEEP = "sleep";
        String DIAPER = "diaper";
        String VACCINE = "vaccine";
        String FOOD = "food";
        String FOOD_DETAILS = "food_details";
        String FOOD_TYPE = "food_type";
    }

    public BabyLogDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + Tables.USER + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + UserColumns.USER_ID + " TEXT NOT NULL,"
                + UserColumns.PASSWORD + " TEXT NOT NULL,"
                + " UNIQUE (" + UserColumns.USER_ID + ") ON CONFLICT FAIL)");

        db.execSQL("CREATE TABLE " + Tables.USER_BABY_MAP + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + UserBabyMapColumns.USER_ID + " TEXT NOT NULL,"
                + UserBabyMapColumns.BABY_ID + " TEXT NOT NULL,"
                + " UNIQUE (" + UserBabyMapColumns.USER_ID + ") ON CONFLICT FAIL,"
                + " UNIQUE (" + UserBabyMapColumns.BABY_ID + ") ON CONFLICT FAIL)");

        db.execSQL("CREATE TABLE " + Tables.BABY + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BabyColumns.BABY_ID + " TEXT NOT NULL,"
                + BabyColumns.NAME + " TEXT NOT NULL,"
                + BabyColumns.BIRTHDAY + " TEXT NOT NULL,"
                + BabyColumns.SEX + " TEXT NOT NULL,"
                + BabyColumns.HEIGHT + " TEXT NOT NULL,"
                + BabyColumns.WEIGHT + " TEXT NOT NULL,"
                + BabyColumns.PHOTO + " TEXT NOT NULL,"
                + " UNIQUE (" + BabyColumns.BABY_ID + ") ON CONFLICT FAIL)");

        db.execSQL("CREATE TABLE " + Tables.MILK + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MilkColumns.ACTIVITY_ID + " TEXT NOT NULL,"
                + MilkColumns.BABY_ID + " TEXT NOT NULL,"
                + MilkColumns.TIMESTAMP + " TEXT NOT NULL,"
                + MilkColumns.DURATION + " TEXT NOT NULL,"
                + MilkColumns.SIDES + " TEXT NOT NULL,"
                + MilkColumns.VOLUME + " TEXT NOT NULL,"
                + " UNIQUE (" + MilkColumns.ACTIVITY_ID + ") ON CONFLICT FAIL,"
                + " UNIQUE (" + MilkColumns.BABY_ID + ") ON CONFLICT FAIL)");

        db.execSQL("CREATE TABLE " + Tables.SLEEP + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SleepColumns.ACTIVITY_ID + " TEXT NOT NULL,"
                + SleepColumns.BABY_ID + " TEXT NOT NULL,"
                + SleepColumns.TIMESTAMP + " TEXT NOT NULL,"
                + SleepColumns.DURATION + " TEXT NOT NULL,"
                + " UNIQUE (" + SleepColumns.ACTIVITY_ID + ") ON CONFLICT FAIL,"
                + " UNIQUE (" + SleepColumns.BABY_ID + ") ON CONFLICT FAIL)");

        db.execSQL("CREATE TABLE " + Tables.DIAPER + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DiaperColumns.ACTIVITY_ID + " TEXT NOT NULL,"
                + DiaperColumns.BABY_ID + " TEXT NOT NULL,"
                + DiaperColumns.TIMESTAMP + " TEXT NOT NULL,"
                + DiaperColumns.TYPE + " TEXT NOT NULL,"
                + " UNIQUE (" + DiaperColumns.ACTIVITY_ID + ") ON CONFLICT FAIL,"
                + " UNIQUE (" + DiaperColumns.BABY_ID + ") ON CONFLICT FAIL)");

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
                + " UNIQUE (" + VaccineColumns.BABY_ID + ") ON CONFLICT FAIL)");

        db.execSQL("CREATE TABLE " + Tables.FOOD + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FoodColumns.ACTIVITY_ID + " TEXT NOT NULL,"
                + FoodColumns.BABY_ID + " TEXT NOT NULL,"
                + FoodColumns.TIMESTAMP + " TEXT NOT NULL,"
                + FoodColumns.DURATION + " TEXT NOT NULL,"
                + " UNIQUE (" + FoodColumns.ACTIVITY_ID + ") ON CONFLICT FAIL,"
                + " UNIQUE (" + FoodColumns.BABY_ID + ") ON CONFLICT FAIL)");

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

        //TODO: to implement foreign key constraint of each table by using TRIGGER!!

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {

    }

    public static void deleteDataBase(Context context){
        context.deleteDatabase(DATABASE_NAME);
    }

}
