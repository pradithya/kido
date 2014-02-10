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

public class BabyLogProvider extends ContentProvider
{
    private BabyLogDatabase mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final int USER = 100;
    private static final int USER_BABY_MAP = 200;
    private static final int BABY = 300;
    private static final int MILK = 400;
    private static final int SLEEP = 500;
    private static final int DIAPER = 600;
    private static final int MEASUREMENT = 700;
    private static final int PHOTO = 800;

    private static UriMatcher buildUriMatcher()
    {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BabyLogContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, "user", USER);
        matcher.addURI(authority, "user_baby_map", USER_BABY_MAP);
        matcher.addURI(authority, "baby", BABY);
        matcher.addURI(authority, "milk", MILK);
        matcher.addURI(authority, "sleep", SLEEP);
        matcher.addURI(authority, "diaper", DIAPER);
        matcher.addURI(authority, "measurement", MEASUREMENT);
        matcher.addURI(authority, "photo", PHOTO);

        return matcher;
    }

    @Override
    public boolean onCreate()
    {
        mOpenHelper = new BabyLogDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match)
        {
            case USER:
            {
                final SelectionBuilder builder = buildExpandableSelection(uri, match);
                return builder.where(selection, selectionArgs).query(db, projection, sortOrder);
            }

            case BABY:
            {
                final SelectionBuilder builder = buildExpandableSelection(uri, match);
                return builder.where(selection, selectionArgs).query(db, projection, sortOrder);
            }

            case SLEEP:
            {
                final SelectionBuilder builder = buildExpandableSelection(uri, match);
                return builder.where(selection, selectionArgs).query(db, projection, sortOrder);
            }

            default:
            {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

        }
    }

    @Override
    public String getType(Uri uri)
    {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues)
    {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match)
        {
            case USER:
            {
                // add user to user table
                db.insertOrThrow(BabyLogDatabase.Tables.USER, null, contentValues);
                return BabyLogContract.User.buildUri(contentValues.getAsString(BaseColumns._ID));
            }

            case BABY:
            {
                //TODO: create User-Baby Map Table here

                // add user to baby table
                db.insertOrThrow(BabyLogDatabase.Tables.BABY, null, contentValues);
                return BabyLogContract.Baby.buildUri(contentValues.getAsString(BaseColumns._ID));
            }

            case SLEEP:
            {
                // add new activity sleep to activity table
                ContentValues values = new ContentValues();
                values.put(BabyLogContract.ActivityColumns.BABY_ID,
                           contentValues.getAsString(BabyLogContract.SleepColumns.BABY_ID));
                values.put(BabyLogContract.ActivityColumns.ACTIVITY_TYPE, BabyLogContract.Activity.TYPE_SLEEP);
                long actId = db.insertOrThrow(BabyLogDatabase.Tables.ACTIVITY, null, values);

                // add sleep details to sleep table
                contentValues.put(BabyLogContract.SleepColumns.ACTIVITY_ID, actId);
                db.insertOrThrow(BabyLogDatabase.Tables.SLEEP, null, contentValues);
                notifyChange(uri);
                return BabyLogContract.Sleep.buildUri(contentValues.getAsString(BaseColumns._ID));
            }

            default:
            {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

        }
    }

    @Override
    public int delete(Uri uri, String s, String[] strings)
    {
        deleteDataBase();
        return 0;
    }

    private void deleteDataBase()
    {
        mOpenHelper.close();
        BabyLogDatabase.deleteDataBase(getContext());
        mOpenHelper = new BabyLogDatabase(getContext());
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings)
    {
        return 0;
    }

    private void notifyChange(Uri uri)
    {
        Context context = getContext();
        context.getContentResolver().notifyChange(uri, null);
    }

    private  SelectionBuilder buildExpandableSelection(Uri uri, int match)
    {
        final SelectionBuilder builder = new SelectionBuilder();
        switch(match)
        {
            case USER:
            {
                return builder.table(BabyLogDatabase.Tables.USER);
            }

            case BABY:
            {
                return builder.table(BabyLogDatabase.Tables.BABY);
            }

            case SLEEP:
            {
                return builder.table(BabyLogDatabase.Tables.SLEEP);
            }

            default :
            {
                return null;
            }
        }
    }
}
