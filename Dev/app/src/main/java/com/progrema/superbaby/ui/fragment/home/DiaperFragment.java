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
import com.progrema.superbaby.adapter.diaperhistory.DiaperHistoryAdapter;
import com.progrema.superbaby.holograph.PieGraph;
import com.progrema.superbaby.holograph.PieSlice;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.ActiveContext;
import com.progrema.superbaby.util.FormatUtils;
import com.progrema.superbaby.widget.customview.ObserveAbleListView;

import java.util.Calendar;

public class DiaperFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_LIST_VIEW = 0;
    private static final int LOADER_LAST_WET = 1;
    private static final int LOADER_LAST_DRY = 2;
    private static final int LOADER_LAST_MIXED = 3;
    private static final int LOADER_TODAY_WET = 4;
    private static final int LOADER_TODAY_DRY = 5;
    private static final int LOADER_TODAY_MIXED = 6;
    private ObserveAbleListView diaperHistoryList;
    private DiaperHistoryAdapter mAdapter;
    private TextView WetTotalToday;
    private TextView WetLast;
    private TextView DryTotalToday;
    private TextView DryLast;
    private TextView MixedTotalToday;
    private TextView MixedLast;
    private PieGraph HeaderPieGraph;

    public static DiaperFragment getInstance() {
        return new DiaperFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate fragment layout
        View rootView = inflater.inflate(R.layout.fragment_diaper, container, false);

        // set action bar icon and title
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setIcon(getResources().getDrawable(R.drawable.ic_baby_nappy_white));

        // get Header UI object
        WetTotalToday = (TextView) rootView.findViewById(R.id.wet_average);
        DryTotalToday = (TextView) rootView.findViewById(R.id.dry_average);
        MixedTotalToday = (TextView) rootView.findViewById(R.id.mix_average);
        WetLast = (TextView) rootView.findViewById(R.id.wet_last);
        DryLast = (TextView) rootView.findViewById(R.id.dry_last);
        MixedLast = (TextView) rootView.findViewById(R.id.mix_last);
        HeaderPieGraph = (PieGraph) rootView.findViewById(R.id.diaper_piegraph);

        // Init text value for the first time
        WetTotalToday.setText(getResources().getString(R.string.activity_today_initial));
        DryTotalToday.setText(getResources().getString(R.string.activity_today_initial));
        MixedTotalToday.setText(getResources().getString(R.string.activity_today_initial));
        WetLast.setText(getResources().getString(R.string.activity_last_initial));
        DryLast.setText(getResources().getString(R.string.activity_last_initial));
        MixedLast.setText(getResources().getString(R.string.activity_last_initial));

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
        lm.initLoader(LOADER_TODAY_WET, null, this);
        lm.initLoader(LOADER_TODAY_DRY, null, this);
        lm.initLoader(LOADER_TODAY_MIXED, null, this);

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
        Calendar today = Calendar.getInstance();
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
                return new CursorLoader(getActivity(), BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        BabyLogContract.BABY_SELECTION_ARG,
                        argumentSelectionOne,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);

            case LOADER_LAST_WET:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND type = 'WET'",
                        argumentSelectionOne,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);

            case LOADER_LAST_DRY:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND type = 'DRY'",
                        argumentSelectionOne,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);

            case LOADER_LAST_MIXED:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND type = 'MIXED'",
                        argumentSelectionOne,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);

            case LOADER_TODAY_DRY:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND type = 'DRY' AND timestamp >= ?",
                        argumentSelectionTwo,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);

            case LOADER_TODAY_WET:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND type = 'WET' AND timestamp >= ?",
                        argumentSelectionTwo,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);

            case LOADER_TODAY_MIXED:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND type = 'MIXED' AND timestamp >= ?",
                        argumentSelectionTwo,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);

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

                case LOADER_LAST_WET:
                    WetLast.setText(FormatUtils.fmtDiaperLastActivity(getActivity(),
                            cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TIMESTAMP)));
                    break;

                case LOADER_LAST_DRY:
                    DryLast.setText(FormatUtils.fmtDiaperLastActivity(getActivity(),
                            cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TIMESTAMP)));
                    break;

                case LOADER_LAST_MIXED:
                    MixedLast.setText(FormatUtils.fmtDiaperLastActivity(getActivity(),
                            cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TIMESTAMP)));
                    break;

                case LOADER_TODAY_WET:
                    WetTotalToday.setText(FormatUtils.fmtDiaperTotalToday(getActivity(),
                            String.valueOf(cursor.getCount())));
                    PieSlice WetPieSlice = new PieSlice();
                    WetPieSlice.setColor(getResources().getColor(R.color.blue));
                    WetPieSlice.setValue(cursor.getCount());
                    HeaderPieGraph.addSlice(WetPieSlice);
                    break;

                case LOADER_TODAY_DRY:
                    DryTotalToday.setText(FormatUtils.fmtDiaperTotalToday(getActivity(),
                            String.valueOf(cursor.getCount())));
                    PieSlice DryPieSlice = new PieSlice();
                    DryPieSlice.setColor(getResources().getColor(R.color.orange));
                    DryPieSlice.setValue(cursor.getCount());
                    HeaderPieGraph.addSlice(DryPieSlice);
                    break;

                case LOADER_TODAY_MIXED:
                    MixedTotalToday.setText(FormatUtils.fmtDiaperTotalToday(getActivity(),
                            String.valueOf(cursor.getCount())));
                    PieSlice MixedPieSlice = new PieSlice();
                    MixedPieSlice.setColor(getResources().getColor(R.color.purple));
                    MixedPieSlice.setValue(cursor.getCount());
                    HeaderPieGraph.addSlice(MixedPieSlice);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }

}
