package com.progrema.superbaby.ui.fragment.history;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
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
import com.progrema.superbaby.adapter.diaper.DiaperAdapter;
import com.progrema.superbaby.holograph.PieGraph;
import com.progrema.superbaby.holograph.PieSlice;
import com.progrema.superbaby.models.ActivityDiaper;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.ui.fragment.dialog.DiaperDialog;
import com.progrema.superbaby.util.ActiveContext;
import com.progrema.superbaby.util.FormatUtils;
import com.progrema.superbaby.widget.customfragment.HistoryFragment;
import com.progrema.superbaby.widget.customlistview.ObserveableListView;

public class DiaperFragment extends HistoryFragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
        HistoryFragmentServices, DiaperAdapter.Callbacks, DiaperDialog.Callbacks {

    // Asynchronous cursor loader type
    private static final int LOADER_LIST_VIEW = 0;
    private static final int LOADER_LAST_WET = 1;
    private static final int LOADER_LAST_DRY = 2;
    private static final int LOADER_LAST_MIXED = 3;
    private static final int LOADER_TOTAL_WET = 4;
    private static final int LOADER_TOTAL_DRY = 5;
    private static final int LOADER_TOTAL_MIXED = 6;
    private final static int RESULT_OK = 0;

    // Entry handler
    private TextView wetTotalHandler;
    private TextView WetLastHandler;
    private TextView dryTotalHandler;
    private TextView dryLastHandler;
    private TextView mixedTotalHandler;
    private TextView mixedLastHandler;
    private PieGraph pieGraphHandler;

    // List view operation
    private ObserveableListView diaperHistoryList;
    private DiaperAdapter adapter;
    private View root;
    private View placeholder;
    private String currentEntryTag;

    public static DiaperFragment getInstance() {
        return new DiaperFragment();
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
        root = inflater.inflate(R.layout.fragment_diaper, container, false);
        placeholder = inflater.inflate(R.layout.placeholder_header, null);
        super.attachQuickReturnView(root, R.id.header_container);
        super.attachPlaceHolderLayout(placeholder, R.id.placeholder_header);
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setIcon(getResources().getDrawable(R.drawable.ic_diaper_top));
    }

    @Override
    public void prepareHandler() {
        wetTotalHandler = (TextView) root.findViewById(R.id.wet_average);
        dryTotalHandler = (TextView) root.findViewById(R.id.dry_average);
        mixedTotalHandler = (TextView) root.findViewById(R.id.mix_average);
        WetLastHandler = (TextView) root.findViewById(R.id.wet_last);
        dryLastHandler = (TextView) root.findViewById(R.id.dry_last);
        mixedLastHandler = (TextView) root.findViewById(R.id.mix_last);
        pieGraphHandler = (PieGraph) root.findViewById(R.id.diaper_piegraph);
    }

    @Override
    public void prepareListView() {
        diaperHistoryList = (ObserveableListView) root.findViewById(R.id.activity_list);
        adapter = new DiaperAdapter(getActivity(), null, 0);
        adapter.setCallbacks(this);
        diaperHistoryList.addHeaderView(placeholder);
        diaperHistoryList.addFooterView(new View(getActivity()));//TODO: do I need this API?
        diaperHistoryList.setAdapter(adapter);
        super.attachListView(diaperHistoryList);
    }

    @Override
    public void onDiaperEntryEditSelected(View entry) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        currentEntryTag = entry.getTag().toString();
        DiaperDialog diaperChoiceBox = DiaperDialog.getInstance();
        diaperChoiceBox.setCallbacks(this);
        diaperChoiceBox.show(fragmentTransaction, "diaper_dialog");
    }

    @Override
    public void onDiaperChoiceSelected(int result, Intent data) {
        if (result == RESULT_OK) {
            Bundle recordData = data.getExtras();
            String diaperType = (String) recordData.get(ActivityDiaper.DIAPER_TYPE_KEY);
            ActivityDiaper activityDiaper = new ActivityDiaper();
            activityDiaper.setActivityId(Long.valueOf(currentEntryTag));
            activityDiaper.setType(ActivityDiaper.DiaperType.valueOf(diaperType));
            activityDiaper.edit(getActivity());
        }
    }

    @Override
    public void prepareLoaderManager() {
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_LIST_VIEW, getArguments(), this);
        loaderManager.initLoader(LOADER_LAST_WET, getArguments(), this);
        loaderManager.initLoader(LOADER_LAST_DRY, getArguments(), this);
        loaderManager.initLoader(LOADER_LAST_MIXED, getArguments(), this);
        loaderManager.initLoader(LOADER_TOTAL_WET, getArguments(), this);
        loaderManager.initLoader(LOADER_TOTAL_DRY, getArguments(), this);
        loaderManager.initLoader(LOADER_TOTAL_MIXED, getArguments(), this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        String[] timeFilterArg = getTimeFilterArg(bundle);
        String[] babyIdArg = {
                String.valueOf(ActiveContext.getActiveBaby(getActivity()).getActivityId())
        };
        switch (loaderId) {
            case LOADER_LIST_VIEW:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND timestamp >= ? AND timestamp <= ?",
                        timeFilterArg,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);
            case LOADER_LAST_WET:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND type = 'WET'",
                        babyIdArg,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);
            case LOADER_LAST_DRY:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND type = 'DRY'",
                        babyIdArg,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);
            case LOADER_LAST_MIXED:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND type = 'MIXED'",
                        babyIdArg,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);
            case LOADER_TOTAL_DRY:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND type = 'DRY' AND timestamp >= ? AND timestamp <= ?",
                        timeFilterArg,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);
            case LOADER_TOTAL_WET:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        "baby_id = ? AND type = 'WET' AND timestamp >= ? AND timestamp <= ?",
                        timeFilterArg,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);
            case LOADER_TOTAL_MIXED:
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
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (cursor.getCount() >= 0) {
            cursor.moveToFirst();
            switch (cursorLoader.getId()) {
                case LOADER_LIST_VIEW:
                    adapter.swapCursor(cursor);
                    break;
                case LOADER_LAST_WET:
                    inflateLastEntry(WetLastHandler, cursor);
                    break;
                case LOADER_LAST_DRY:
                    inflateLastEntry(dryLastHandler, cursor);
                    break;
                case LOADER_LAST_MIXED:
                    inflateLastEntry(mixedLastHandler, cursor);
                    break;
                case LOADER_TOTAL_WET:
                    inflateTotalEntry(wetTotalHandler, cursor);
                    break;
                case LOADER_TOTAL_DRY:
                    inflateTotalEntry(dryTotalHandler, cursor);
                    break;
                case LOADER_TOTAL_MIXED:
                    inflateTotalEntry(mixedTotalHandler, cursor);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        adapter.swapCursor(null);
        drawPieChart();
    }

    private void inflateTotalEntry(TextView entryHandler, Cursor cursor) {
        entryHandler.setText(FormatUtils.fmtDiaperTotalToday(getActivity(), String.valueOf(cursor.getCount())));
        drawPieChart();
    }

    private void inflateLastEntry(TextView entryHandler, Cursor cursor) {
        try {
            entryHandler.setText(FormatUtils.fmtDiaperLastActivity(getActivity(),
                    cursor.getString(BabyLogContract.Diaper.Query.OFFSET_TIMESTAMP)));
        } catch (CursorIndexOutOfBoundsException exception) {
            entryHandler.setText(getResources().getString(R.string.activity_last_initial));
        }
    }

    private void drawPieChart() {
        removeSlices();
        drawWetSlice();
        drawDrySlice();
        drawMixSlice();
    }

    private void removeSlices() {
        pieGraphHandler.removeSlices();
    }

    private void drawWetSlice() {
        PieSlice WetPieSlice = new PieSlice();
        WetPieSlice.setColor(getResources().getColor(R.color.blue));
        WetPieSlice.setValue(calculateEntryNumber(wetTotalHandler));
        pieGraphHandler.addSlice(WetPieSlice);
    }

    private void drawDrySlice() {
        PieSlice DryPieSlice = new PieSlice();
        DryPieSlice.setColor(getResources().getColor(R.color.orange));
        DryPieSlice.setValue(calculateEntryNumber(dryTotalHandler));
        pieGraphHandler.addSlice(DryPieSlice);
    }

    private void drawMixSlice() {
        PieSlice MixedPieSlice = new PieSlice();
        MixedPieSlice.setColor(getResources().getColor(R.color.purple));
        MixedPieSlice.setValue(calculateEntryNumber(mixedTotalHandler));
        pieGraphHandler.addSlice(MixedPieSlice);
    }

    private int calculateEntryNumber(TextView entryHandler) {
        /**
         * We have to get the the number of entry for deletion process because cursor only
         * contain newly created entry. We can't get the current number of all entry from cursor
         * to draw the pie chart.
         */
        String entryHandlerText = entryHandler.getText().toString().replace(" times", "");
        return Integer.parseInt(entryHandlerText);
    }

}
