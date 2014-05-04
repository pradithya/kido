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
import com.progrema.superbaby.adapter.diaperhistory.DiaperHistoryAdapter;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.ActiveContext;
import com.progrema.superbaby.util.FormatUtils;
import com.progrema.superbaby.widget.customview.ObserveAbleListView;

import java.util.Calendar;

/**
 * Created by iqbalpakeh on 18/1/14.
 *
 * @author aria
 */
public class DiaperFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_LIST_VIEW = 0;
    private static final int LOADER_LAST_WET = 1;
    private static final int LOADER_LAST_DRY = 2;
    private static final int LOADER_LAST_MIXED = 3;
    private ObserveAbleListView diaperHistoryList;
    private DiaperHistoryAdapter mAdapter;
    private TextView WetAverage;
    private TextView WetLast;
    private TextView DryAverage;
    private TextView DryLast;
    private TextView MixAverage;
    private TextView MixLast;

    public static DiaperFragment getInstance() {
        return new DiaperFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate fragment layout
        View rootView = inflater.inflate(R.layout.fragment_diaper, container, false);

        // get Header UI object
        WetAverage = (TextView) rootView.findViewById(R.id.wet_average);
        DryAverage = (TextView) rootView.findViewById(R.id.dry_average);
        MixAverage = (TextView) rootView.findViewById(R.id.mix_average);
        WetLast = (TextView) rootView.findViewById(R.id.wet_last);
        DryLast = (TextView) rootView.findViewById(R.id.dry_last);
        MixLast = (TextView) rootView.findViewById(R.id.mix_last);

        // set adapter to list view
        diaperHistoryList = (ObserveAbleListView) rootView.findViewById(R.id.activity_list);
        mAdapter = new DiaperHistoryAdapter(getActivity(), null, 0);
        diaperHistoryList.addHeaderView(new View(getActivity()));
        diaperHistoryList.addFooterView(new View(getActivity()));
        diaperHistoryList.setAdapter(mAdapter);

        // prepare loader
        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_LIST_VIEW, null, this);
        lm.initLoader(LOADER_LAST_WET, null, this);
        lm.initLoader(LOADER_LAST_DRY, null, this);
        lm.initLoader(LOADER_LAST_MIXED, null, this);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        switch (loaderId){
            case LOADER_LIST_VIEW: {
                String[] args = {String.valueOf(ActiveContext.getActiveBaby(getActivity()).getID())};
                CursorLoader cl = new CursorLoader(getActivity(), BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        BabyLogContract.BABY_SELECTION_ARG,
                        args,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);
                return cl;
            }
            case LOADER_LAST_WET: {
                String[] argumentSelection =
                        {
                                String.valueOf(ActiveContext.getActiveBaby(getActivity()).getID())
                        };
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND type = 'WET'",
                        argumentSelection,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);
            }
            case LOADER_LAST_DRY: {
                String[] argumentSelection =
                        {
                                String.valueOf(ActiveContext.getActiveBaby(getActivity()).getID())
                        };
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND type = 'DRY'",
                        argumentSelection,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);
            }
            case LOADER_LAST_MIXED: {
                String[] argumentSelection =
                        {
                                String.valueOf(ActiveContext.getActiveBaby(getActivity()).getID())
                        };
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND type = 'MIXED'",
                        argumentSelection,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);
            }
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

        if (cursor.getCount() > 0) {
            //show last inserted row
            cursor.moveToFirst();

            switch (cursorLoader.getId()) {
                case LOADER_LIST_VIEW:
                    mAdapter.swapCursor(cursor);
                    break;

                case LOADER_LAST_WET:
                    //show last activity
                    WetLast.setText(FormatUtils.formatDiaperLastActivity(getActivity(),
                            cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TIMESTAMP)));
                    //show average
                    WetAverage.setText(FormatUtils.formatDiaperAverageActivity(getActivity(),
                            calculateUsageAverage(cursor)));
                    break;

                case LOADER_LAST_DRY:
                    //show last activity
                    DryLast.setText(FormatUtils.formatDiaperLastActivity(getActivity(),
                            cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TIMESTAMP)));
                    //show average
                    DryAverage.setText(FormatUtils.formatDiaperAverageActivity(getActivity(),
                            calculateUsageAverage(cursor)));
                    break;

                case LOADER_LAST_MIXED:
                    //show last activity
                    MixLast.setText(FormatUtils.formatDiaperLastActivity(getActivity(),
                            cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TIMESTAMP)));
                    //show average
                    MixAverage.setText(FormatUtils.formatDiaperAverageActivity(getActivity(),
                            calculateUsageAverage(cursor)));
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }

    private String calculateUsageAverage(Cursor cursor){

        double minDateInMilis, maxDateInMilis, oneDayInMilis;
        double dayCount, average = 0;

        cursor.moveToLast();

        oneDayInMilis = 24 * 60 * 60 * 1000;

        maxDateInMilis = Calendar.getInstance().getTimeInMillis();
        minDateInMilis = Long.valueOf(cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TIMESTAMP));

        dayCount = (maxDateInMilis - minDateInMilis) / oneDayInMilis;
        dayCount = Math.ceil(dayCount);

        try {
            average = cursor.getCount() / dayCount;
            average = Math.ceil(average);
        } catch (ArithmeticException e) {
            // do nothing if divided by zero
        } finally {
            return String.valueOf(average);
        }

    }
}
