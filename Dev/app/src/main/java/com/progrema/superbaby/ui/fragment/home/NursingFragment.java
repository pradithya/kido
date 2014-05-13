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
import com.progrema.superbaby.adapter.nursinghistory.NursingHistoryAdapter;
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
    private NursingHistoryAdapter nha_adapter;
    private ObserveableListView olv_nursingHistoryList;
    private TextView tv_leftPct;
    private TextView tv_rightPct;
    private TextView tv_lastSide;
    private TextView tv_rightToday;
    private TextView tv_leftToday;
    private TextView tv_formulaToday;

    public static NursingFragment getInstance() {
        return new NursingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v_root = inflater.inflate(R.layout.fragment_nursing, container, false);

        // set action bar icon and title
        ActionBar ab_actionBar = getActivity().getActionBar();
        ab_actionBar.setIcon(getResources().getDrawable(R.drawable.ic_nursing_action_bar_top));

        // get ui object
        tv_leftPct = (TextView) v_root.findViewById(R.id.percentage_left);
        tv_rightPct = (TextView) v_root.findViewById(R.id.percentage_right);
        tv_lastSide = (TextView) v_root.findViewById(R.id.last_side);
        tv_rightToday = (TextView) v_root.findViewById(R.id.right_today);
        tv_leftToday = (TextView) v_root.findViewById(R.id.left_today);
        tv_formulaToday = (TextView) v_root.findViewById(R.id.formula_today);

        // set adapter to list view
        olv_nursingHistoryList = (ObserveableListView) v_root.findViewById(R.id.activity_list);
        nha_adapter = new NursingHistoryAdapter(getActivity(), null, 0);
        olv_nursingHistoryList.addHeaderView(new View(getActivity()));
        olv_nursingHistoryList.addFooterView(new View(getActivity()));
        olv_nursingHistoryList.setAdapter(nha_adapter);

        // prepare loader
        LoaderManager lm_loaderManager = getLoaderManager();
        lm_loaderManager.initLoader(LOADER_LIST_VIEW, null, this);
        lm_loaderManager.initLoader(LOADER_TODAY_ENTRY, null, this);
        lm_loaderManager.initLoader(LOADER_LAST_SIDE, null, this);
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

        String[] sa_lastSideProjection = {
                BabyLogContract.Nursing.SIDES
        };

        switch (i_loaderId) {
            case LOADER_LIST_VIEW:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Nursing.CONTENT_URI,
                        BabyLogContract.Nursing.Query.PROJECTION,
                        BabyLogContract.BABY_SELECTION_ARG,
                        sa_argumentSelectionOne,
                        BabyLogContract.Nursing.Query.SORT_BY_TIMESTAMP_DESC);

            case LOADER_TODAY_ENTRY:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Nursing.CONTENT_URI,
                        BabyLogContract.Nursing.Query.PROJECTION,
                        "baby_id = ? AND timestamp >= ?",
                        sa_argumentSelectionTwo,
                        BabyLogContract.Nursing.Query.SORT_BY_TIMESTAMP_DESC);

            case LOADER_LAST_SIDE:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Nursing.LAST_SIDES,
                        sa_lastSideProjection,
                        BabyLogContract.BABY_SELECTION_ARG,
                        sa_argumentSelectionOne,
                        null);

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
                    nha_adapter.swapCursor(c_cursor);
                    break;

                case LOADER_TODAY_ENTRY:

                    long l_duration;
                    long l_totalDuration = 0;
                    long l_leftDuration = 0;
                    long l_rightDuration = 0;
                    long l_formulaVolume = 0;
                    float f_result;

                    for (c_cursor.moveToFirst(); !c_cursor.isAfterLast(); c_cursor.moveToNext()) {
                        l_duration = Long.valueOf(
                                c_cursor.getString(BabyLogContract.Nursing.Query.OFFSET_DURATION));
                        String s_side = c_cursor.getString(BabyLogContract.Nursing.Query.OFFSET_SIDES);
                        l_totalDuration += l_duration;
                        if (s_side.equals(Nursing.NursingType.LEFT.getTitle())) {
                            l_leftDuration += l_duration;
                        } else if (s_side.equals(Nursing.NursingType.RIGHT.getTitle())) {
                            l_rightDuration += l_duration;
                        } else if (s_side.equals(Nursing.NursingType.FORMULA.getTitle())) {
                            l_formulaVolume += Float.valueOf(
                                    c_cursor.getString(BabyLogContract.Nursing.Query.OFFSET_VOLUME));
                        }
                    }

                    // left percentage information
                    f_result = l_leftDuration / l_totalDuration * 100;
                    tv_leftPct.setText(
                            FormatUtils.fmtNursingPct(getActivity(),
                                    getActivity().getResources().getString(R.string.left_side),
                                    String.valueOf(f_result))
                    );

                    // right percentage information
                    f_result = l_rightDuration / l_totalDuration * 100;
                    tv_rightPct.setText(
                            FormatUtils.fmtNursingPct(getActivity(),
                                    getActivity().getResources().getString(R.string.right_side),
                                    String.valueOf(f_result))
                    );

                    // left side duration information
                    f_result = TimeUnit.MILLISECONDS.toMinutes(l_leftDuration);
                    tv_leftToday.setText(
                            FormatUtils.fmtNursingToday(getActivity(),
                                    getActivity().getResources().getString(R.string.left_side),
                                    String.valueOf(f_result), "m")
                    );

                    // right side duration information
                    f_result = TimeUnit.MILLISECONDS.toMinutes(l_rightDuration);
                    tv_rightToday.setText(
                            FormatUtils.fmtNursingToday(getActivity(),
                                    getActivity().getResources().getString(R.string.right_side),
                                    String.valueOf(f_result), "m")
                    );

                    // formula volume information
                    tv_formulaToday.setText(
                            FormatUtils.fmtNursingToday(getActivity(),
                                    getActivity().getResources().getString(R.string.formula),
                                    String.valueOf(l_formulaVolume), "mL")
                    );
                    break;

                case LOADER_LAST_SIDE:
                    String s_sides = c_cursor.getString(0);
                    tv_lastSide.setText(FormatUtils.fmtNursingLastSide(getActivity(), s_sides));
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cl) {
        nha_adapter.swapCursor(null);
    }
}
