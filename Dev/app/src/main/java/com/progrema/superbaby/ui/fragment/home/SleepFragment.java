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
import com.progrema.superbaby.adapter.sleep.SleepAdapter;
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
    private ObserveableListView olvSleepHistoryList;
    private SleepAdapter saAdapter;
    private TextView tvNightPct;
    private TextView tvNapPct;
    private TextView tvTodayNightDrt;
    private TextView tvTodayNapDrt;
    private TextView tvSleepPct;
    private TextView tvActivePct;
    private TextView tvTodaySleepDrt;
    private TextView tvTodayActiveDrt;
    private Calendar cMidnight;
    private PieGraph pgNapNight;
    private PieGraph pgActiveSleep;

    public static SleepFragment getInstance() {
        return new SleepFragment();
    }

    @Override
    public View onCreateView(LayoutInflater liInflater, ViewGroup vgContainer,
                             Bundle bSavedInstanceState) {
        // inflate fragment layout
        View vRoot = liInflater.inflate(R.layout.fragment_sleep, vgContainer, false);

        // set action bar icon and title
        ActionBar abActionBar = getActivity().getActionBar();
        abActionBar.setIcon(getResources().getDrawable(R.drawable.ic_sleep_top));
        abActionBar.setTitle(getString(R.string.title_sleep_fragment));

        // get Header UI object
        tvNightPct = (TextView) vRoot.findViewById(R.id.night_percentage);
        tvNapPct = (TextView) vRoot.findViewById(R.id.nap_percentage);
        tvTodayNightDrt = (TextView) vRoot.findViewById(R.id.today_night_duration);
        tvTodayNapDrt = (TextView) vRoot.findViewById(R.id.today_nap_duration);
        tvSleepPct = (TextView) vRoot.findViewById(R.id.sleep_percentage);
        tvActivePct = (TextView) vRoot.findViewById(R.id.active_percentage);
        tvTodaySleepDrt = (TextView) vRoot.findViewById(R.id.today_sleep_duration);
        tvTodayActiveDrt = (TextView) vRoot.findViewById(R.id.today_active_duration);
        pgNapNight = (PieGraph) vRoot.findViewById(R.id.sleep_nap_night_pie_chart);
        pgActiveSleep = (PieGraph) vRoot.findViewById(R.id.sleep_active_sleep_pie_chart);

        // set adapter to list view
        olvSleepHistoryList = (ObserveableListView) vRoot.findViewById(R.id.activity_list);
        saAdapter = new SleepAdapter(getActivity(), null, 0);
        olvSleepHistoryList.addHeaderView(new View(getActivity()));
        olvSleepHistoryList.addFooterView(new View(getActivity()));
        olvSleepHistoryList.setAdapter(saAdapter);

        // prepare loader
        LoaderManager lmLoaderManager = getLoaderManager();
        lmLoaderManager.initLoader(LOADER_LIST_VIEW, null, this);
        lmLoaderManager.initLoader(LOADER_TODAY_ENTRY, null, this);

        return vRoot;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        /**
         * as stated here: http://developer.android.com/reference/java/util/Calendar.html
         * 24:00:00 "belongs" to the following day.
         * That is, 23:59 on Dec 31, 1969 < 24:00 on Jan 1, 1970 < 24:01:00 on Jan 1, 1970
         * form a sequence of three consecutive minutes in time.
         */
        cMidnight = Calendar.getInstance();
        cMidnight.set(Calendar.HOUR_OF_DAY, 0);
        cMidnight.set(Calendar.MINUTE, 0);
        cMidnight.set(Calendar.SECOND, 0);
        cMidnight.set(Calendar.MILLISECOND, 0);
        String sTimestampReference = String.valueOf(cMidnight.getTimeInMillis());

        String[] saArgumentSelectionOne = {
                String.valueOf(ActiveContext.getActiveBaby(getActivity()).getID())
        };

        String[] saArgumentSelectionTwo = {
                String.valueOf(ActiveContext.getActiveBaby(getActivity()).getID()),
                sTimestampReference
        };

        switch (loaderId) {
            case LOADER_LIST_VIEW:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Sleep.CONTENT_URI,
                        BabyLogContract.Sleep.Query.PROJECTION,
                        BabyLogContract.BABY_SELECTION_ARG,
                        saArgumentSelectionOne,
                        BabyLogContract.Sleep.Query.SORT_BY_TIMESTAMP_DESC);

            case LOADER_TODAY_ENTRY:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Sleep.CONTENT_URI,
                        BabyLogContract.Sleep.Query.PROJECTION,
                        "baby_id = ? AND timestamp >= ?",
                        saArgumentSelectionTwo,
                        BabyLogContract.Sleep.Query.SORT_BY_TIMESTAMP_DESC);

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cCursor) {
        if (cCursor.getCount() > 0) {
            cCursor.moveToFirst();
            switch (cursorLoader.getId()) {
                case LOADER_LIST_VIEW:
                    saAdapter.swapCursor(cCursor);
                    break;

                case LOADER_TODAY_ENTRY:

                    float fTotalSleepPercentage;
                    float fTotalActivePercentage;
                    float fNightPercentage;
                    float fNapPercentage;
                    long lTotalOneDay = 24 * 60 * 60;
                    long lTotalSleepDuration = 0;
                    long lTotalActiveDuration;
                    long lDuration;
                    long lNightDuration = 0;
                    long lNapDuration = 0;
                    long lTimestamp;

                    for (cCursor.moveToFirst(); !cCursor.isAfterLast(); cCursor.moveToNext()) {
                        lDuration = Long.valueOf(
                                cCursor.getString(BabyLogContract.Sleep.Query.OFFSET_DURATION));
                        lTimestamp = Long.valueOf(
                                cCursor.getString(BabyLogContract.Sleep.Query.OFFSET_TIMESTAMP));
                        lTotalSleepDuration += lDuration;
                        if (FormatUtils.isNight(lTimestamp)) {
                            lNightDuration += lDuration;
                        } else {
                            lNapDuration += lDuration;
                        }
                    }

                    lTotalActiveDuration
                            = (Calendar.getInstance().getTimeInMillis()
                            - cMidnight.getTimeInMillis()) - lTotalSleepDuration;
                    fTotalSleepPercentage = ((float) lTotalSleepDuration / (float) lTotalOneDay) * 100;
                    fTotalActivePercentage = 100 - fTotalSleepPercentage;
                    fNightPercentage = lNightDuration / lTotalSleepDuration * 100;
                    fNapPercentage = lNapDuration / lTotalSleepDuration * 100;

                    DecimalFormat dfForm = new DecimalFormat("0.00");

                    // today nap percentage information
                    tvNapPct.setText(
                            FormatUtils.fmtSleepNapPct(getActivity(),
                                    String.valueOf(dfForm.format(fNapPercentage)))
                    );

                    // today night sleep percentage information
                    tvNightPct.setText(
                            FormatUtils.fmtSleepNightPct(getActivity(),
                                    String.valueOf(dfForm.format(fNightPercentage)))
                    );

                    // today nap duration information
                    tvTodayNapDrt.setText(
                            FormatUtils.fmtSleepNapDrt(getActivity(),
                                    String.valueOf(lNapDuration))
                    );
                    PieSlice psNap = new PieSlice();
                    psNap.setColor(getResources().getColor(R.color.orange));
                    psNap.setValue(lNapDuration);
                    pgNapNight.addSlice(psNap);

                    // today night duration information
                    tvTodayNightDrt.setText(
                            FormatUtils.fmtSleepNightDrt(getActivity(),
                                    String.valueOf(lNightDuration))
                    );
                    PieSlice psNight = new PieSlice();
                    psNight.setColor(getResources().getColor(R.color.blue));
                    psNight.setValue(lNightDuration);
                    pgNapNight.addSlice(psNight);

                    // today sleep percentage information
                    tvSleepPct.setText(
                            FormatUtils.fmtSleepPct(getActivity(),
                                    String.valueOf(dfForm.format(fTotalSleepPercentage)))
                    );

                    // today active percentage information
                    tvActivePct.setText(
                            FormatUtils.fmtActivePct(getActivity(),
                                    String.valueOf(dfForm.format(fTotalActivePercentage)))
                    );

                    // today sleep duration information
                    tvTodaySleepDrt.setText(
                            FormatUtils.fmtSleepDrt(getActivity(),
                                    String.valueOf(lTotalSleepDuration))
                    );
                    PieSlice psSleep = new PieSlice();
                    psSleep.setColor(getResources().getColor(R.color.black));
                    psSleep.setValue(lTotalSleepDuration);
                    pgActiveSleep.addSlice(psSleep);

                    // today active duration information
                    tvTodayActiveDrt.setText(
                            FormatUtils.fmtActiveDrt(getActivity(),
                                    String.valueOf(lTotalActiveDuration))
                    );
                    PieSlice ps_active = new PieSlice();
                    ps_active.setColor(getResources().getColor(R.color.red));
                    ps_active.setValue(lTotalActiveDuration);
                    pgActiveSleep.addSlice(ps_active);

                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        saAdapter.swapCursor(null);
    }

}
