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
import com.progrema.superbaby.widget.customview.ObserveableListView;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class NursingFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_LIST_VIEW = 0;
    private static final int LOADER_TODAY_ENTRY = 1;
    private static final int LOADER_LAST_SIDE = 2;
    private NursingAdapter naAdapter;
    private ObserveableListView olvNursingHistoryList;
    private TextView tvLeftPct;
    private TextView tvRightPct;
    private TextView tvRightToday;
    private TextView tvLeftToday;
    private TextView tvFormulaToday;
    private ImageView ivLastSide;
    private PieGraph pgLeftRight;

    public static NursingFragment getInstance() {
        return new NursingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vRoot = inflater.inflate(R.layout.fragment_nursing, container, false);

        // set action bar icon and title
        ActionBar ab_actionBar = getActivity().getActionBar();
        ab_actionBar.setIcon(getResources().getDrawable(R.drawable.ic_nursing_top));

        // get ui object
        tvLeftPct = (TextView) vRoot.findViewById(R.id.percentage_left);
        tvRightPct = (TextView) vRoot.findViewById(R.id.percentage_right);
        tvRightToday = (TextView) vRoot.findViewById(R.id.right_today);
        tvLeftToday = (TextView) vRoot.findViewById(R.id.left_today);
        tvFormulaToday = (TextView) vRoot.findViewById(R.id.formula_today);
        ivLastSide = (ImageView) vRoot.findViewById(R.id.last_side);
        pgLeftRight = (PieGraph) vRoot.findViewById(R.id.nursing_left_right_pie_chart);

        // set adapter to list view
        olvNursingHistoryList = (ObserveableListView) vRoot.findViewById(R.id.activity_list);
        naAdapter = new NursingAdapter(getActivity(), null, 0);
        olvNursingHistoryList.addHeaderView(new View(getActivity()));
        olvNursingHistoryList.addFooterView(new View(getActivity()));
        olvNursingHistoryList.setAdapter(naAdapter);

        // prepare loader
        LoaderManager lmLoaderManager = getLoaderManager();
        lmLoaderManager.initLoader(LOADER_LIST_VIEW, null, this);
        lmLoaderManager.initLoader(LOADER_TODAY_ENTRY, null, this);
        lmLoaderManager.initLoader(LOADER_LAST_SIDE, null, this);
        return vRoot;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int iLoaderId, Bundle bBundle) {

        /**
         * as stated here: http://developer.android.com/reference/java/util/Calendar.html
         * 24:00:00 "belongs" to the following day.
         * That is, 23:59 on Dec 31, 1969 < 24:00 on Jan 1, 1970 < 24:01:00 on Jan 1, 1970
         * form a sequence of three consecutive minutes in time.
         */
        Calendar cMidnight = Calendar.getInstance();
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

        String[] saLastSideProjection = {
                BabyLogContract.Nursing.SIDES
        };

        switch (iLoaderId) {
            case LOADER_LIST_VIEW:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Nursing.CONTENT_URI,
                        BabyLogContract.Nursing.Query.PROJECTION,
                        BabyLogContract.BABY_SELECTION_ARG,
                        saArgumentSelectionOne,
                        BabyLogContract.Nursing.Query.SORT_BY_TIMESTAMP_DESC);

            case LOADER_TODAY_ENTRY:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Nursing.CONTENT_URI,
                        BabyLogContract.Nursing.Query.PROJECTION,
                        "baby_id = ? AND timestamp >= ?",
                        saArgumentSelectionTwo,
                        BabyLogContract.Nursing.Query.SORT_BY_TIMESTAMP_DESC);

            case LOADER_LAST_SIDE:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Nursing.LAST_SIDES,
                        saLastSideProjection,
                        BabyLogContract.BABY_SELECTION_ARG,
                        saArgumentSelectionOne,
                        null);

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> lCursorLoader, Cursor cCursor) {
        if (cCursor.getCount() > 0) {
            cCursor.moveToFirst();
            switch (lCursorLoader.getId()) {
                case LOADER_LIST_VIEW:
                    naAdapter.swapCursor(cCursor);
                    break;

                case LOADER_TODAY_ENTRY:

                    long lDuration;
                    long lTotalDuration = 0;
                    long lLeftDuration = 0;
                    long lRightDuration = 0;
                    long lFormulaVolume = 0;
                    float fResult;

                    for (cCursor.moveToFirst(); !cCursor.isAfterLast(); cCursor.moveToNext()) {
                        lDuration = Long.valueOf(
                                cCursor.getString(BabyLogContract.Nursing.Query.OFFSET_DURATION));
                        String s_side = cCursor.getString(BabyLogContract.Nursing.Query.OFFSET_SIDES);
                        lTotalDuration += lDuration;
                        if (s_side.equals(Nursing.NursingType.LEFT.getTitle())) {
                            lLeftDuration += lDuration;
                        } else if (s_side.equals(Nursing.NursingType.RIGHT.getTitle())) {
                            lRightDuration += lDuration;
                        } else if (s_side.equals(Nursing.NursingType.FORMULA.getTitle())) {
                            lFormulaVolume += Float.valueOf(
                                    cCursor.getString(BabyLogContract.Nursing.Query.OFFSET_VOLUME));
                        }
                    }

                    // left percentage information
                    fResult = lLeftDuration / lTotalDuration * 100;
                    tvLeftPct.setText(
                            FormatUtils.fmtNursingPct(getActivity(),
                                    getActivity().getResources().getString(R.string.left_side),
                                    String.valueOf(fResult))
                    );

                    // right percentage information
                    fResult = lRightDuration / lTotalDuration * 100;
                    tvRightPct.setText(
                            FormatUtils.fmtNursingPct(getActivity(),
                                    getActivity().getResources().getString(R.string.right_side),
                                    String.valueOf(fResult))
                    );

                    // left side duration information
                    fResult = TimeUnit.MILLISECONDS.toMinutes(lLeftDuration);
                    tvLeftToday.setText(
                            FormatUtils.fmtNursingToday(getActivity(),
                                    getActivity().getResources().getString(R.string.left_side),
                                    String.valueOf(fResult), "m")
                    );
                    PieSlice psLeft = new PieSlice();
                    psLeft.setColor(getResources().getColor(R.color.green));
                    psLeft.setValue(lLeftDuration);
                    pgLeftRight.addSlice(psLeft);

                    // right side duration information
                    fResult = TimeUnit.MILLISECONDS.toMinutes(lRightDuration);
                    tvRightToday.setText(
                            FormatUtils.fmtNursingToday(getActivity(),
                                    getActivity().getResources().getString(R.string.right_side),
                                    String.valueOf(fResult), "m")
                    );
                    PieSlice psRight = new PieSlice();
                    psRight.setColor(getResources().getColor(R.color.orange));
                    psRight.setValue(lRightDuration);
                    pgLeftRight.addSlice(psRight);

                    // formula volume information
                    tvFormulaToday.setText(
                            FormatUtils.fmtNursingToday(getActivity(),
                                    getActivity().getResources().getString(R.string.formula),
                                    String.valueOf(lFormulaVolume), "mL")
                    );
                    break;

                case LOADER_LAST_SIDE:
                    String sType = cCursor.getString(0);
                    if (sType.compareTo(Nursing.NursingType.RIGHT.getTitle()) == 0) {
                        ivLastSide.setImageDrawable(getResources()
                                .getDrawable(R.drawable.ic_nursing_right));
                    } else if(sType.compareTo(Nursing.NursingType.LEFT.getTitle()) == 0) {
                        ivLastSide.setImageDrawable(getResources()
                                .getDrawable(R.drawable.ic_nursing_left));
                    }
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cl) {
        naAdapter.swapCursor(null);
    }
}
