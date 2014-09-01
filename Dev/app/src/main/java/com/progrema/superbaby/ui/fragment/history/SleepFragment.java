package com.progrema.superbaby.ui.fragment.history;

import android.app.ActionBar;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
import com.progrema.superbaby.ui.activity.HomeActivity;
import com.progrema.superbaby.util.FormatUtils;
import com.progrema.superbaby.widget.customfragment.HistoryFragment;
import com.progrema.superbaby.widget.customlistview.ObserveableListView;

import java.text.DecimalFormat;

public class SleepFragment extends HistoryFragment implements
        LoaderManager.LoaderCallbacks<Cursor>, HistoryFragmentServices, SleepAdapter.Callback {

    private static final int LOADER_LIST_VIEW = 0;
    private static final int LOADER_GENERAL_ENTRY = 1;
    private TextView nightPercentHandler;
    private TextView napPercentHandler;
    private TextView nightDurationHandler;
    private TextView napDurationHandler;
    private TextView totalDurationHandler;
    private PieGraph pieGraphHandler;
    private ObserveableListView sleepHistoryList;
    private SleepAdapter adapter;
    private View root;
    private View placeholder;

    public static SleepFragment getInstance() {
        return new SleepFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        prepareFragment(inflater, container);
        prepareHandler();
        prepareListView();
        prepareLoaderManager();
        return root;
    }

    @Override
    public void prepareFragment(LayoutInflater inflater, ViewGroup container) {
        root = inflater.inflate(R.layout.fragment_sleep, container, false);
        placeholder = inflater.inflate(R.layout.placeholder_header, null);
        super.attachQuickReturnView(root, R.id.header_container);
        super.attachPlaceHolderLayout(placeholder, R.id.placeholder_header);
        ActionBar actionbar = getActivity().getActionBar();
        actionbar.setIcon(getResources().getDrawable(R.drawable.ic_sleep_top));
    }

    @Override
    public void prepareHandler() {
        nightPercentHandler = (TextView) root.findViewById(R.id.percent_night);
        napPercentHandler = (TextView) root.findViewById(R.id.percent_nap);
        nightDurationHandler = (TextView) root.findViewById(R.id.duration_night);
        napDurationHandler = (TextView) root.findViewById(R.id.duration_nap);
        totalDurationHandler = (TextView) root.findViewById(R.id.duration_total);
        pieGraphHandler = (PieGraph) root.findViewById(R.id.sleep_nap_night_pie_chart);
    }

    @Override
    public void prepareListView() {
        sleepHistoryList = (ObserveableListView) root.findViewById(R.id.activity_list);
        adapter = new SleepAdapter(getActivity(), null, 0);
        adapter.setCallback(this);
        sleepHistoryList.addHeaderView(placeholder);
        sleepHistoryList.setAdapter(adapter);
        super.attachListView(sleepHistoryList);
    }

    @Override
    public void onNursingSleepEditSelected(View entry) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(HomeActivity.ACTIVITY_TRIGGER_KEY, HomeActivity.Trigger.SLEEP.getTitle());
        bundle.putString(HomeActivity.ACTIVITY_EDIT_KEY, getResources().getString(R.string.menu_edit));
        bundle.putString(HomeActivity.ACTIVITY_ENTRY_TAG_KEY, entry.getTag().toString());
        StopwatchFragment frStopWatch = StopwatchFragment.getInstance();
        frStopWatch.setArguments(bundle);
        fragmentTransaction.replace(R.id.home_activity_container, frStopWatch);
        fragmentTransaction.commit();
    }

    @Override
    public void prepareLoaderManager() {
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_LIST_VIEW, getArguments(), this);
        loaderManager.initLoader(LOADER_GENERAL_ENTRY, getArguments(), this);
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
            case LOADER_GENERAL_ENTRY:
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
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            switch (cursorLoader.getId()) {
                case LOADER_LIST_VIEW:
                    adapter.swapCursor(cursor);
                    break;
                case LOADER_GENERAL_ENTRY:
                    inflateGeneralEntry(cursor);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        adapter.swapCursor(null);
    }

    private void inflateGeneralEntry(Cursor cursor) {
        SleepFragmentEntry entry = new SleepFragmentEntry();
        entry.prepareEntry(cursor);
        inflateNapPercentEntry(entry);
        inflateNightPercentEntry(entry);
        inflateNapDurationEntry(entry);
        inflateNightDurationEntry(entry);
        inflateTotalSleepDurationEntry(entry);
        inflatePieChart(entry);
    }

    private void inflateNapPercentEntry(SleepFragmentEntry entry) {
        napPercentHandler.setText(
                FormatUtils.fmtSleepNapPct(getActivity(), entry.getNapPercentage()));
    }

    private void inflateNightPercentEntry(SleepFragmentEntry entry) {
        nightPercentHandler.setText(
                FormatUtils.fmtSleepNightPct(getActivity(), entry.getNightPercentage()));
    }

    private void inflateNapDurationEntry(SleepFragmentEntry entry) {
        napDurationHandler.setText(
                FormatUtils.fmtSleepNapDrt(getActivity(), String.valueOf(entry.getNapDuration())));
    }

    private void inflateNightDurationEntry(SleepFragmentEntry entry) {
        nightDurationHandler.setText(
                FormatUtils.fmtSleepNightDrt(getActivity(), String.valueOf(entry.getNightDuration())));
    }

    private void inflateTotalSleepDurationEntry(SleepFragmentEntry entry) {
        totalDurationHandler.setText(
                FormatUtils.fmtSleepDrt(getActivity(), String.valueOf(entry.getTotalSleepDuration())));
    }

    private void inflatePieChart(SleepFragmentEntry entry) {
        removeSlices();
        inflateNightSlice(entry);
        inflateNapSlice(entry);
    }

    private void removeSlices() {
        pieGraphHandler.removeSlices();
    }

    private void inflateNightSlice(SleepFragmentEntry entry) {
        PieSlice psNight = new PieSlice();
        psNight.setColor(getResources().getColor(R.color.blue));
        psNight.setValue(entry.getNightDuration());
        pieGraphHandler.addSlice(psNight);
    }

    private void inflateNapSlice(SleepFragmentEntry entry) {
        PieSlice psNap = new PieSlice();
        psNap.setColor(getResources().getColor(R.color.orange));
        psNap.setValue(entry.getNapDuration());
        pieGraphHandler.addSlice(psNap);
    }

    private class SleepFragmentEntry {

        private DecimalFormat decimalConverter = new DecimalFormat("0.00");
        private long totalSleepDuration;
        private long duration;
        private long nightDuration;
        private long napDuration ;
        private long timestamp;

        public void prepareEntry(Cursor cursor) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                duration = Long.valueOf(cursor.getString(BabyLogContract.Sleep.Query.OFFSET_DURATION));
                timestamp = Long.valueOf(cursor.getString(BabyLogContract.Sleep.Query.OFFSET_TIMESTAMP));
                totalSleepDuration += duration;
                if (FormatUtils.isNightTime(timestamp)) {
                    nightDuration += duration;
                } else {
                    napDuration += duration;
                }
            }
        }

        public String getNightPercentage() {
            return decimalConverter.format((float)nightDuration / (float)totalSleepDuration * 100);
        }

        public String getNapPercentage() {
            return decimalConverter.format((float)napDuration / (float)totalSleepDuration * 100);
        }

        public long getTotalSleepDuration() {
            return totalSleepDuration;
        }

        public long getNightDuration() {
            return nightDuration;
        }

        public long getNapDuration() {
            return napDuration;
        }
    }

}
