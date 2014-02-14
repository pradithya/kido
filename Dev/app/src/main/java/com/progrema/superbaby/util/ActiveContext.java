package com.progrema.superbaby.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.progrema.superbaby.models.Baby;
import com.progrema.superbaby.models.BaseActor;
import com.progrema.superbaby.models.User;
import com.progrema.superbaby.provider.BabyLogContract;

/**
 * ActiveContext class holds the active user and the baby. This object can be called anytime from
 * application because it uses android preference class to store the value.
 *
 * Created by iqbalpakeh on 10/2/14.
 */
public class ActiveContext
{
    /**
     * preference name of the active context object
     */
    private static final String PREF_CONTEXT = "prefContext";
    private static final String PREF_BABY_ID = "prefBabyId";
    private static final String PREF_BABY_NAME = "prefBabyName";
    private static final String PREF_BABY_BIRTHDAY = "prefBabyBirthday";
    private static final String PREF_BABY_SEX = "prefBabySex";
    private static final String PREF_USER_ID = "prefUserId";
    private static final String PREF_USER_NAME = "prefUserName";

    /**
     * Set the active baby
     *
     * @param context application context
     * @param babyName active baby name
     */
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

    /**
     * Return the active Baby object
     *
     * @param context application context
     * @return active Baby object
     */
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

    /**
     * Set active user
     *
     * @param context application context
     * @param userName active user's name
     */
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

    /**
     * Get active User object
     *
     * @param context application context
     * @return active User object
     */
    public static User getActiveUser(Context context)
    {
        SharedPreferences setting = context.getSharedPreferences(PREF_CONTEXT, 0);
        User user = new User();

        user.setID(setting.getLong(PREF_USER_ID, 0));
        user.setName(setting.getString(PREF_USER_NAME, ""));
        return user;
    }

    /**
     * query user data from database
     *
     * @param context application context
     * @param username user name
     * @return cursor holding the user data from database
     */
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

    /**
     * query baby data from database
     *
     * @param context application context
     * @param babyName baby name
     * @return cursor holding baby data from database
     */
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
