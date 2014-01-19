package com.progrema.superbaby.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by iqbalpakeh on 18/1/14.
 */
public class BabyLogContract {

    interface UserColumns{
        String USER_ID = "user_id";
        String PASSWORD = "password";
    }

    interface UserBabyMapColumns{
        String USER_ID = "user_id";
        String BABY_ID = "baby_id";
    }

    interface BabyColumns{
        String BABY_ID = "baby_id";
        String NAME = "name";
        String BIRTHDAY = "birthday";
        String SEX = "sex";
        String HEIGHT = "height";
        String WEIGHT = "weight";
        String PHOTO = "photo";
    }

    interface MilkColumns{
        String ACTIVITY_ID = "activity_id";
        String BABY_ID = "baby_id";
        String TIMESTAMP = "timestamp";
        String DURATION = "duration";
        String SIDES = "sides"; // Left or Right or Formula
        String VOLUME = "volume";
    }

    interface SleepColumns{
        String ACTIVITY_ID = "activity_id";
        String BABY_ID = "baby_id";
        String TIMESTAMP = "timestamp";
        String DURATION = "duration";
    }

    interface DiaperColumns{
        String ACTIVITY_ID = "activity_id";
        String BABY_ID = "baby_id";
        String TIMESTAMP = "timestamp";
        String TYPE = "type"; // Wet or Dry or Mix
    }

    interface VaccineColumns{
        String ACTIVITY_ID = "activity_id";
        String BABY_ID = "baby_id";
        String VACCINE_NAME = "vaccine_name";
        String LOCATION = "location";
        String NOTES = "notes";
        String TIMESTAMP = "timestamp";
        String REMINDER_TIME = "reminder_time";
    }

    interface FoodColumns{
        String ACTIVITY_ID = "activity_id";
        String BABY_ID = "baby_id";
        String TIMESTAMP = "timestamp";
        String DURATION = "duration";
    }

    interface FoodDetailsColumns{
        String ACTIVITY_ID = "activity_id";
        String FOOD_TYPE = "food_type";
        String QUANTITY = "quantity";
    }

    interface FoodTypeColumns{
        String FOOD_TYPE = "food_type";
        String NAME = "name";
    }

    public static final String CONTENT_AUTHORITY = "com.progrema.superbaby";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String PATH_USER = "user";
    private static final String PATH_USER_BABY_MAP = "user_baby_map";
    private static final String PATH_BABY = "baby";
    private static final String PATH_MILK = "milk";
    private static final String PATH_SLEEP = "sleep";
    private static final String PATH_DIAPER ="diaper";
    private static final String PATH_VACCINE = "vaccine";
    private static final String PATH_FOOD = "food";
    private static final String PATH_FOOD_DETAILS = "food_details";
    private static final String PATH_FOOD_TYPE ="food_type";

    public static class User implements UserColumns, BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();
    }

    public static class UserBabyMap implements UserBabyMapColumns, BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER_BABY_MAP).build();
    }

    public static class Baby implements BabyColumns, BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_BABY).build();
    }

    public static class Milk implements MilkColumns, BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MILK).build();
    }

    public static class Sleep implements SleepColumns, BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SLEEP).build();
    }

    public static class Diaper implements DiaperColumns, BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_DIAPER).build();
    }

    public static class Vaccine implements VaccineColumns, BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_VACCINE).build();
    }

    public static class Food implements FoodColumns, BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FOOD).build();
    }

    public static class FoodDetails implements FoodDetailsColumns, BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FOOD_DETAILS).build();
    }

    public static class FoodType implements FoodTypeColumns, BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FOOD_TYPE).build();
    }

}
