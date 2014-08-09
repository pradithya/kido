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
import android.widget.ImageView;
import android.widget.TextView;

import com.progrema.superbaby.R;
import com.progrema.superbaby.adapter.nursing.NursingAdapter;
import com.progrema.superbaby.holograph.PieGraph;
import com.progrema.superbaby.holograph.PieSlice;
import com.progrema.superbaby.models.Nursing;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.ActiveContext;
import com.progrema.superbaby.util.FormatUtils;
import com.progrema.superbaby.widget.customfragment.HistoryFragment;
import com.progrema.superbaby.widget.customlistview.ObserveableListView;

import java.text.DecimalFormat;

public class NursingFragment extends HistoryFragment
        implements LoaderManager.LoaderCallbacks<Cursor>, HistoryFragmentServices {

    // Asynchronous cursor loader type
    private static final int LOADER_LIST_VIEW = 0;
    private static final int LOADER_GENERAL_ENTRY = 1;
    private static final int LOADER_LAST_SIDE_ENTRY = 2;

    // Entry handler
    private TextView leftPercentHandler;
    private TextView rightPercentHandler;
    private TextView rightDurationHandler;
    private TextView leftDurationHandler;
    private TextView formulaDurationHandler;
    private TextView lastSideHandler;
    private ImageView lastSideImageHandler;
    private PieGraph pieGraphHandler;

    // List view operation
    private NursingAdapter adapter;
    private ObserveableListView nursingHistoryList;
    private View root;
    private View placeholder;

    public static NursingFragment getInstance() {
        return new NursingFragment();
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
        root = inflater.inflate(R.layout.fragment_nursing, container, false);
        placeholder = inflater.inflate(R.layout.placeholder_header, null);
        super.attachQuickReturnView(root, R.id.header_container);
        super.attachPlaceHolderLayout(placeholder, R.id.placeholder_header);
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setIcon(getResources().getDrawable(R.drawable.ic_nursing_top));
    }

    @Override
    public void prepareHandler() {
        leftPercentHandler = (TextView) root.findViewById(R.id.percent_left);
        rightPercentHandler = (TextView) root.findViewById(R.id.percent_right);
        rightDurationHandler = (TextView) root.findViewById(R.id.right_duration);
        leftDurationHandler = (TextView) root.findViewById(R.id.left_duration);
        formulaDurationHandler = (TextView) root.findViewById(R.id.formula_volume);
        lastSideHandler = (TextView) root.findViewById(R.id.last_side_title);
        lastSideImageHandler = (ImageView) root.findViewById(R.id.last_side);
        pieGraphHandler = (PieGraph) root.findViewById(R.id.nursing_left_right_pie_chart);
    }

    @Override
    public void prepareListView() {
        nursingHistoryList = (ObserveableListView) root.findViewById(R.id.activity_list);
        adapter = new NursingAdapter(getActivity(), null, 0);
        nursingHistoryList.addHeaderView(placeholder);
        nursingHistoryList.setAdapter(adapter);
        super.attachListView(nursingHistoryList);
    }

    @Override
    public void prepareLoaderManager() {
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_LIST_VIEW, getArguments(), this);
        loaderManager.initLoader(LOADER_GENERAL_ENTRY, getArguments(), this);
        loaderManager.initLoader(LOADER_LAST_SIDE_ENTRY, getArguments(), this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        String[] timeFilterArg = getTimeFilterArg(bundle);
        String[] babyIdArg = {
                String.valueOf(ActiveContext.getActiveBaby(getActivity()).getActivityId())
        };
        String[] lastSideProjection = {
                BabyLogContract.Nursing.SIDES
        };
        switch (loaderId) {
            case LOADER_LIST_VIEW:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Nursing.CONTENT_URI,
                        BabyLogContract.Nursing.Query.PROJECTION,
                        "baby_id = ? AND timestamp >= ? AND timestamp <= ?",
                        timeFilterArg,
                        BabyLogContract.Nursing.Query.SORT_BY_TIMESTAMP_DESC);
            case LOADER_GENERAL_ENTRY:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Nursing.CONTENT_URI,
                        BabyLogContract.Nursing.Query.PROJECTION,
                        "baby_id = ? AND timestamp >= ? AND timestamp <= ?",
                        timeFilterArg,
                        BabyLogContract.Nursing.Query.SORT_BY_TIMESTAMP_DESC);
            case LOADER_LAST_SIDE_ENTRY:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Nursing.LAST_SIDES,
                        lastSideProjection,
                        BabyLogContract.BABY_SELECTION_ARG,
                        babyIdArg,
                        null);
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
                case LOADER_LAST_SIDE_ENTRY:
                    inflateLastSideEntry(cursor);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        adapter.swapCursor(null);
    }

    private void inflateGeneralEntry(Cursor cursor) {
        NursingFragmentEntry entry = new NursingFragmentEntry();
        entry.prepareEntry(cursor);
        inflateLeftPercentageEntry(entry);
        inflateRightPercentageEntry(entry);
        inflateLeftDurationEntry(entry);
        inflateRightDurationEntry(entry);
        inflateFormulaVolumeEntry(entry);
        inflatePieChart(entry);
    }

    private void inflateLastSideEntry(Cursor cursor) {
        if (isLeftSideLast(cursor)) {
            lastSideHandler.setTextColor(getResources().getColor(R.color.green));
            lastSideImageHandler.setImageDrawable(getResources().getDrawable(R.drawable.ic_nursing_right));
        } else if (isRightSideLast(cursor)) {
            lastSideHandler.setTextColor(getResources().getColor(R.color.orange));
            lastSideImageHandler.setImageDrawable(getResources().getDrawable(R.drawable.ic_nursing_left));
        }
    }

    private boolean isLeftSideLast(Cursor cursor) {
        return (cursor.getString(0).compareTo(Nursing.NursingType.RIGHT.getTitle()) == 0);
    }

    private boolean isRightSideLast(Cursor cursor) {
        return (cursor.getString(0).compareTo(Nursing.NursingType.LEFT.getTitle()) == 0);
    }

    private void inflateLeftPercentageEntry(NursingFragmentEntry data) {
        leftPercentHandler.setText(
                FormatUtils.fmtNursingPct(getActivity(), data.getLeftPercentage()));
    }

    private void inflateRightPercentageEntry(NursingFragmentEntry data) {
        rightPercentHandler.setText(
                FormatUtils.fmtNursingPct(getActivity(), data.getRightPercentage()));
    }

    private void inflateLeftDurationEntry(NursingFragmentEntry data) {
        leftDurationHandler.setText(
                FormatUtils.fmtNursingDrt(getActivity(), String.valueOf(data.getLeftDuration())));
    }

    private void inflateRightDurationEntry(NursingFragmentEntry data) {
        rightDurationHandler.setText(
                FormatUtils.fmtNursingDrt(getActivity(), String.valueOf(data.getRightDuration())));
    }

    private void inflatePieChart(NursingFragmentEntry entry) {
        removeSlices();
        drawLeftSlice(entry);
        drawRightSlice(entry);
    }

    private void removeSlices() {
        pieGraphHandler.removeSlices();
    }

    private void drawLeftSlice(NursingFragmentEntry data) {
        PieSlice leftSlice = new PieSlice();
        leftSlice.setColor(getResources().getColor(R.color.orange));
        leftSlice.setValue(data.getLeftDuration());
        pieGraphHandler.addSlice(leftSlice);
    }

    private void drawRightSlice(NursingFragmentEntry data) {
        PieSlice rightSlice = new PieSlice();
        rightSlice.setColor(getResources().getColor(R.color.green));
        rightSlice.setValue(data.getRightDuration());
        pieGraphHandler.addSlice(rightSlice);
    }

    private void inflateFormulaVolumeEntry(NursingFragmentEntry data) {
        formulaDurationHandler.setText(
                FormatUtils.fmtVolumeToday(getActivity(), String.valueOf(data.getFormulaVolume())));
    }

    private class NursingFragmentEntry {

        private DecimalFormat decimalConverter = new DecimalFormat("0.00");
        private long duration;
        private long totalDuration;
        private long leftDuration;
        private long rightDuration;
        private long formulaVolume;
        private String side;

        public void prepareEntry(Cursor cursor) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                side = cursor.getString(BabyLogContract.Nursing.Query.OFFSET_SIDES);
                duration = Long.valueOf(cursor.getString(BabyLogContract.Nursing.Query.OFFSET_DURATION));
                totalDuration += duration;
                if (isSideLeft()) {
                    leftDuration += duration;
                } else if (side.equals(Nursing.NursingType.RIGHT.getTitle())) {
                    rightDuration += duration;
                } else if (side.equals(Nursing.NursingType.FORMULA.getTitle())) {
                    formulaVolume += Float.valueOf(cursor.getString(BabyLogContract.Nursing.Query.OFFSET_VOLUME));
                }
            }
        }

        public String getLeftPercentage() {
            return decimalConverter.format((float) leftDuration / (float) totalDuration * 100);
        }

        public String getRightPercentage() {
            return decimalConverter.format((float) rightDuration / (float) totalDuration * 100);
        }

        public long getLeftDuration() {
            return leftDuration;
        }

        public long getRightDuration() {
            return rightDuration;
        }

        public long getFormulaVolume() {
            return formulaVolume;
        }

        private boolean isSideLeft() {
            return this.side.equals(Nursing.NursingType.LEFT.getTitle());
        }
    }

}
