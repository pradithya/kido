package com.progrema.superbaby.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by iqbalpakeh on 18/1/14.
 * @author aria
 */
public class BabyLogContract
{

    interface UserColumns
    {
        String USER_NAME = "user_name";
        String PASSWORD = "password";
        String SEC_QUESTION = "security_question";
        String SEC_ANSWER = "security_answer";
    }

    interface UserBabyMapColumns
    {
        String USER_ID = "user_id";
        String BABY_ID = "baby_id";
    }

    interface BabyColumns
    {
        String NAME = "name";
        String BIRTHDAY = "birthday";
        String SEX = "sex";
    }

    interface ActivityColumns
    {
        String BABY_ID = "baby_id";
        String ACTIVITY_TYPE = "activity_type";
        String TIMESTAMP = "timestamp";
    }

    interface NursingColumns
    {
        String ACTIVITY_ID = "activity_id";
        String BABY_ID = "baby_id";
        String TIMESTAMP = "timestamp";
        String DURATION = "duration";
        String SIDES = "sides"; // Left or Right or Formula
        String VOLUME = "volume";
    }

    interface SleepColumns
    {
        String ACTIVITY_ID = "activity_id";
        String BABY_ID = "baby_id";
        String TIMESTAMP = "timestamp";
        String DURATION = "duration";
    }

    interface DiaperColumns
    {
        String ACTIVITY_ID = "activity_id";
        String BABY_ID = "baby_id";
        String TIMESTAMP = "timestamp";
        String TYPE = "type"; // Wet or Dry or Mix
    }

    interface MeasurementTable
    {
        String MEASUREMENT_ID = "activity_id"; //same column name as activity
        String BABY_ID = "baby_id";
        String TIMESTAMP = "timestamp";
        String HEIGHT = "height";
        String WEIGHT = "weight";
    }

    interface PhotoTable
    {
        String PHOTO_ID = "activity_id"; //same column name as activity
        String BABY_ID = "baby_id";
        String TIMESTAMP = "timestamp";
        String PHOTO_LOCATION = "photo_location";
    }

    public static final String CONTENT_AUTHORITY = "com.progrema.superbaby";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String PATH_USER = "user";
    private static final String PATH_USER_BABY_MAP = "user_baby_map";
    private static final String PATH_BABY = "baby";
    private static final String PATH_ACTIVITY = "activity";
    private static final String PATH_MILK = "milk";
    private static final String PATH_SLEEP = "sleep";
    private static final String PATH_DIAPER = "diaper";
    private static final String PATH_MEASUREMENT = "measurement";
    private static final String PATH_PHOTO = "photo";

    public static final String BABY_SELECTION_ARG = "baby_id = ?";

    /**
     * User table contract class
     */
    public static class User implements UserColumns, BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();

        public interface Query
        {
            String[] PROJECTION =
                    {
                            BaseColumns._ID,
                            BabyLogContract.User.USER_NAME,
                            BabyLogContract.User.PASSWORD,
                            BabyLogContract.User.SEC_QUESTION,
                            BabyLogContract.User.SEC_ANSWER
                    };
            final int OFFSET_ID = 0;
            final int OFFSET_NAME = 1;
            final int OFFSET_PASSWORD = 2;
            final int OFFSET_SEC_QUESTION = 3;
            final int OFFSET_SEC_ANSWER = 4;


        }

        public static Uri buildUri(String activityId)
        {
            return CONTENT_URI.buildUpon().appendPath(activityId).build();
        }
    }

    /**
     * UserBabyMap table contract class
     */
    public static class UserBabyMap implements UserBabyMapColumns, BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER_BABY_MAP).build();

        public static Uri buildUri(String activityId)
        {
            return CONTENT_URI.buildUpon().appendPath(activityId).build();
        }
    }

    /**
     * Baby table contract class
     */
    public static class Baby implements BabyColumns, BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_BABY).build();

        public interface Query
        {
            String[] PROJECTION =
                    {
                            BaseColumns._ID,
                            Baby.NAME,
                            Baby.BIRTHDAY,
                            Baby.SEX
                    };
            final int OFFSET_ID = 0;
            final int OFFSET_NAME = 1;
            final int OFFSET_BIRTHDAY = 2;
            final int OFFSET_SEX = 3;
        }

        public static Uri buildUri(String activityId)
        {
            return CONTENT_URI.buildUpon().appendPath(activityId).build();
        }
    }

    /**
     * Activity table contract class
     */
    public static class Activity implements ActivityColumns, BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ACTIVITY).build();
        public static final String TYPE_SLEEP = "SLEEP";
        public static final String TYPE_DIAPER = "DIAPER";
        public static final String TYPE_NURSING = "NURSING";

        public interface Query
        {
            String[] PROJECTION =
                    {
                            BaseColumns._ID,
                            Activity.BABY_ID,
                            Activity.ACTIVITY_TYPE,
                            Activity.TIMESTAMP,
                            Diaper.TYPE,
                            Sleep.DURATION,
                            Nursing.SIDES,
                            Nursing.DURATION,
                            Nursing.VOLUME

                    };
            final int OFFSET_ID = 0;
            final int OFFSET_BABY_ID = 1;
            final int OFFSET_ACTIVITY_TYPE = 2;
            final int OFFSET_TIMESTAMP = 3;
            final int OFFSET_DIAPER_TYPE = 4;
            final int OFFSET_SLEEP_DURATION = 5;
            final int OFFSET_NURSING_SIDES = 6;
            final int OFFSET_NURSING_DURATION = 7;
            final int OFFSET_NURSING_VOLUME = 8;

            final String SORT_BY_TIMESTAMP_ASC = ActivityColumns.TIMESTAMP + " ASC ";
            final String SORT_BY_TIMESTAMP_DESC = ActivityColumns.TIMESTAMP + " DESC ";
        }
    }

    /**
     * Nursing table contract class
     */
    public static class Nursing implements NursingColumns, BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MILK).build();

        public interface Query
        {
            String[] PROJECTION =
                    {
                            BaseColumns._ID,
                            Nursing.ACTIVITY_ID,
                            Nursing.BABY_ID,
                            Nursing.TIMESTAMP,
                            Nursing.SIDES,
                            Nursing.DURATION,
                            Nursing.VOLUME

                    };
            final int OFFSET_ID = 0;
            final int OFFSET_ACTIVITY_ID = 1;
            final int OFFSET_BABY_ID = 2;
            final int OFFSET_TIMESTAMP = 3;
            final int OFFSET_SIDES = 4;
            final int OFFSET_DURATION = 5;
            final int OFFSET_VOLUME = 6;

            final String SORT_BY_TIMESTAMP_ASC = NursingColumns.TIMESTAMP + " ASC ";
            final String SORT_BY_TIMESTAMP_DESC = NursingColumns.TIMESTAMP + " DESC ";
        }


        public static Uri buildUri(String activityId)
        {
            return CONTENT_URI.buildUpon().appendPath(activityId).build();
        }

    }

    /**
     * Sleep table contract class
     */
    public static class Sleep implements SleepColumns, BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SLEEP).build();

        public interface Query
        {
            String[] PROJECTION =
                    {
                            BaseColumns._ID,
                            Sleep.ACTIVITY_ID,
                            Sleep.BABY_ID,
                            Sleep.TIMESTAMP,
                            Sleep.DURATION
                    };
            final int OFFSET_ID = 0;
            final int OFFSET_ACTIVITY_ID = 1;
            final int OFFSET_BABY_ID = 2;
            final int OFFSET_TIMESTAMP = 3;
            final int OFFSET_DURATION = 4;


            final String SORT_BY_TIMESTAMP_ASC = SleepColumns.TIMESTAMP + " ASC ";
            final String SORT_BY_TIMESTAMP_DESC = SleepColumns.TIMESTAMP + " DESC ";
        }

        public static Uri buildUri(String activityId)
        {
            return CONTENT_URI.buildUpon().appendPath(activityId).build();
        }



    }

    /**
     * Diaper table contract class
     */
    public static class Diaper implements DiaperColumns, BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_DIAPER).build();

        public interface Query
        {
            String[] PROJECTION =
                    {
                            BaseColumns._ID,
                            Diaper.ACTIVITY_ID,
                            Diaper.BABY_ID,
                            Diaper.TIMESTAMP,
                            Diaper.TYPE
                    };
            final int OFFSET_ID = 0;
            final int OFFSET_ACTIVITY_ID = 1;
            final int OFFSET_BABY_ID = 2;
            final int OFFSET_TIMESTAMP = 3;
            final int OFFSET_TYPE = 4;

            final String SORT_BY_TIMESTAMP_ASC = DiaperColumns.TIMESTAMP + " ASC ";
            final String SORT_BY_TIMESTAMP_DESC = DiaperColumns.TIMESTAMP + " DESC ";
        }

        public static Uri buildUri(String activityId)
        {
            return CONTENT_URI.buildUpon().appendPath(activityId).build();
        }


    }

    /**
     * Measurement table contract class
     */
    public static class Measurement implements MeasurementTable, BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEASUREMENT).build();
    }

    /**
     * Photo table contract class
     */
    public static class Photo implements PhotoTable, BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PHOTO).build();
    }

}
