package com.progrema.superbaby.ui.fragment.home;

import android.app.ActionBar;
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
import com.progrema.superbaby.holograph.PieGraph;
import com.progrema.superbaby.holograph.PieSlice;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.ActiveContext;
import com.progrema.superbaby.util.FormatUtils;
import com.progrema.superbaby.widget.customview.ObserveableListView;

import java.text.DecimalFormat;
import java.util.Calendar;

public class SleepFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_LIST_VIEW = 0;
    private static final int LOADER_TODAY_ENTRY = 1;
    private ObserveableListView olv_sleepHistoryList;
    private SleepHistoryAdapter sha_adapter;
    private TextView tv_nightPct;
    private TextView tv_napPct;
    private TextView tv_todayNightDrt;
    private TextView tv_todayNapDrt;
    private TextView tv_sleepPct;
    private TextView tv_activePct;
    private TextView tv_todaySleepDrt;
    private TextView tv_todayActiveDrt;
    private Calendar c_midnight;
    private PieGraph pg_napNight;
    private PieGraph pg_activeSleep;

    public static SleepFragment getInstance() {
        return new SleepFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate fragment layout
        View v_root = inflater.inflate(R.layout.fragment_sleep, container, false);

        // set action bar icon and title
        ActionBar ab_actionBar = getActivity().getActionBar();
        ab_actionBar.setIcon(getResources().getDrawable(R.drawable.ic_sleep_action_bar_top));

        // get Header UI object
        tv_nightPct = (TextView) v_root.findViewById(R.id.night_percentage);
        tv_napPct = (TextView) v_root.findViewById(R.id.nap_percentage);
        tv_todayNightDrt = (TextView) v_root.findViewById(R.id.today_night_duration);
        tv_todayNapDrt = (TextView) v_root.findViewById(R.id.today_nap_duration);
        tv_sleepPct = (TextView) v_root.findViewById(R.id.sleep_percentage);
        tv_activePct = (TextView) v_root.findViewById(R.id.active_percentage);
        tv_todaySleepDrt = (TextView) v_root.findViewById(R.id.today_sleep_duration);
        tv_todayActiveDrt = (TextView) v_root.findViewById(R.id.today_active_duration);
        pg_napNight = (PieGraph) v_root.findViewById(R.id.sleep_nap_night_pie_chart);
        pg_activeSleep = (PieGraph) v_root.findViewById(R.id.sleep_active_sleep_pie_chart);

        // initiate text value for the first time
        tv_nightPct.setText(getResources().getString(R.string.night_percentage_initial));
        tv_napPct.setText(getResources().getString(R.string.nap_percentage_initial));
        tv_todayNightDrt.setText(getResources().getString(R.string.night_duration_initial));
        tv_todayNapDrt.setText(getResources().getString(R.string.nap_duration_initial));
        tv_sleepPct.setText(getResources().getString(R.string.sleep_percentage_initial));
        tv_activePct.setText(getResources().getString(R.string.active_percentage_initial));
        tv_todaySleepDrt.setText(getResources().getString(R.string.sleep_duration_initial));
        tv_todayActiveDrt.setText(getResources().getString(R.string.active_duration_initial));

        // set adapter to list view
        olv_sleepHistoryList = (ObserveableListView) v_root.findViewById(R.id.activity_list);
        sha_adapter = new SleepHistoryAdapter(getActivity(), null, 0);
        olv_sleepHistoryList.addHeaderView(new View(getActivity()));
        olv_sleepHistoryList.addFooterView(new View(getActivity()));
        olv_sleepHistoryList.setAdapter(sha_adapter);

        // prepare loader
        LoaderManager lm_loaderManager = getLoaderManager();
        lm_loaderManager.initLoader(LOADER_LIST_VIEW, null, this);
        lm_loaderManager.initLoader(LOADER_TODAY_ENTRY, null, this);

        return v_root;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        /**
         * as stated here: http://developer.android.com/reference/java/util/Calendar.html
         * 24:00:00 "belongs" to the following day.
         * That is, 23:59 on Dec 31, 1969 < 24:00 on Jan 1, 1970 < 24:01:00 on Jan 1, 1970
         * form a sequence of three consecutive minutes in time.
         */
        c_midnight = Calendar.getInstance();
        c_midnight.set(Calendar.HOUR_OF_DAY, 0);
        c_midnight.set(Calendar.MINUTE, 0);
        c_midnight.set(Calendar.SECOND, 0);
        c_midnight.set(Calendar.MILLISECOND, 0);
        String s_timestampReference = String.valueOf(c_midnight.getTimeInMillis());

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

                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
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
                            - c_midnight.getTimeInMillis()) - l_totalSleepDuration;
                    f_totalSleepPercentage = ((float) l_totalSleepDuration / (float) l_totalOneDay) * 100;
                    f_totalActivePercentage = 100 - f_totalSleepPercentage;
                    f_nightPercentage = l_nightDuration / l_totalSleepDuration * 100;
                    f_napPercentage = l_napDuration / l_totalSleepDuration * 100;

                    DecimalFormat df_form = new DecimalFormat("0.00");

                    // today nap percentage information
                    tv_napPct.setText(
                            FormatUtils.fmtSleepNapPct(getActivity(),
                                    String.valueOf(df_form.format(f_napPercentage)))
                    );

                    // today night sleep percentage information
                    tv_nightPct.setText(
                            FormatUtils.fmtSleepNightPct(getActivity(),
                                    String.valueOf(df_form.format(f_nightPercentage)))
                    );

                    // today nap duration information
                    tv_todayNapDrt.setText(
                            FormatUtils.fmtSleepNapDrt(getActivity(),
                                    String.valueOf(l_napDuration))
                    );
                    PieSlice ps_nap = new PieSlice();
                    ps_nap.setColor(getResources().getColor(R.color.orange));
                    ps_nap.setValue(l_napDuration);
                    pg_napNight.addSlice(ps_nap);

                    // today night duration information
                    tv_todayNightDrt.setText(
                            FormatUtils.fmtSleepNightDrt(getActivity(),
                                    String.valueOf(l_nightDuration))
                    );
                    PieSlice ps_night = new PieSlice();
                    ps_night.setColor(getResources().getColor(R.color.blue));
                    ps_night.setValue(l_nightDuration);
                    pg_napNight.addSlice(ps_night);

                    // today sleep percentage information
                    tv_sleepPct.setText(
                            FormatUtils.fmtSleepPct(getActivity(),
                                    String.valueOf(df_form.format(f_totalSleepPercentage)))
                    );

                    // today active percentage information
                    tv_activePct.setText(
                            FormatUtils.fmtActivePct(getActivity(),
                                    String.valueOf(df_form.format(f_totalActivePercentage)))
                    );

                    // today sleep duration information
                    tv_todaySleepDrt.setText(
                            FormatUtils.fmtSleepDrt(getActivity(),
                                    String.valueOf(l_totalSleepDuration))
                    );
                    PieSlice ps_sleep = new PieSlice();
                    ps_sleep.setColor(getResources().getColor(R.color.black));
                    ps_sleep.setValue(l_totalSleepDuration);
                    pg_activeSleep.addSlice(ps_sleep);

                    // today active duration information
                    tv_todayActiveDrt.setText(
                            FormatUtils.fmtActiveDrt(getActivity(),
                                    String.valueOf(l_totalActiveDuration))
                    );
                    PieSlice ps_active = new PieSlice();
                    ps_active.setColor(getResources().getColor(R.color.red));
                    ps_active.setValue(l_totalActiveDuration);
                    pg_activeSleep.addSlice(ps_active);

                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        sha_adapter.swapCursor(null);
    }

}
