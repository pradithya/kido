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

import java.util.Calendar;

public class SleepFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_LIST_VIEW = 0;
    private static final int LOADER_TODAY_ENTRY = 1;
    private ObserveAbleListView sleepHistoryList;
    private SleepHistoryAdapter mAdapter;
    private TextView tvNightPct;
    private TextView tvNapPct;
    private TextView tvTodayNightDrt;
    private TextView tvTodayNapDrt;
    private TextView tvSleepPct;
    private TextView tvActivePct;
    private TextView tvTodaySleepDrt;
    private TextView tvTodayActiveDrt;
    private Calendar today;

    public static SleepFragment getInstance() {
        return new SleepFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate fragment layout
        View rootView = inflater.inflate(R.layout.fragment_sleep, container, false);

        // get Header UI object
        tvNightPct = (TextView) rootView.findViewById(R.id.night_percentage);
        tvNapPct = (TextView) rootView.findViewById(R.id.nap_percentage);
        tvTodayNightDrt = (TextView) rootView.findViewById(R.id.today_night_duration);
        tvTodayNapDrt = (TextView) rootView.findViewById(R.id.today_nap_duration);
        tvSleepPct = (TextView) rootView.findViewById(R.id.sleep_percentage);
        tvActivePct = (TextView) rootView.findViewById(R.id.active_percentage);
        tvTodaySleepDrt = (TextView) rootView.findViewById(R.id.today_sleep_duration);
        tvTodayActiveDrt = (TextView) rootView.findViewById(R.id.today_active_duration);

        // set adapter to list view
        sleepHistoryList = (ObserveAbleListView) rootView.findViewById(R.id.activity_list);
        mAdapter = new SleepHistoryAdapter(getActivity(), null, 0);
        sleepHistoryList.addHeaderView(new View(getActivity()));
        sleepHistoryList.addFooterView(new View(getActivity()));
        sleepHistoryList.setAdapter(mAdapter);

        // prepare loader
        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_LIST_VIEW, null, this);
        lm.initLoader(LOADER_TODAY_ENTRY, null, this);

        return rootView;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        /**
         * as stated here: http://developer.android.com/reference/java/util/Calendar.html
         * 24:00:00 "belongs" to the following day.
         * That is, 23:59 on Dec 31, 1969 < 24:00 on Jan 1, 1970 < 24:01:00 on Jan 1, 1970
         * form a sequence of three consecutive minutes in time.
         */
        today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        String timestampReference = String.valueOf(today.getTimeInMillis());

        String[] argumentSelectionOne = {
                String.valueOf(ActiveContext.getActiveBaby(getActivity()).getID())
        };

        String[] argumentSelectionTwo = {
                String.valueOf(ActiveContext.getActiveBaby(getActivity()).getID()),
                timestampReference
        };

        switch (loaderId) {
            case LOADER_LIST_VIEW:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Sleep.CONTENT_URI,
                        BabyLogContract.Sleep.Query.PROJECTION,
                        BabyLogContract.BABY_SELECTION_ARG,
                        argumentSelectionOne,
                        BabyLogContract.Sleep.Query.SORT_BY_TIMESTAMP_DESC);

            case LOADER_TODAY_ENTRY:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Sleep.CONTENT_URI,
                        BabyLogContract.Sleep.Query.PROJECTION,
                        "baby_id = ? AND timestamp >= ?",
                        argumentSelectionTwo,
                        BabyLogContract.Sleep.Query.SORT_BY_TIMESTAMP_DESC);

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            switch (cursorLoader.getId()) {
                case LOADER_LIST_VIEW:
                    mAdapter.swapCursor(cursor);
                    break;

                case LOADER_TODAY_ENTRY:

                    float totalSleepPercentage;
                    float totalActivePercentage;
                    float nightPercentage;
                    float napPercentage;
                    long totalOneDay = 24 * 60 * 60;
                    long totalSleepDuration = 0;
                    long totalActiveDuration;
                    long duration;
                    long nightDuration = 0;
                    long napDuration = 0;
                    long timestamp;

                    for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                        duration = Long.valueOf(
                                cursor.getString(BabyLogContract.Sleep.Query.OFFSET_DURATION));
                        timestamp = Long.valueOf(
                                cursor.getString(BabyLogContract.Sleep.Query.OFFSET_TIMESTAMP));
                        totalSleepDuration += duration;
                        if (isNight(timestamp)) {
                            nightDuration += duration;
                        } else {
                            napDuration += duration;
                        }
                    }

                    totalActiveDuration
                            = (Calendar.getInstance().getTimeInMillis()
                            - today.getTimeInMillis()) - totalSleepDuration;
                    totalSleepPercentage = ((float)totalSleepDuration / (float)totalOneDay) * 100;
                    totalActivePercentage = 100 - totalSleepPercentage;
                    nightPercentage = nightDuration / totalSleepDuration * 100;
                    napPercentage = napDuration / totalSleepDuration * 100;

                    tvNapPct.setText(
                            FormatUtils.fmtSleepNapPct(getActivity(),
                                    String.valueOf(napPercentage))
                    );

                    tvNightPct.setText(
                            FormatUtils.fmtSleepNightPct(getActivity(),
                                    String.valueOf(nightPercentage))
                    );

                    tvTodayNightDrt.setText(
                            FormatUtils.fmtSleepNightDrt(getActivity(),
                                    String.valueOf(nightDuration))
                    );

                    tvTodayNapDrt.setText(
                            FormatUtils.fmtSleepNapDrt(getActivity(),
                                    String.valueOf(napDuration))
                    );

                    tvSleepPct.setText(
                            FormatUtils.fmtSleepPct(getActivity(),
                                    String.valueOf(totalSleepPercentage))
                    );

                    tvActivePct.setText(
                            FormatUtils.fmtActivePct(getActivity(),
                                    String.valueOf(totalActivePercentage))
                    );

                    tvTodaySleepDrt.setText(
                            FormatUtils.fmtSleepDrt(getActivity(),
                                    String.valueOf(totalSleepDuration))
                    );

                    tvTodayActiveDrt.setText(
                            FormatUtils.fmtActiveDrt(getActivity(),
                                    String.valueOf(totalActiveDuration))
                    );

                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }

    private boolean isNight(Long timeStamp) {
        Calendar object = Calendar.getInstance();
        object.setTimeInMillis(timeStamp);
        int hour = object.get(Calendar.HOUR_OF_DAY);

        // TODO: Let user change this day and night boundary
        if ((hour < 6) || (hour > 18))
            return true;

        return false;
    }
}
