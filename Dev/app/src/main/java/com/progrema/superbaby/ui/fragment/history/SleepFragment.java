package com.progrema.superbaby.ui.fragment.history;

import android.app.ActionBar;
import android.database.Cursor;
import android.os.Bundle;
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
import com.progrema.superbaby.util.FormatUtils;
import com.progrema.superbaby.widget.customfragment.HistoryFragment;
import com.progrema.superbaby.widget.customlistview.ObserveableListView;

import java.text.DecimalFormat;

public class SleepFragment extends HistoryFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /*
     * Loader Type used for asynchronous cursor loading
     */
    private static final int LOADER_LIST_VIEW = 0;
    private static final int LOADER_HEADER_INFORMATION = 1;

    /*
     * View Object for header information
     */
    private TextView tvNightPercent;
    private TextView tvNapPercent;
    private TextView tvNightDuration;
    private TextView tvNapDuration;
    private TextView tvTotalDuration;
    private PieGraph pgNapNight;

    /*
     * List and adapter to manage list view
     */
    private ObserveableListView olvSleepHistoryList;
    private SleepAdapter saAdapter;

    public static SleepFragment getInstance() {
        return new SleepFragment();
    }

    @Override
    public View onCreateView(LayoutInflater liInflater, ViewGroup vgContainer,
                             Bundle bSavedInstanceState) {
        // inflate fragment layout
        View vRoot = liInflater.inflate(R.layout.fragment_sleep, vgContainer, false);
        View vPlaceholderRoot = liInflater.inflate(R.layout.placeholder_header, null);
        super.attachQuickReturnView(vRoot, R.id.header_container);
        super.attachPlaceHolderLayout(vPlaceholderRoot, R.id.placeholder_header);

        // set action bar icon and title
        ActionBar abActionBar = getActivity().getActionBar();
        abActionBar.setIcon(getResources().getDrawable(R.drawable.ic_sleep_top));

        // get Header UI object
        tvNightPercent = (TextView) vRoot.findViewById(R.id.percent_night);
        tvNapPercent = (TextView) vRoot.findViewById(R.id.percent_nap);
        tvNightDuration = (TextView) vRoot.findViewById(R.id.duration_night);
        tvNapDuration = (TextView) vRoot.findViewById(R.id.duration_nap);
        tvTotalDuration = (TextView) vRoot.findViewById(R.id.duration_total);
        pgNapNight = (PieGraph) vRoot.findViewById(R.id.sleep_nap_night_pie_chart);

        // set adapter to list view
        olvSleepHistoryList = (ObserveableListView) vRoot.findViewById(R.id.activity_list);
        saAdapter = new SleepAdapter(getActivity(), null, 0);
        olvSleepHistoryList.addHeaderView(vPlaceholderRoot);
        olvSleepHistoryList.setAdapter(saAdapter);
        super.attachListView(olvSleepHistoryList);

        // prepare loader
        LoaderManager lmLoaderManager = getLoaderManager();
        lmLoaderManager.initLoader(LOADER_LIST_VIEW, getArguments(), this);
        lmLoaderManager.initLoader(LOADER_HEADER_INFORMATION, getArguments(), this);

        return vRoot;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        String[] timeFilterArg = getTimeFilterArg(bundle);

        switch (loaderId) {
            case LOADER_LIST_VIEW:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Sleep.CONTENT_URI,
                        BabyLogContract.Sleep.Query.PROJECTION,
                        "baby_id = ? AND timestamp >= ? AND timestamp <= ?",
                        timeFilterArg,
                        BabyLogContract.Sleep.Query.SORT_BY_TIMESTAMP_DESC);

            case LOADER_HEADER_INFORMATION:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Sleep.CONTENT_URI,
                        BabyLogContract.Sleep.Query.PROJECTION,
                        "baby_id = ? AND timestamp >= ? AND timestamp <= ?",
                        timeFilterArg,
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

                case LOADER_HEADER_INFORMATION:

                    float fNightPercentage;
                    float fNapPercentage;
                    long lTotalSleepDuration = 0;
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

                    fNightPercentage = (float)lNightDuration / (float)lTotalSleepDuration * 100;
                    fNapPercentage = (float)lNapDuration / (float)lTotalSleepDuration * 100;
                    DecimalFormat dfForm = new DecimalFormat("0.00");

                    // today nap percentage information
                    tvNapPercent.setText(
                            FormatUtils.fmtSleepNapPct(getActivity(),
                                    String.valueOf(dfForm.format(fNapPercentage)))
                    );

                    // today night sleep percentage information
                    tvNightPercent.setText(
                            FormatUtils.fmtSleepNightPct(getActivity(),
                                    String.valueOf(dfForm.format(fNightPercentage)))
                    );

                    // today nap duration information
                    tvNapDuration.setText(
                            FormatUtils.fmtSleepNapDrt(getActivity(),
                                    String.valueOf(lNapDuration))
                    );
                    PieSlice psNap = new PieSlice();
                    psNap.setColor(getResources().getColor(R.color.orange));
                    psNap.setValue(lNapDuration);
                    pgNapNight.addSlice(psNap);

                    // today night duration information
                    tvNightDuration.setText(
                            FormatUtils.fmtSleepNightDrt(getActivity(),
                                    String.valueOf(lNightDuration))
                    );
                    PieSlice psNight = new PieSlice();
                    psNight.setColor(getResources().getColor(R.color.blue));
                    psNight.setValue(lNightDuration);
                    pgNapNight.addSlice(psNight);

                    // today sleep duration information
                    tvTotalDuration.setText(
                            FormatUtils.fmtSleepDrt(getActivity(),
                                    String.valueOf(lTotalSleepDuration))
                    );

                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        saAdapter.swapCursor(null);
    }

}
