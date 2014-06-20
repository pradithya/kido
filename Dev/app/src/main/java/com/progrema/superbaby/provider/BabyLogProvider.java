package com.progrema.superbaby.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import com.progrema.superbaby.util.SelectionBuilder;

public class BabyLogProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final int USER = 100;
    private static final int USER_BABY_MAP = 200;
    private static final int BABY = 300;
    private static final int NURSING = 400;
    private static final int NURSING_MAX_TIMESTAMP = 401;
    private static final int NURSING_LAST_SIDE = 402;
    private static final int SLEEP = 500;
    private static final int SLEEP_MAX_TIMESTAMP = 501;
    private static final int DIAPER = 600;
    private static final int DIAPER_MAX_TIMESTAMP = 601;
    private static final int MEASUREMENT = 700;
    private static final int MEASUREMENT_MAX_TIMESTAMP = 701;
    private static final int PHOTO = 800;
    private static final int ACTIVITY = 900;
    private BabyLogDatabase mOpenHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BabyLogContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, "user", USER);
        matcher.addURI(authority, "user_baby_map", USER_BABY_MAP);
        matcher.addURI(authority, "baby", BABY);
        matcher.addURI(authority, "nursing", NURSING);
        matcher.addURI(authority, "nursing_max_timestamp", NURSING_MAX_TIMESTAMP);
        matcher.addURI(authority, "nursing_last_side", NURSING_LAST_SIDE);
        matcher.addURI(authority, "sleep", SLEEP);
        matcher.addURI(authority, "sleep_max_timestamp", SLEEP_MAX_TIMESTAMP);
        matcher.addURI(authority, "diaper", DIAPER);
        matcher.addURI(authority, "diaper_max_timestamp", DIAPER_MAX_TIMESTAMP);
        matcher.addURI(authority, "measurement", MEASUREMENT);
        matcher.addURI(authority, "measurement_max_timestamp", MEASUREMENT_MAX_TIMESTAMP);
        matcher.addURI(authority, "photo", PHOTO);
        matcher.addURI(authority, "activity", ACTIVITY);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new BabyLogDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            //TODO: improve the ugly code by not using rawQuery!!
            case ACTIVITY:
                return db.rawQuery(BabyLogDatabase.JOIN_ALL, selectionArgs);
            case NURSING_MAX_TIMESTAMP:
                return db.rawQuery("SELECT MAX(timestamp) FROM nursing WHERE baby_id = ?", selectionArgs);
            case NURSING_LAST_SIDE:
                return db.rawQuery("SELECT sides FROM nursing WHERE baby_id = ? AND timestamp = (SELECT MAX(timestamp) FROM nursing)", selectionArgs);
            case SLEEP_MAX_TIMESTAMP:
                return db.rawQuery("SELECT MAX(timestamp) FROM sleep WHERE baby_id = ?", selectionArgs);
            case DIAPER_MAX_TIMESTAMP:
                return db.rawQuery("SELECT MAX(timestamp) FROM diaper WHERE baby_id = ?", selectionArgs);
            case MEASUREMENT_MAX_TIMESTAMP:
                return db.rawQuery("SELECT MAX(timestamp) FROM measurement WHERE baby_id = ?", selectionArgs);
            default: {
                final SelectionBuilder builder = buildSelection(uri, match);
                return builder.where(selection, selectionArgs).query(db, projection, sortOrder);
            }
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case USER: {
                // add new user to user table
                db.insertOrThrow(BabyLogDatabase.Tables.USER, null, contentValues);
                return BabyLogContract.User.buildUri(contentValues.getAsString(BaseColumns._ID));
            }

            case BABY: {
                // add new baby to baby table
                ContentValues values = new ContentValues();
                values.put(BabyLogContract.Baby.NAME, contentValues.getAsString(BabyLogContract.Baby.NAME));
                values.put(BabyLogContract.Baby.BIRTHDAY, contentValues.getAsString(BabyLogContract.Baby.BIRTHDAY));
                values.put(BabyLogContract.Baby.SEX, contentValues.getAsString(BabyLogContract.Baby.SEX));
                long babyID = db.insertOrThrow(BabyLogDatabase.Tables.BABY, null, values);

                // add new user-baby map to user-baby map table
                ContentValues userBabyMap = new ContentValues();
                userBabyMap.put(BabyLogContract.UserBabyMap.USER_ID, contentValues.getAsLong(BabyLogContract.UserBabyMap.USER_ID));
                userBabyMap.put(BabyLogContract.UserBabyMap.BABY_ID, babyID);
                db.insertOrThrow(BabyLogDatabase.Tables.USER_BABY_MAP, null, userBabyMap);

                return BabyLogContract.Baby.buildUri(contentValues.getAsString(BaseColumns._ID));
            }

            case SLEEP: {
                // add new sleep activity to activity table
                ContentValues values = new ContentValues();
                values.put(BabyLogContract.ActivityColumns.BABY_ID,
                        contentValues.getAsString(BabyLogContract.SleepColumns.BABY_ID));
                values.put(BabyLogContract.ActivityColumns.ACTIVITY_TYPE, BabyLogContract.Activity.TYPE_SLEEP);
                values.put(BabyLogContract.ActivityColumns.TIMESTAMP,
                        contentValues.getAsString(BabyLogContract.SleepColumns.TIMESTAMP));
                long actId = db.insertOrThrow(BabyLogDatabase.Tables.ACTIVITY, null, values);

                // add sleep details to sleep table
                contentValues.put(BabyLogContract.SleepColumns.ACTIVITY_ID, actId);
                db.insertOrThrow(BabyLogDatabase.Tables.SLEEP, null, contentValues);

                // notify all observer that subscribe to sleep table and activity table
                notifyChange(uri);
                notifyChange(BabyLogContract.Activity.CONTENT_URI);
                return BabyLogContract.Sleep.buildUri(contentValues.getAsString(BaseColumns._ID));
            }
            case DIAPER: {
                // add new diaper activity to activity table
                ContentValues values = new ContentValues();
                values.put(BabyLogContract.ActivityColumns.BABY_ID,
                        contentValues.getAsString(BabyLogContract.DiaperColumns.BABY_ID));
                values.put(BabyLogContract.ActivityColumns.ACTIVITY_TYPE, BabyLogContract.Activity.TYPE_DIAPER);
                values.put(BabyLogContract.ActivityColumns.TIMESTAMP,
                        contentValues.getAsString(BabyLogContract.DiaperColumns.TIMESTAMP));
                long actId = db.insertOrThrow(BabyLogDatabase.Tables.ACTIVITY, null, values);

                // add sleep details to sleep table
                contentValues.put(BabyLogContract.DiaperColumns.ACTIVITY_ID, actId);
                db.insertOrThrow(BabyLogDatabase.Tables.DIAPER, null, contentValues);

                // notify all observer that subscribe to diaper table and activity table
                notifyChange(uri);
                notifyChange(BabyLogContract.Activity.CONTENT_URI);
                return BabyLogContract.Diaper.buildUri(contentValues.getAsString(BaseColumns._ID));

            }

            case NURSING: {
                // add new nursing activity to activity table
                ContentValues values = new ContentValues();
                values.put(BabyLogContract.ActivityColumns.BABY_ID,
                        contentValues.getAsString(BabyLogContract.NursingColumns.BABY_ID));
                values.put(BabyLogContract.ActivityColumns.ACTIVITY_TYPE, BabyLogContract.Activity.TYPE_NURSING);
                values.put(BabyLogContract.ActivityColumns.TIMESTAMP,
                        contentValues.getAsString(BabyLogContract.NursingColumns.TIMESTAMP));
                long actId = db.insertOrThrow(BabyLogDatabase.Tables.ACTIVITY, null, values);

                // add nursing details to nursing table
                contentValues.put(BabyLogContract.NursingColumns.ACTIVITY_ID, actId);
                db.insertOrThrow(BabyLogDatabase.Tables.NURSING, null, contentValues);

                // notify all observer that subscribe to diaper table and activity table
                notifyChange(uri);
                notifyChange(BabyLogContract.Activity.CONTENT_URI);
                return BabyLogContract.Nursing.buildUri(contentValues.getAsString(BaseColumns._ID));
            }

            case MEASUREMENT: {
                // add new measurement activity  to activity table
                ContentValues values = new ContentValues();
                values.put(BabyLogContract.Measurement.BABY_ID,
                        contentValues.getAsString(BabyLogContract.MeasurementColumns.BABY_ID));
                values.put(BabyLogContract.ActivityColumns.ACTIVITY_TYPE, BabyLogContract.Activity.TYPE_MEASUREMENT);
                values.put(BabyLogContract.ActivityColumns.TIMESTAMP,
                        contentValues.getAsString(BabyLogContract.MeasurementColumns.TIMESTAMP));
                long actId = db.insertOrThrow(BabyLogDatabase.Tables.ACTIVITY, null, values);

                // add measurement details to measurement table
                contentValues.put(BabyLogContract.MeasurementColumns.ACTIVITY_ID, actId);
                db.insertOrThrow(BabyLogDatabase.Tables.MEASUREMENT, null, contentValues);

                // notify all observer that subscribe to measurement table and activity table
                notifyChange(uri);
                notifyChange(BabyLogContract.Activity.CONTENT_URI);
                return BabyLogContract.Measurement.buildUri(contentValues.getAsString(BaseColumns._ID));
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        if (uri == BabyLogContract.BASE_CONTENT_URI)
        {
            // Handle whole database deletes (e.g. when signing out)
            deleteDataBase();
            notifyChange(uri);
            return 1;
        }
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final SelectionBuilder builder = buildSelection(uri,  sUriMatcher.match(uri));
        int retVal = builder.where(selection, selectionArgs).delete(db);
        notifyChange(uri);
        return retVal;
    }

    private void deleteDataBase() {
        mOpenHelper.close();
        BabyLogDatabase.deleteDataBase(getContext());
        mOpenHelper = new BabyLogDatabase(getContext());
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    private void notifyChange(Uri uri) {
        Context context = getContext();
        context.getContentResolver().notifyChange(uri, null);
    }

    private SelectionBuilder buildSelection(Uri uri, int match) {
        final SelectionBuilder builder = new SelectionBuilder();
        switch (match) {
            case USER:
                return builder.table(BabyLogDatabase.Tables.USER);

            case BABY:
                return builder.table(BabyLogDatabase.Tables.BABY);

            case USER_BABY_MAP:
                return builder.table(BabyLogDatabase.Tables.USER_BABY_MAP);

            case SLEEP:
                return builder.table(BabyLogDatabase.Tables.SLEEP);

            case DIAPER:
                return builder.table(BabyLogDatabase.Tables.DIAPER);

            case NURSING:
                return builder.table(BabyLogDatabase.Tables.NURSING);

            case MEASUREMENT:
                return builder.table(BabyLogDatabase.Tables.MEASUREMENT);

            default:
                return null;
        }
    }
}
