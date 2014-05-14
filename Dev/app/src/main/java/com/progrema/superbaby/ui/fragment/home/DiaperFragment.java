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
import com.progrema.superbaby.widget.customview.ObserveableListView;

import java.util.Calendar;

public class DiaperFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_LIST_VIEW = 0;
    private static final int LOADER_LAST_WET = 1;
    private static final int LOADER_LAST_DRY = 2;
    private static final int LOADER_LAST_MIXED = 3;
    private static final int LOADER_TODAY_WET = 4;
    private static final int LOADER_TODAY_DRY = 5;
    private static final int LOADER_TODAY_MIXED = 6;
    private ObserveableListView olv_diaperHistoryList;
    private DiaperHistoryAdapter dha_adapter;
    private TextView tv_wetTotalToday;
    private TextView tv_wetLast;
    private TextView tv_dryTotalToday;
    private TextView tv_dryLast;
    private TextView tv_mixedTotalToday;
    private TextView tv_mixedLast;
    private PieGraph pg_header;

    public static DiaperFragment getInstance() {
        return new DiaperFragment();
    }

    @Override
    public View onCreateView(LayoutInflater li_inflater, ViewGroup vg_container,
                             Bundle b_savedInstanceState) {

        // inflate fragment layout
        View v_root = li_inflater.inflate(R.layout.fragment_diaper, vg_container, false);

        // set action bar icon and title
        ActionBar ab_actionBar = getActivity().getActionBar();
        ab_actionBar.setIcon(getResources().getDrawable(R.drawable.ic_diaper_action_bar_top));

        // get Header UI object
        tv_wetTotalToday = (TextView) v_root.findViewById(R.id.wet_average);
        tv_dryTotalToday = (TextView) v_root.findViewById(R.id.dry_average);
        tv_mixedTotalToday = (TextView) v_root.findViewById(R.id.mix_average);
        tv_wetLast = (TextView) v_root.findViewById(R.id.wet_last);
        tv_dryLast = (TextView) v_root.findViewById(R.id.dry_last);
        tv_mixedLast = (TextView) v_root.findViewById(R.id.mix_last);
        pg_header = (PieGraph) v_root.findViewById(R.id.diaper_piegraph);

        // set adapter to list view
        olv_diaperHistoryList = (ObserveableListView) v_root.findViewById(R.id.activity_list);
        dha_adapter = new DiaperHistoryAdapter(getActivity(), null, 0);
        olv_diaperHistoryList.addHeaderView(new View(getActivity()));
        olv_diaperHistoryList.addFooterView(new View(getActivity()));
        olv_diaperHistoryList.setAdapter(dha_adapter);

        // prepare loader
        LoaderManager lm_loaderManager = getLoaderManager();
        lm_loaderManager.initLoader(LOADER_LIST_VIEW, null, this);
        lm_loaderManager.initLoader(LOADER_LAST_WET, null, this);
        lm_loaderManager.initLoader(LOADER_LAST_DRY, null, this);
        lm_loaderManager.initLoader(LOADER_LAST_MIXED, null, this);
        lm_loaderManager.initLoader(LOADER_TODAY_WET, null, this);
        lm_loaderManager.initLoader(LOADER_TODAY_DRY, null, this);
        lm_loaderManager.initLoader(LOADER_TODAY_MIXED, null, this);

        return v_root;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i_loaderId, Bundle b_bundle) {

        /**
         * as stated here: http://developer.android.com/reference/java/util/Calendar.html
         * 24:00:00 "belongs" to the following day.
         * That is, 23:59 on Dec 31, 1969 < 24:00 on Jan 1, 1970 < 24:01:00 on Jan 1, 1970
         * form a sequence of three consecutive minutes in time.
         */
        Calendar c_midnight = Calendar.getInstance();
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

        switch (i_loaderId) {
            case LOADER_LIST_VIEW:
                return new CursorLoader(getActivity(), BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        BabyLogContract.BABY_SELECTION_ARG,
                        sa_argumentSelectionOne,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);

            case LOADER_LAST_WET:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND type = 'WET'",
                        sa_argumentSelectionOne,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);

            case LOADER_LAST_DRY:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND type = 'DRY'",
                        sa_argumentSelectionOne,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);

            case LOADER_LAST_MIXED:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND type = 'MIXED'",
                        sa_argumentSelectionOne,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);

            case LOADER_TODAY_DRY:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND type = 'DRY' AND timestamp >= ?",
                        sa_argumentSelectionTwo,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);

            case LOADER_TODAY_WET:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND type = 'WET' AND timestamp >= ?",
                        sa_argumentSelectionTwo,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);

            case LOADER_TODAY_MIXED:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND type = 'MIXED' AND timestamp >= ?",
                        sa_argumentSelectionTwo,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> l_cursorLoader, Cursor c_cursor) {
        if (c_cursor.getCount() > 0) {
            c_cursor.moveToFirst();
            switch (l_cursorLoader.getId()) {
                case LOADER_LIST_VIEW:
                    dha_adapter.swapCursor(c_cursor);
                    break;

                case LOADER_LAST_WET:
                    tv_wetLast.setText(FormatUtils.fmtDiaperLastActivity(getActivity(),
                            c_cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TIMESTAMP)));
                    break;

                case LOADER_LAST_DRY:
                    tv_dryLast.setText(FormatUtils.fmtDiaperLastActivity(getActivity(),
                            c_cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TIMESTAMP)));
                    break;

                case LOADER_LAST_MIXED:
                    tv_mixedLast.setText(FormatUtils.fmtDiaperLastActivity(getActivity(),
                            c_cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TIMESTAMP)));
                    break;

                case LOADER_TODAY_WET:
                    tv_wetTotalToday.setText(FormatUtils.fmtDiaperTotalToday(getActivity(),
                            String.valueOf(c_cursor.getCount())));
                    PieSlice WetPieSlice = new PieSlice();
                    WetPieSlice.setColor(getResources().getColor(R.color.blue));
                    WetPieSlice.setValue(c_cursor.getCount());
                    pg_header.addSlice(WetPieSlice);
                    break;

                case LOADER_TODAY_DRY:
                    tv_dryTotalToday.setText(FormatUtils.fmtDiaperTotalToday(getActivity(),
                            String.valueOf(c_cursor.getCount())));
                    PieSlice DryPieSlice = new PieSlice();
                    DryPieSlice.setColor(getResources().getColor(R.color.orange));
                    DryPieSlice.setValue(c_cursor.getCount());
                    pg_header.addSlice(DryPieSlice);
                    break;

                case LOADER_TODAY_MIXED:
                    tv_mixedTotalToday.setText(FormatUtils.fmtDiaperTotalToday(getActivity(),
                            String.valueOf(c_cursor.getCount())));
                    PieSlice MixedPieSlice = new PieSlice();
                    MixedPieSlice.setColor(getResources().getColor(R.color.purple));
                    MixedPieSlice.setValue(c_cursor.getCount());
                    pg_header.addSlice(MixedPieSlice);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        dha_adapter.swapCursor(null);
    }

}
