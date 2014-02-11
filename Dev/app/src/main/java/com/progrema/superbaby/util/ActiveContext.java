package com.progrema.superbaby.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.progrema.superbaby.models.Baby;
import com.progrema.superbaby.models.BaseActor;
import com.progrema.superbaby.models.User;
import com.progrema.superbaby.provider.BabyLogContract;

/**
 * Created by iqbalpakeh on 10/2/14.
 */
public class ActiveContext
{
    private static final String PREF_CONTEXT = "prefContext";
    private static final String PREF_BABY_ID = "prefBabyId";
    private static final String PREF_BABY_NAME = "prefBabyName";
    private static final String PREF_BABY_BIRTHDAY = "prefBabyBirthday";
    private static final String PREF_BABY_SEX = "prefBabySex";
    private static final String PREF_USER_ID = "prefUserId";
    private static final String PREF_USER_NAME = "prefUserName";

    public static void setActiveBaby(Context context, String babyName)
    {
        Cursor cursor = babyQuery(context, babyName);
        SharedPreferences setting = context.getSharedPreferences(PREF_CONTEXT, 0);
        SharedPreferences.Editor editor = setting.edit();

        cursor.moveToFirst();
        editor.putLong(PREF_BABY_ID, cursor.getLong(BabyLogContract.Baby.Query.OFFSET_ID));
        editor.putString(PREF_BABY_NAME, cursor.getString(BabyLogContract.Baby.Query.OFFSET_NAME));
        editor.putString(PREF_BABY_BIRTHDAY, cursor.getString(BabyLogContract.Baby.Query.OFFSET_BIRTHDAY));
        editor.putString(PREF_BABY_SEX, cursor.getString(BabyLogContract.Baby.Query.OFFSET_SEX));
        editor.commit();
    }

    public static Baby getActiveBaby(Context context)
    {
        SharedPreferences setting = context.getSharedPreferences(PREF_CONTEXT, 0);
        Baby baby = new Baby();

        baby.setID(setting.getLong(PREF_BABY_ID, 0));
        baby.setName(setting.getString(PREF_BABY_NAME, ""));
        baby.setBirthday(setting.getString(PREF_BABY_BIRTHDAY, ""));
        if (setting.getString(PREF_BABY_SEX, "").equals(BaseActor.Sex.MALE.getTitle()))
        {
            baby.setSex(BaseActor.Sex.MALE);
        }
        else if (setting.getString(PREF_BABY_SEX, "").equals(BaseActor.Sex.FEMALE.getTitle()))
        {
            baby.setSex(BaseActor.Sex.FEMALE);
        }
        return baby;
    }

    public static void setActiveUser(Context context, String userName)
    {
        Cursor cursor = userQuery(context, userName);
        SharedPreferences setting = context.getSharedPreferences(PREF_CONTEXT, 0);
        SharedPreferences.Editor editor = setting.edit();

        cursor.moveToFirst();
        editor.putLong(PREF_USER_ID, cursor.getLong(BabyLogContract.User.Query.OFFSET_ID));
        editor.putString(PREF_USER_NAME, cursor.getString(BabyLogContract.User.Query.OFFSET_USER_NAME));
        editor.commit();
    }

    public static User getActiveUser(Context context)
    {
        SharedPreferences setting = context.getSharedPreferences(PREF_CONTEXT, 0);
        User user = new User();

        user.setID(setting.getLong(PREF_USER_ID, 0));
        user.setName(setting.getString(PREF_USER_NAME, ""));
        return user;
    }

    private static Cursor userQuery(Context context, String username)
    {
        //TODO: we should do query on another thread and show the waiting icon

        String [] selectionArgument = {username};
        return context.getContentResolver().query(BabyLogContract.User.CONTENT_URI,
               BabyLogContract.User.Query.PROJECTION,
               BabyLogContract.User.USER_NAME + "=?",
               selectionArgument,
               BabyLogContract.User.USER_NAME);
    }

    private static Cursor babyQuery(Context context, String babyName)
    {
        //TODO: we should do query on another thread and show the waiting icon

        String [] selectionArgument = {babyName};
        return context.getContentResolver().query(BabyLogContract.Baby.CONTENT_URI,
                BabyLogContract.Baby.Query.PROJECTION,
                BabyLogContract.Baby.NAME + "=?",
                selectionArgument,
                BabyLogContract.Baby.NAME);
    }
}
