package com.progrema.superbaby.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.progrema.superbaby.models.Baby;
import com.progrema.superbaby.models.BaseActor;
import com.progrema.superbaby.models.Diaper;
import com.progrema.superbaby.models.Nursing;
import com.progrema.superbaby.models.Sleep;
import com.progrema.superbaby.models.User;
import com.progrema.superbaby.provider.BabyLogContract;

/**
 * ActiveContext class holds the active user, the baby and last activity. This object can be called anytime from
 * application because it uses android preference class to store the value.
 * <p/>
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
    private static final String PREF_LAST_NURSING_TIMESTAMP = "prefLastNursingTimestamp";
    private static final String PREF_LAST_NURSING_DURATION = "prefLastNursingDuration";
    private static final String PREF_LAST_NURSING_TYPE = "prefLastNursingSides";
    private static final String PREF_LAST_NURSING_VOLUME = "prefLastNursingVolume";
    private static final String PREF_LAST_SLEEP_TIMESTAMP = "prefLastSleepTimestamp";
    private static final String PREF_LAST_SLEEP_DURATION = "prefLastSleepDuration";
    private static final String PREF_LAST_DIAPER_TIMESTAMP = "prefLastDiaperTimestamp";
    private static final String PREF_LAST_DIAPER_TYPE = "prefLastDiaperType";

    /**
     * Set the active baby
     *
     * @param context  application context
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
     * @param context  application context
     * @param userName active user's name
     */
    public static void setActiveUser(Context context, String userName)
    {
        Cursor cursor = userQuery(context, userName);
        SharedPreferences setting = context.getSharedPreferences(PREF_CONTEXT, 0);
        SharedPreferences.Editor editor = setting.edit();

        cursor.moveToFirst();
        editor.putLong(PREF_USER_ID, cursor.getLong(BabyLogContract.User.Query.OFFSET_ID));
        editor.putString(PREF_USER_NAME, cursor.getString(BabyLogContract.User.Query.OFFSET_NAME));
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
     * @param context  application context
     * @param username user name
     * @return cursor holding the user data from database
     */
    private static Cursor userQuery(Context context, String username)
    {
        //TODO: we should do query on another thread and show the waiting icon

        String[] selectionArgument = {username};
        return context.getContentResolver().query(BabyLogContract.User.CONTENT_URI,
                BabyLogContract.User.Query.PROJECTION,
                BabyLogContract.User.USER_NAME + "=?",
                selectionArgument,
                BabyLogContract.User.USER_NAME);
    }

    /**
     * query baby data from database
     *
     * @param context  application context
     * @param babyName baby name
     * @return cursor holding baby data from database
     */
    private static Cursor babyQuery(Context context, String babyName)
    {
        //TODO: we should do query on another thread and show the waiting icon

        String[] selectionArgument = {babyName};
        return context.getContentResolver().query(BabyLogContract.Baby.CONTENT_URI,
                BabyLogContract.Baby.Query.PROJECTION,
                BabyLogContract.Baby.NAME + "=?",
                selectionArgument,
                BabyLogContract.Baby.NAME);
    }

    // TODO: we have to now whose last activity...
    public static void setLastNursing(Context context, Nursing nursing)
    {
        SharedPreferences setting = context.getSharedPreferences(PREF_CONTEXT, 0);
        SharedPreferences.Editor editor = setting.edit();

        editor.putString(PREF_LAST_NURSING_TIMESTAMP, nursing.getTimeStampInString());
        editor.putLong(PREF_LAST_NURSING_DURATION, nursing.getDuration());
        editor.putString(PREF_LAST_NURSING_TYPE, nursing.getType().getTitle());
        editor.putLong(PREF_LAST_NURSING_VOLUME, nursing.getVolume());
        editor.commit();
    }

    public static Nursing getLastNursing(Context context)
    {
        SharedPreferences setting = context.getSharedPreferences(PREF_CONTEXT, 0);
        Nursing nursing = new Nursing();

        nursing.setTimeStamp(setting.getString(PREF_LAST_NURSING_TIMESTAMP, ""));
        nursing.setDuration(setting.getLong(PREF_LAST_NURSING_DURATION, 0));
        nursing.setVolume(setting.getLong(PREF_LAST_NURSING_VOLUME, 0));
        if (setting.getString(PREF_LAST_NURSING_TYPE, "").equals(Nursing.NursingType.FORMULA.getTitle()))
        {
            nursing.setType(Nursing.NursingType.FORMULA);
        }
        else if (setting.getString(PREF_LAST_NURSING_TYPE, "").equals(Nursing.NursingType.LEFT.getTitle()))
        {
            nursing.setType(Nursing.NursingType.LEFT);
        }
        else if (setting.getString(PREF_LAST_NURSING_TYPE, "").equals(Nursing.NursingType.RIGHT.getTitle()))
        {
            nursing.setType(Nursing.NursingType.RIGHT);
        }
        return nursing;
    }

    public static void setLastSleep(Context context, Sleep sleep)
    {
        SharedPreferences setting = context.getSharedPreferences(PREF_CONTEXT, 0);
        SharedPreferences.Editor editor = setting.edit();

        editor.putString(PREF_LAST_SLEEP_TIMESTAMP, sleep.getTimeStampInString());
        editor.putLong(PREF_LAST_SLEEP_DURATION, sleep.getDuration());
        editor.commit();
    }

    public static Sleep getLastSleep(Context context)
    {
        SharedPreferences setting = context.getSharedPreferences(PREF_CONTEXT, 0);
        Sleep sleep = new Sleep();

        sleep.setTimeStamp(setting.getString(PREF_LAST_SLEEP_TIMESTAMP, ""));
        sleep.setDuration(setting.getLong(PREF_LAST_SLEEP_DURATION, 0));
        return sleep;
    }

    public static void setLastDiaper(Context context, Diaper diaper)
    {
        SharedPreferences setting = context.getSharedPreferences(PREF_CONTEXT, 0);
        SharedPreferences.Editor editor = setting.edit();

        editor.putString(PREF_LAST_DIAPER_TIMESTAMP, diaper.getTimeStampInString());
        editor.putString(PREF_LAST_DIAPER_TYPE, diaper.getType().getTitle());
        editor.commit();
    }

    public static Diaper getLastDiaper(Context context)
    {
        SharedPreferences setting = context.getSharedPreferences(PREF_CONTEXT, 0);
        Diaper diaper = new Diaper();

        diaper.setTimeStamp(setting.getString(PREF_LAST_DIAPER_TIMESTAMP, ""));
        if (setting.getString(PREF_LAST_DIAPER_TYPE, "").equals(Diaper.DiaperType.DRY.getTitle()))
        {
            diaper.setType(Diaper.DiaperType.DRY);
        }
        else if (setting.getString(PREF_LAST_NURSING_TYPE, "").equals(Diaper.DiaperType.WET.getTitle()))
        {
            diaper.setType(Diaper.DiaperType.WET);
        }
        else if (setting.getString(PREF_LAST_NURSING_TYPE, "").equals(Diaper.DiaperType.MIXED.getTitle()))
        {
            diaper.setType(Diaper.DiaperType.MIXED);
        }
        return diaper;
    }
}
