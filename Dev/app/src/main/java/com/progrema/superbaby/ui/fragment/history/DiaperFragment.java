package com.progrema.superbaby.ui.fragment.history;

import android.app.ActionBar;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.progrema.superbaby.R;
import com.progrema.superbaby.adapter.diaper.DiaperAdapter;
import com.progrema.superbaby.holograph.PieGraph;
import com.progrema.superbaby.holograph.PieSlice;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.ActiveContext;
import com.progrema.superbaby.util.FormatUtils;
import com.progrema.superbaby.widget.customfragment.HistoryFragment;
import com.progrema.superbaby.widget.customlistview.ObserveableListView;

public class DiaperFragment extends HistoryFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    // Loader Type used for asynchronous cursor loading
    private static final int LOADER_LIST_VIEW = 0;
    private static final int LOADER_LAST_WET = 1;
    private static final int LOADER_LAST_DRY = 2;
    private static final int LOADER_LAST_MIXED = 3;
    private static final int LOADER_TODAY_WET = 4;
    private static final int LOADER_TODAY_DRY = 5;
    private static final int LOADER_TODAY_MIXED = 6;

    //View Object for header information
    private TextView tvWetTotalToday;
    private TextView tvWetLast;
    private TextView tvDryTotalToday;
    private TextView tvDryLast;
    private TextView tvMixedTotalToday;
    private TextView tvMixedLast;
    private PieGraph pgHeader;

    //List and adapter to manage list view
    private ObserveableListView olvDiaperHistoryList;
    private DiaperAdapter daAdapter;

    public static DiaperFragment getInstance() {
        return new DiaperFragment();
    }


    @Override
    public View onCreateView(LayoutInflater liInflater, ViewGroup vgContainer,
                             Bundle bSavedInstanceState) {

        // inflate fragment layout
        View vRoot = liInflater.inflate(R.layout.fragment_diaper, vgContainer, false);
        View vPlaceholderRoot = liInflater.inflate(R.layout.placeholder_header, null);

        // set action bar icon and title
        ActionBar abActionBar = getActivity().getActionBar();
        abActionBar.setIcon(getResources().getDrawable(R.drawable.ic_diaper_top));

        super.attachQuickReturnView(vRoot, R.id.header_container);
        super.attachPlaceHolderLayout(vPlaceholderRoot, R.id.placeholder_header);

        // get Header UI object
        tvWetTotalToday = (TextView) vRoot.findViewById(R.id.wet_average);
        tvDryTotalToday = (TextView) vRoot.findViewById(R.id.dry_average);
        tvMixedTotalToday = (TextView) vRoot.findViewById(R.id.mix_average);
        tvWetLast = (TextView) vRoot.findViewById(R.id.wet_last);
        tvDryLast = (TextView) vRoot.findViewById(R.id.dry_last);
        tvMixedLast = (TextView) vRoot.findViewById(R.id.mix_last);
        pgHeader = (PieGraph) vRoot.findViewById(R.id.diaper_piegraph);

        // set adapter to list view
        olvDiaperHistoryList = (ObserveableListView) vRoot.findViewById(R.id.activity_list);
        daAdapter = new DiaperAdapter(getActivity(), null, 0);
        olvDiaperHistoryList.addHeaderView(vPlaceholderRoot);
        olvDiaperHistoryList.addFooterView(new View(getActivity()));
        olvDiaperHistoryList.setAdapter(daAdapter);
        super.attachListView(olvDiaperHistoryList);

        // prepare loader
        LoaderManager lmLoaderManager = getLoaderManager();
        lmLoaderManager.initLoader(LOADER_LIST_VIEW, getArguments(), this);
        lmLoaderManager.initLoader(LOADER_LAST_WET, getArguments(), this);
        lmLoaderManager.initLoader(LOADER_LAST_DRY, getArguments(), this);
        lmLoaderManager.initLoader(LOADER_LAST_MIXED, getArguments(), this);
        lmLoaderManager.initLoader(LOADER_TODAY_WET, getArguments(), this);
        lmLoaderManager.initLoader(LOADER_TODAY_DRY, getArguments(), this);
        lmLoaderManager.initLoader(LOADER_TODAY_MIXED, getArguments(), this);

        return vRoot;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int iLoaderId, Bundle bBundle) {

        String[] babyIDArg = {
                String.valueOf(ActiveContext.getActiveBaby(getActivity()).getActivityId())
        };

        String[] timeFilterArg = getTimeFilterArg(bBundle);

        switch (iLoaderId) {
            case LOADER_LIST_VIEW:
                return new CursorLoader(getActivity(), BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND timestamp >= ? AND timestamp <= ?",
                        timeFilterArg,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);

            case LOADER_LAST_WET:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND type = 'WET'",
                        babyIDArg,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);

            case LOADER_LAST_DRY:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND type = 'DRY'",
                        babyIDArg,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);

            case LOADER_LAST_MIXED:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND type = 'MIXED'",
                        babyIDArg,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);

            case LOADER_TODAY_DRY:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND type = 'DRY' AND timestamp >= ? AND timestamp <= ?",
                        timeFilterArg,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);

            case LOADER_TODAY_WET:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND type = 'WET' AND timestamp >= ? AND timestamp <= ?",
                        timeFilterArg,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);

            case LOADER_TODAY_MIXED:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND type = 'MIXED' AND timestamp >= ? AND timestamp <= ?",
                        timeFilterArg,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> lCursorLoader, Cursor cCursor) {
        if (cCursor.getCount() >= 0) {
            cCursor.moveToFirst();
            switch (lCursorLoader.getId()) {
                case LOADER_LIST_VIEW:
                    daAdapter.swapCursor(cCursor);
                    break;

                case LOADER_LAST_WET:
                    try {
                        tvWetLast.setText(FormatUtils.fmtDiaperLastActivity(getActivity(),
                                cCursor.getString(BabyLogContract.Diaper.Query.OFFSET_TIMESTAMP)));
                    } catch (CursorIndexOutOfBoundsException exception) {
                        tvWetLast.setText(getResources().getString(R.string.activity_last_initial));
                    }
                    break;

                case LOADER_LAST_DRY:
                    try {
                        tvDryLast.setText(FormatUtils.fmtDiaperLastActivity(getActivity(),
                                cCursor.getString(BabyLogContract.Diaper.Query.OFFSET_TIMESTAMP)));
                    } catch (CursorIndexOutOfBoundsException exception) {
                        tvDryLast.setText(getResources().getString(R.string.activity_last_initial));
                    }
                    break;

                case LOADER_LAST_MIXED:
                    try {
                        tvMixedLast.setText(FormatUtils.fmtDiaperLastActivity(getActivity(),
                                cCursor.getString(BabyLogContract.Diaper.Query.OFFSET_TIMESTAMP)));
                    } catch (CursorIndexOutOfBoundsException exception) {
                        tvMixedLast.setText(getResources().getString(R.string.activity_last_initial));
                    }
                    break;

                case LOADER_TODAY_WET:
                    tvWetTotalToday.setText(FormatUtils.fmtDiaperTotalToday(getActivity(),
                            String.valueOf(cCursor.getCount())));
                    drawPieChart();
                    break;

                case LOADER_TODAY_DRY:
                    tvDryTotalToday.setText(FormatUtils.fmtDiaperTotalToday(getActivity(),
                            String.valueOf(cCursor.getCount())));
                    drawPieChart();
                    break;

                case LOADER_TODAY_MIXED:
                    tvMixedTotalToday.setText(FormatUtils.fmtDiaperTotalToday(getActivity(),
                            String.valueOf(cCursor.getCount())));
                    drawPieChart();
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        daAdapter.swapCursor(null);
        drawPieChart();
    }

    private void drawPieChart() {

        String wetString = tvWetTotalToday.getText().toString().replace(" times", "");
        int wetNumber = Integer.parseInt(wetString);

        String dryString = tvDryTotalToday.getText().toString().replace(" times", "");
        int dryNumber = Integer.parseInt(dryString);

        String mixString = tvMixedTotalToday.getText().toString().replace(" times", "");
        int mixNumber = Integer.parseInt(mixString);

        pgHeader.removeSlices();

        PieSlice WetPieSlice = new PieSlice();
        WetPieSlice.setColor(getResources().getColor(R.color.blue));
        WetPieSlice.setValue(wetNumber);
        pgHeader.addSlice(WetPieSlice);

        PieSlice DryPieSlice = new PieSlice();
        DryPieSlice.setColor(getResources().getColor(R.color.orange));
        DryPieSlice.setValue(dryNumber);
        pgHeader.addSlice(DryPieSlice);

        PieSlice MixedPieSlice = new PieSlice();
        MixedPieSlice.setColor(getResources().getColor(R.color.purple));
        MixedPieSlice.setValue(mixNumber);
        pgHeader.addSlice(MixedPieSlice);

    }

}
