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
import com.progrema.superbaby.adapter.nursinghistory.NursingHistoryAdapter;
import com.progrema.superbaby.models.Nursing;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.ActiveContext;
import com.progrema.superbaby.util.FormatUtils;
import com.progrema.superbaby.widget.customview.ObserveAbleListView;

import java.util.Calendar;

/**
 * Created by iqbalpakeh on 18/1/14.
 */
public class NursingFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final int LOADER_LIST_VIEW = 0;
    private static final int LOADER_NURSING_FROM_TIME_REFERENCE = 1;
    private static final int LOADER_NURSING_LAST_SIDE = 2;
    private Calendar now = Calendar.getInstance();
    private NursingHistoryAdapter mAdapter;
    private ObserveAbleListView nursingHistoryList;
    private TextView percentageLeft;
    private TextView percentageRight;
    private TextView lastSlide;
    private TextView rightPerDay;
    private TextView leftPerDay;
    private TextView formulaPerDay;

    public static NursingFragment getInstance()
    {
        return new NursingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_nursing, container, false);

        // get ui object
        percentageLeft = (TextView) rootView.findViewById(R.id.percentage_left);
        percentageRight = (TextView) rootView.findViewById(R.id.percentage_right);
        lastSlide = (TextView) rootView.findViewById(R.id.last_side);
        rightPerDay = (TextView) rootView.findViewById(R.id.right_total_per_day);
        leftPerDay = (TextView) rootView.findViewById(R.id.left_total_per_day);
        formulaPerDay = (TextView) rootView.findViewById(R.id.formula_total_per_day);

        // set adapter to list view
        nursingHistoryList = (ObserveAbleListView) rootView.findViewById(R.id.activity_list);
        mAdapter = new NursingHistoryAdapter(getActivity(), null, 0);
        nursingHistoryList.setAdapter(mAdapter);

        // prepare loader
        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_LIST_VIEW, null, this);
        lm.initLoader(LOADER_NURSING_FROM_TIME_REFERENCE, null, this);
        lm.initLoader(LOADER_NURSING_LAST_SIDE, null, this);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundel)
    {
        switch (loaderId)
        {
            case LOADER_LIST_VIEW:
            {
                String[] argumentSelection =
                        {String.valueOf(ActiveContext.getActiveBaby(getActivity()).getID())};
                return new CursorLoader(getActivity(),
                        BabyLogContract.Nursing.CONTENT_URI,
                        BabyLogContract.Nursing.Query.PROJECTION,
                        BabyLogContract.BABY_SELECTION_ARG,
                        argumentSelection,
                        BabyLogContract.Nursing.Query.SORT_BY_TIMESTAMP_DESC);
            }
            case LOADER_NURSING_FROM_TIME_REFERENCE:
            {
                // TODO: timeReference must be configurable based on user input
                String timeReference =
                        String.valueOf(now.getTimeInMillis() - 7 * FormatUtils.DAY_MILLIS);
                String[] argumentSelection =
                        {
                                String.valueOf(ActiveContext.getActiveBaby(getActivity()).getID()),
                                timeReference
                        };
                return new CursorLoader(getActivity(),
                        BabyLogContract.Nursing.CONTENT_URI,
                        BabyLogContract.Nursing.Query.PROJECTION,
                        "baby_id = ? AND timestamp >= ?",
                        argumentSelection,
                        BabyLogContract.Nursing.Query.SORT_BY_TIMESTAMP_DESC);
            }
            case LOADER_NURSING_LAST_SIDE:
            {
                String[] projection = {BabyLogContract.Nursing.SIDES};
                String[] argumentSelection =
                        {String.valueOf(ActiveContext.getActiveBaby(getActivity()).getID())};
                return new CursorLoader(getActivity(),
                        BabyLogContract.Nursing.LAST_SIDES,
                        projection,
                        BabyLogContract.BABY_SELECTION_ARG,
                        argumentSelection,
                        null);
            }
            default:
            {
                return null;
            }
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> cl, Cursor cursor)
    {
        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            switch (cl.getId())
            {
                case LOADER_LIST_VIEW:
                {
                    mAdapter.swapCursor(cursor);
                    break;
                }
                case LOADER_NURSING_FROM_TIME_REFERENCE:
                {
                    /**
                     * Calculate average value of nursing from both side since the last 7 days.
                     * That is, get the value from DB than calculate the average value.
                     */
                    float duration = 0, totalDuration = 0,
                            leftDuration = 0, rightDuration = 0, formulaVol = 0, result = 0;
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
                    {
                        duration = Float.valueOf(
                                cursor.getString(BabyLogContract.Nursing.Query.OFFSET_DURATION));
                        String side = cursor.getString(BabyLogContract.Nursing.Query.OFFSET_SIDES);
                        totalDuration += duration;
                        if (side.equals(Nursing.NursingType.LEFT.getTitle()))
                        {
                            leftDuration += duration;
                        }
                        else if (side.equals(Nursing.NursingType.RIGHT.getTitle()))
                        {
                            rightDuration += duration;
                        }
                        else if (side.equals(Nursing.NursingType.FORMULA.getTitle()))
                        {
                            formulaVol += Float.valueOf(
                                    cursor.getString(BabyLogContract.Nursing.Query.OFFSET_VOLUME));
                        }
                    }

                    result = leftDuration / totalDuration * 100;
                    percentageLeft.setText(
                            FormatUtils.formatNursingPercentage(getActivity(),
                                    getActivity().getResources().getString(R.string.left_side),
                                    String.valueOf(result)));

                    result = rightDuration / totalDuration * 100;
                    percentageRight.setText(
                            FormatUtils.formatNursingPercentage(getActivity(),
                                    getActivity().getResources().getString(R.string.right_side),
                                    String.valueOf(result)));

                    // calculate total left side
                    result = leftDuration / FormatUtils.MINUTE_MILLIS; // convert to minutes
                    result = result / 7; // meantime from one week (7 days)
                    // TODO: timeReference must be configurable based on user input
                    leftPerDay.setText(
                            FormatUtils.formatNursingPerDay(getActivity(),
                                    getActivity().getResources().getString(R.string.left_side),
                                    String.valueOf(result), "m/d"));

                    // calculate total right side
                    result = rightDuration / FormatUtils.MINUTE_MILLIS; // convert to minutes
                    result = result / 7; // meantime from one week (7 days)
                    // TODO: timeReference must be configurable based on user input
                    rightPerDay.setText(
                            FormatUtils.formatNursingPerDay(getActivity(),
                                    getActivity().getResources().getString(R.string.right_side),
                                    String.valueOf(result), "m/d"));

                    // calculate total formula
                    result = formulaVol / 7; // meantime from one week (7 days)
                    // TODO: timeReference must be configurable based on user input
                    formulaPerDay.setText(
                            FormatUtils.formatNursingPerDay(getActivity(),
                                    getActivity().getResources().getString(R.string.formula),
                                    String.valueOf(result), "mL/d"));

                    break;
                }
                case LOADER_NURSING_LAST_SIDE:
                {
                    String sides = cursor.getString(0);
                    if (sides != null)
                    {
                        lastSlide.setText(FormatUtils.formatNursingLastSide(getActivity(), sides));
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cl)
    {
        mAdapter.swapCursor(null);
    }
}
