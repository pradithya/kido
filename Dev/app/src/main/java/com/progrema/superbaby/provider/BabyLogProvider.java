package com.progrema.superbaby.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by iqbalpakeh on 18/1/14.
 */
public class BabyLogProvider extends ContentProvider{

    private BabyLogDatabase mOpenHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int USER = 100;
    private static final int USER_BABY_MAP = 200;
    private static final int BABY = 300;
    private static final int MILK = 400;
    private static final int SLEEP = 500;
    private static final int DIAPER = 600;
    private static final int VACCINE = 700;
    private static final int FOOD = 800;
    private static final int FOOD_DETAILS = 900;
    private static final int FOOD_TYPE = 1000;
    private static final int MEASUREMENT = 1100;
    private static final int PHOTO = 1200;

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BabyLogContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, "user", USER);

        matcher.addURI(authority, "user_baby_map", USER_BABY_MAP);

        matcher.addURI(authority, "baby", BABY);

        matcher.addURI(authority, "milk", MILK);

        matcher.addURI(authority, "sleep", SLEEP);

        matcher.addURI(authority, "diaper", DIAPER);

        matcher.addURI(authority, "vaccine", VACCINE);

        matcher.addURI(authority, "food", FOOD);

        matcher.addURI(authority, "food_details", FOOD_DETAILS);

        matcher.addURI(authority, "food_type", FOOD_TYPE);

        matcher.addURI(authority, "measurement", MEASUREMENT);

        matcher.addURI(authority, "photo", PHOTO);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new BabyLogDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings2, String s2) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        deleteDataBase();
        return 0;
    }

    private void deleteDataBase(){
        mOpenHelper.close();
        BabyLogDatabase.deleteDataBase(getContext());
        mOpenHelper = new BabyLogDatabase(getContext());
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
