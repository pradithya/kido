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
import com.progrema.superbaby.models.Diaper;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.ActiveContext;
import com.progrema.superbaby.util.FormatUtils;
import com.progrema.superbaby.widget.customview.ObserveAbleListView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by iqbalpakeh on 18/1/14.
 *
 * @author aria
 */
public class DiaperFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_DIAPER_LIST_VIEW = 0;
    private static final int LOADER_DIAPER_FROM_TIME_REFERENCE = 1;
    private static final int LOADER_DIAPER_LAST_WET = 2;
    private static final int LOADER_DIAPER_LAST_DRY = 3;
    private static final int LOADER_DIAPER_LAST_MIX = 4;
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
        diaperHistoryList.setAdapter(mAdapter);

        // prepare loader
        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_DIAPER_LIST_VIEW, null, this);
        lm.initLoader(LOADER_DIAPER_FROM_TIME_REFERENCE, null, this);
        lm.initLoader(LOADER_DIAPER_LAST_WET, null, this);
        lm.initLoader(LOADER_DIAPER_LAST_DRY, null, this);
        lm.initLoader(LOADER_DIAPER_LAST_MIX, null, this);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        switch (loaderId){

            case LOADER_DIAPER_LIST_VIEW: {
                String[] args = {String.valueOf(ActiveContext.getActiveBaby(getActivity()).getID())};
                CursorLoader cl = new CursorLoader(getActivity(), BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        BabyLogContract.BABY_SELECTION_ARG,
                        args,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);
                return cl;
            }
            case LOADER_DIAPER_FROM_TIME_REFERENCE: {
                // TODO: timeReference must be configurable based on user input
                Calendar now = Calendar.getInstance();
                String timeReference = String.valueOf(now.getTimeInMillis() - 7 * FormatUtils.DAY_MILLIS);
                String[] argumentSelection =
                        {
                                String.valueOf(ActiveContext.getActiveBaby(getActivity()).getID()),
                                timeReference
                        };
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND timestamp >= ?",
                        argumentSelection,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);
            }
            case LOADER_DIAPER_LAST_WET: {
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
            case LOADER_DIAPER_LAST_DRY: {
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
            case LOADER_DIAPER_LAST_MIX: {
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
                case LOADER_DIAPER_LIST_VIEW:
                    mAdapter.swapCursor(cursor);
                    break;

                case LOADER_DIAPER_FROM_TIME_REFERENCE:
                    /**
                     * Calculate average value of nursing from both side since the last 7 days.
                     * That is, get the value from DB than calculate the average value.
                     */
                    float wetAverage = 0, dryAverage = 0, mixAverage = 0;
                    ArrayList<Float> wetList = new ArrayList<Float>();
                    ArrayList<Float> dryList = new ArrayList<Float>();
                    ArrayList<Float> mixList = new ArrayList<Float>();

                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        String type = cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TYPE);
                        if (type.equals(Diaper.DiaperType.WET.getTitle())) {
                            wetList.add(Float.valueOf(cursor
                                    .getString(BabyLogContract.Diaper.Query.OFFSET_TIMESTAMP)));
                        }
                        else if (type.equals(Diaper.DiaperType.DRY.getTitle())) {
                            dryList.add(Float.valueOf(cursor
                                    .getString(BabyLogContract.Diaper.Query.OFFSET_TIMESTAMP)));
                        }
                        else if (type.equals(Diaper.DiaperType.MIXED.getTitle())) {
                            mixList.add(Float.valueOf(cursor
                                    .getString(BabyLogContract.Diaper.Query.OFFSET_TIMESTAMP)));
                        }
                    }

                    wetAverage = calculateAverage(wetList);
                    dryAverage = calculateAverage(dryList);
                    mixAverage = calculateAverage(mixList);

                    WetAverage.setText(
                            FormatUtils.formatDiaperAverageWet(getActivity(),
                                    String.valueOf(wetAverage))
                    );

                    DryAverage.setText(
                            FormatUtils.formatDiaperAverageDry(getActivity(),
                                    String.valueOf(dryAverage))
                    );

                    MixAverage.setText(
                            FormatUtils.formatDiaperAverageMix(getActivity(),
                                    String.valueOf(mixAverage))
                    );

                    break;

                case LOADER_DIAPER_LAST_WET:
                    WetLast.setText(FormatUtils.formatDiaperLastActivity(getActivity(),
                            cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TIMESTAMP)));
                    break;

                case LOADER_DIAPER_LAST_DRY:
                    DryLast.setText(FormatUtils.formatDiaperLastActivity(getActivity(),
                            cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TIMESTAMP)));
                    break;

                case LOADER_DIAPER_LAST_MIX:
                    MixLast.setText(FormatUtils.formatDiaperLastActivity(getActivity(),
                            cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TIMESTAMP)));
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }

    private long calculateAverage(ArrayList<Float> collection) {
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
}
