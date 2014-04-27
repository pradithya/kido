package com.progrema.superbaby.ui.fragment.home;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.progrema.superbaby.R;
import com.progrema.superbaby.adapter.sleephistory.SleepHistoryAdapter;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.ActiveContext;
import com.progrema.superbaby.util.FormatUtils;
import com.progrema.superbaby.widget.customview.ObserveAbleListView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Fragment to log all sleep activity
 */
public class SleepFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_LIST_VIEW = 0;
    private static final int LOADER_SLEEP_FROM_TIME_REFERENCE = 1;
    private ObserveAbleListView sleepHistoryList;
    private Calendar now = Calendar.getInstance();
    private SleepHistoryAdapter mAdapter;
    private TextView nightPercentage;
    private TextView napPercentage;
    private TextView avgNightSleepDuration;
    private TextView avgNapDuration;
    private TextView sleepPercentage;
    private TextView activePercentage;
    private TextView avgSleepDuration;
    private TextView avgActiveDuration;

    public static SleepFragment getInstance() {
        return new SleepFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate fragment layout
        View rootView = inflater.inflate(R.layout.fragment_sleep, container, false);

        // get UI object
        nightPercentage = (TextView) rootView.findViewById(R.id.night_percentage);
        napPercentage = (TextView) rootView.findViewById(R.id.nap_percentage);
        avgNightSleepDuration = (TextView) rootView.findViewById(R.id.avg_night_duration);
        avgNapDuration = (TextView) rootView.findViewById(R.id.avg_nap_duration);
        sleepPercentage = (TextView) rootView.findViewById(R.id.sleep_percentage);
        activePercentage = (TextView) rootView.findViewById(R.id.active_percentage);
        avgSleepDuration = (TextView) rootView.findViewById(R.id.avg_sleep_duration);
        avgActiveDuration = (TextView) rootView.findViewById(R.id.avg_active_duration);

        // set adapter to list view
        sleepHistoryList = (ObserveAbleListView) rootView.findViewById(R.id.activity_list);
        mAdapter = new SleepHistoryAdapter(getActivity(), null, 0);
        sleepHistoryList.setAdapter(mAdapter);

        // prepare loader
        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_LIST_VIEW, null, this);
        lm.initLoader(LOADER_SLEEP_FROM_TIME_REFERENCE, null, this);

        return rootView;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        switch (loaderId) {
            case LOADER_LIST_VIEW: {
                String[] args = {String.valueOf(ActiveContext.getActiveBaby(getActivity()).getID())};
                return new CursorLoader(getActivity(),
                        BabyLogContract.Sleep.CONTENT_URI,
                        BabyLogContract.Sleep.Query.PROJECTION,
                        BabyLogContract.BABY_SELECTION_ARG,
                        args,
                        BabyLogContract.Sleep.Query.SORT_BY_TIMESTAMP_DESC);
            }
            case LOADER_SLEEP_FROM_TIME_REFERENCE: {
                // TODO: timeReference must be configurable based on user input
                String timeReference =
                        String.valueOf(now.getTimeInMillis() - 7 * FormatUtils.DAY_MILLIS);
                String[] argumentSelection =
                        {
                                String.valueOf(ActiveContext.getActiveBaby(getActivity()).getID()),
                                timeReference
                        };
                return new CursorLoader(getActivity(),
                        BabyLogContract.Sleep.CONTENT_URI,
                        BabyLogContract.Sleep.Query.PROJECTION,
                        "baby_id = ? AND timestamp >= ?",
                        argumentSelection,
                        BabyLogContract.Sleep.Query.SORT_BY_TIMESTAMP_DESC);
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (cursor.getCount() > 0) {
            // show last inserted row
            cursor.moveToFirst();

            switch (cursorLoader.getId()) {
                case LOADER_LIST_VIEW: {
                    mAdapter.swapCursor(cursor);
                    break;
                }
                case LOADER_SLEEP_FROM_TIME_REFERENCE: {
                    /**
                     * Calculate average value of nursing from both side since the last 7 days.
                     * That is, get the value from DB than calculate the average value.
                     */
                    long duration = 0, totalDuration = 0;
                    long napDuration = 0, nightDuration = 0, timestamp = 0;
                    long percentageNight, percentageNap;
                    long percentageActive, percentageSleep;
                    long averageNight, averageNap, averageSleep;
                    long one_week_duration = 7 * 24 * 60 * 60 * 1000;

                    ArrayList<Long> nightSleepList = new ArrayList<Long>();
                    ArrayList<Long> napSleepList = new ArrayList<Long>();
                    ArrayList<Long> sleepList = new ArrayList<Long>();

                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        duration = Long.valueOf(
                                cursor.getString(BabyLogContract.Sleep.Query.OFFSET_DURATION));
                        timestamp = Long.valueOf(
                                cursor.getString(BabyLogContract.Sleep.Query.OFFSET_TIMESTAMP));
                        sleepList.add(duration);
                        totalDuration += duration;
                        if (isNight(timestamp)) {
                            nightDuration += duration;
                            nightSleepList.add(duration);
                        } else {
                            napDuration += duration;
                            napSleepList.add(duration);
                        }
                    }

                    percentageNap = napDuration / totalDuration * 100;
                    percentageNight = nightDuration / totalDuration * 100;
                    percentageSleep = totalDuration / one_week_duration * 100;
                    percentageActive = 100 - percentageSleep;
                    averageNight = calculateAverage(nightSleepList);
                    averageNap = calculateAverage(napSleepList);
                    averageSleep = calculateAverage(sleepList);
                    //TODO: calculate average active

                    napPercentage.setText(
                            FormatUtils.formatSleepNapPercentage(getActivity(),
                                    String.valueOf(percentageNap))
                    );

                    nightPercentage.setText(
                            FormatUtils.formatSleepNightPercentage(getActivity(),
                                    String.valueOf(percentageNight))
                    );

                    avgNightSleepDuration.setText(
                            FormatUtils.formatSleepAvgNight(getActivity(),
                                    String.valueOf(averageNight))
                    );

                    avgNapDuration.setText(
                            FormatUtils.formatSleepAvgNap(getActivity(),
                                    String.valueOf(averageNap))
                    );

                    sleepPercentage.setText(
                            FormatUtils.formatSleepPercentage(getActivity(),
                                    String.valueOf(percentageSleep))
                    );

                    activePercentage.setText(
                            FormatUtils.formatActivePercentage(getActivity(),
                                    String.valueOf(percentageActive))
                    );

                    avgSleepDuration.setText(
                            FormatUtils.formatAvgSleep(getActivity(),
                                    String.valueOf(averageSleep))
                    );

                    //TODO: show average active

                    break;
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }

    private long calculateAverage(ArrayList<Long> collection) {
        long average = 0;
        for (float data : collection) {
            average += data;
        }
        try {
            average = average / collection.size();
        } catch (ArithmeticException e) {
            // do nothing if divided by zero
        } finally {
            return average;
        }
    }

    private boolean isNight(long timeStamp) {
        Calendar object = Calendar.getInstance();
        object.setTimeInMillis(timeStamp);
        int hour = object.get(Calendar.HOUR_OF_DAY);

        // TODO: Let user change this day and night boundary
        if ((hour < 6) || (hour > 18))
            return true;

        return false;
    }
}
