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
    private ObserveAbleListView olv_sleepHistoryList;
    private SleepHistoryAdapter sha_adapter;
    private TextView tv_nightPct;
    private TextView tv_napPct;
    private TextView tv_todayNightDrt;
    private TextView tv_todayNapDrt;
    private TextView tv_sleepPct;
    private TextView tv_activePct;
    private TextView tv_todaySleepDrt;
    private TextView tv_todayActiveDrt;
    private Calendar c_today;

    public static SleepFragment getInstance() {
        return new SleepFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate fragment layout
        View v_rootView = inflater.inflate(R.layout.fragment_sleep, container, false);

        // get Header UI object
        tv_nightPct = (TextView) v_rootView.findViewById(R.id.night_percentage);
        tv_napPct = (TextView) v_rootView.findViewById(R.id.nap_percentage);
        tv_todayNightDrt = (TextView) v_rootView.findViewById(R.id.today_night_duration);
        tv_todayNapDrt = (TextView) v_rootView.findViewById(R.id.today_nap_duration);
        tv_sleepPct = (TextView) v_rootView.findViewById(R.id.sleep_percentage);
        tv_activePct = (TextView) v_rootView.findViewById(R.id.active_percentage);
        tv_todaySleepDrt = (TextView) v_rootView.findViewById(R.id.today_sleep_duration);
        tv_todayActiveDrt = (TextView) v_rootView.findViewById(R.id.today_active_duration);

        // set adapter to list view
        olv_sleepHistoryList = (ObserveAbleListView) v_rootView.findViewById(R.id.activity_list);
        sha_adapter = new SleepHistoryAdapter(getActivity(), null, 0);
        olv_sleepHistoryList.addHeaderView(new View(getActivity()));
        olv_sleepHistoryList.addFooterView(new View(getActivity()));
        olv_sleepHistoryList.setAdapter(sha_adapter);

        // prepare loader
        LoaderManager lm_loaderManager = getLoaderManager();
        lm_loaderManager.initLoader(LOADER_LIST_VIEW, null, this);
        lm_loaderManager.initLoader(LOADER_TODAY_ENTRY, null, this);

        return v_rootView;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        /**
         * as stated here: http://developer.android.com/reference/java/util/Calendar.html
         * 24:00:00 "belongs" to the following day.
         * That is, 23:59 on Dec 31, 1969 < 24:00 on Jan 1, 1970 < 24:01:00 on Jan 1, 1970
         * form a sequence of three consecutive minutes in time.
         */
        c_today = Calendar.getInstance();
        c_today.set(Calendar.HOUR_OF_DAY, 0);
        c_today.set(Calendar.MINUTE, 0);
        c_today.set(Calendar.SECOND, 0);
        c_today.set(Calendar.MILLISECOND, 0);
        String s_timestampReference = String.valueOf(c_today.getTimeInMillis());

        String[] sa_argumentSelectionOne = {
                String.valueOf(ActiveContext.getActiveBaby(getActivity()).getID())
        };

        String[] sa_argumentSelectionTwo = {
                String.valueOf(ActiveContext.getActiveBaby(getActivity()).getID()),
                s_timestampReference
        };

        switch (loaderId) {
            case LOADER_LIST_VIEW:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Sleep.CONTENT_URI,
                        BabyLogContract.Sleep.Query.PROJECTION,
                        BabyLogContract.BABY_SELECTION_ARG,
                        sa_argumentSelectionOne,
                        BabyLogContract.Sleep.Query.SORT_BY_TIMESTAMP_DESC);

            case LOADER_TODAY_ENTRY:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Sleep.CONTENT_URI,
                        BabyLogContract.Sleep.Query.PROJECTION,
                        "baby_id = ? AND timestamp >= ?",
                        sa_argumentSelectionTwo,
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
                    sha_adapter.swapCursor(cursor);
                    break;

                case LOADER_TODAY_ENTRY:

                    float f_totalSleepPercentage;
                    float f_totalActivePercentage;
                    float f_nightPercentage;
                    float f_napPercentage;
                    long l_totalOneDay = 24 * 60 * 60;
                    long l_totalSleepDuration = 0;
                    long l_totalActiveDuration;
                    long l_duration;
                    long l_nightDuration = 0;
                    long l_napDuration = 0;
                    long l_timestamp;

                    for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                        l_duration = Long.valueOf(
                                cursor.getString(BabyLogContract.Sleep.Query.OFFSET_DURATION));
                        l_timestamp = Long.valueOf(
                                cursor.getString(BabyLogContract.Sleep.Query.OFFSET_TIMESTAMP));
                        l_totalSleepDuration += l_duration;
                        if (FormatUtils.isNight(l_timestamp)) {
                            l_nightDuration += l_duration;
                        } else {
                            l_napDuration += l_duration;
                        }
                    }

                    l_totalActiveDuration
                            = (Calendar.getInstance().getTimeInMillis()
                            - c_today.getTimeInMillis()) - l_totalSleepDuration;
                    f_totalSleepPercentage = ((float)l_totalSleepDuration / (float)l_totalOneDay) * 100;
                    f_totalActivePercentage = 100 - f_totalSleepPercentage;
                    f_nightPercentage = l_nightDuration / l_totalSleepDuration * 100;
                    f_napPercentage = l_napDuration / l_totalSleepDuration * 100;

                    tv_napPct.setText(
                            FormatUtils.fmtSleepNapPct(getActivity(),
                                    String.valueOf(f_napPercentage))
                    );

                    tv_nightPct.setText(
                            FormatUtils.fmtSleepNightPct(getActivity(),
                                    String.valueOf(f_nightPercentage))
                    );

                    tv_todayNightDrt.setText(
                            FormatUtils.fmtSleepNightDrt(getActivity(),
                                    String.valueOf(l_nightDuration))
                    );

                    tv_todayNapDrt.setText(
                            FormatUtils.fmtSleepNapDrt(getActivity(),
                                    String.valueOf(l_napDuration))
                    );

                    tv_sleepPct.setText(
                            FormatUtils.fmtSleepPct(getActivity(),
                                    String.valueOf(f_totalSleepPercentage))
                    );

                    tv_activePct.setText(
                            FormatUtils.fmtActivePct(getActivity(),
                                    String.valueOf(f_totalActivePercentage))
                    );

                    tv_todaySleepDrt.setText(
                            FormatUtils.fmtSleepDrt(getActivity(),
                                    String.valueOf(l_totalSleepDuration))
                    );

                    tv_todayActiveDrt.setText(
                            FormatUtils.fmtActiveDrt(getActivity(),
                                    String.valueOf(l_totalActiveDuration))
                    );

                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        sha_adapter.swapCursor(null);
    }

}
