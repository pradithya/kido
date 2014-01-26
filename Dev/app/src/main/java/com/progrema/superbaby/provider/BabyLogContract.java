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
        String SEC_QUESTION = "security_question";
        String SEC_ANSWER = "security_answer";
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
    }

    interface NursingColumns {
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
        String TIMESTAMP = "timestamp";
        String VACCINE_NAME = "vaccine_name";
        String LOCATION = "location";
        String NOTES = "notes";
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

    interface MeasurementTable{
        String MEASUREMENT_ID = "measurement_id";
        String BABY_ID  = "baby_id";
        String TIMESTAMP = "timestamp";
        String HEIGHT = "height";
        String WEIGHT = "weight";
    }

    interface PhotoTable{
        String PHOTO_ID = "photo_id";
        String BABY_ID = "baby_id";
        String TIMESTAMP = "timestamp";
        String PHOTO_LOCATION = "photo_location";
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
    private static final String PATH_MEASUREMENT = "measurement";
    private static final String PATH_PHOTO = "photo";

    public static class User implements UserColumns, BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();
    }

    public static class UserBabyMap implements UserBabyMapColumns, BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER_BABY_MAP).build();
    }

    public static class Baby implements BabyColumns, BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_BABY).build();
    }

    public static class Nursing implements NursingColumns, BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MILK).build();
    }

    public static class Sleep implements SleepColumns, BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SLEEP).build();

        public static Uri buildSleepUri(String activityId){
            return CONTENT_URI.buildUpon().appendPath(activityId).build();
        }

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

    public static class Measurement implements MeasurementTable, BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEASUREMENT).build();
    }

    public static class Photo implements PhotoTable, BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PHOTO).build();
    }

}
