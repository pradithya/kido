package com.progrema.superbaby.ui.fragment.home;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.progrema.superbaby.R;
import com.progrema.superbaby.adapter.timelinehistory.TimeLineHistoryAdapter;
import com.progrema.superbaby.models.Baby;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.ActiveContext;
import com.progrema.superbaby.util.FormatUtils;
import com.progrema.superbaby.widget.customview.ObserveAbleListView;

public class TimeLineFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    public static LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
    public static final int LOADER_ID = 3;
    private TimeLineHistoryAdapter mAdapter;
    private ObserveAbleListView historyList;
    private TextView headerBabyName;
    private TextView headerBabyBirthday;
    private TextView headerBabyAge;
    private TextView headerBabySex;
    private TextView headerLastNursing;
    private TextView headerLastSleep;
    private TextView headerLastDiaper;
    private TextView headerLastMeasurement;

    public static TimeLineFragment getInstance()
    {
        return new TimeLineFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // inflate fragment layout
        View rootView = inflater.inflate(R.layout.fragment_timeline, container, false);

        // get ui object
        headerBabyName = (TextView) rootView.findViewById(R.id.baby_name);
        headerBabyBirthday = (TextView) rootView.findViewById(R.id.baby_birthday);
        headerBabyAge = (TextView) rootView.findViewById(R.id.baby_age);
        headerBabySex = (TextView) rootView.findViewById(R.id.baby_sex);
        headerLastNursing = (TextView) rootView.findViewById(R.id.last_nursing);
        headerLastSleep = (TextView) rootView.findViewById(R.id.last_sleep);
        headerLastDiaper = (TextView) rootView.findViewById(R.id.last_diaper);
        headerLastMeasurement = (TextView) rootView.findViewById(R.id.last_measurement);

        // prepare adapter
        historyList = (ObserveAbleListView) rootView.findViewById(R.id.activity_list);
        mAdapter = new TimeLineHistoryAdapter(getActivity(), null, 0);
        historyList.setAdapter(mAdapter);

        // prepare loader
        mCallbacks = this;
        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_ID, null, mCallbacks);

        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Baby baby = ActiveContext.getActiveBaby(getActivity());
        headerBabyName.setText(baby.getName());
        headerBabyBirthday.setText(baby.getBirthdayInReadableFormat(getActivity()));
        headerBabyAge.setText(baby.getAgeInReadableFormat(getActivity()));
        headerBabySex.setText(baby.getSex().getTitle());
        headerLastNursing.setText(lastNursing(String.valueOf(baby.getID())));
        headerLastSleep.setText(lastSleep(String.valueOf(baby.getID())));
        headerLastDiaper.setText(lastDiaper(String.valueOf(baby.getID())));
        headerLastMeasurement.setText(lastMeasurement(String.valueOf(baby.getID())));
    }

    private String lastNursing(String babyId)
    {
        String[] selectionArgument = {babyId};
        String[] projection = {BabyLogContract.Nursing.TIMESTAMP};
        try
        {
            Cursor cursor = getActivity().getContentResolver().query(
                    BabyLogContract.Nursing.MAX_TIMESTAMP,
                    projection,
                    null,
                    selectionArgument,
                    null);
            cursor.moveToFirst();
            String time = DateUtils.getRelativeTimeSpanString(Long.parseLong(cursor.getString(0))).toString();
            return FormatUtils.formatLastActivity(getActivity(), BabyLogContract.Nursing.table, time);
        }
        catch (Exception e)
        {
            // do nothing
        }
        return FormatUtils.formatLastActivity(getActivity(),
                BabyLogContract.Nursing.table, getResources().getString(R.string.no_activity));
    }

    private String lastSleep(String babyId)
    {
        String[] selectionArgument = {babyId};
        String[] projection = {BabyLogContract.Sleep.TIMESTAMP};
        try
        {
            Cursor cursor = getActivity().getContentResolver().query(
                    BabyLogContract.Sleep.MAX_TIMESTAMP,
                    projection,
                    null,
                    selectionArgument,
                    null);
            cursor.moveToFirst();
            String time = DateUtils.getRelativeTimeSpanString(Long.parseLong(cursor.getString(0))).toString();
            return FormatUtils.formatLastActivity(getActivity(), BabyLogContract.Sleep.table, time);
        }
        catch (Exception e)
        {
            // do nothing
        }
        return FormatUtils.formatLastActivity(getActivity(),
                BabyLogContract.Sleep.table, getResources().getString(R.string.no_activity));
    }

    private String lastDiaper(String babyId)
    {
        String[] selectionArgument = {babyId};
        String[] projection = {BabyLogContract.Diaper.TIMESTAMP};
        try
        {
            Cursor cursor = getActivity().getContentResolver().query(
                    BabyLogContract.Diaper.MAX_TIMESTAMP,
                    projection,
                    null,
                    selectionArgument,
                    null);
            cursor.moveToFirst();
            String time = DateUtils.getRelativeTimeSpanString(Long.parseLong(cursor.getString(0))).toString();
            return FormatUtils.formatLastActivity(getActivity(), BabyLogContract.Diaper.table, time);
        }
        catch (Exception e)
        {
            // do nothing
        }
        return FormatUtils.formatLastActivity(getActivity(),
                BabyLogContract.Diaper.table, getResources().getString(R.string.no_activity));
    }

    private String lastMeasurement(String babyId)
    {
        String[] selectionArgument = {babyId};
        String[] projection = {BabyLogContract.Measurement.TIMESTAMP};
        try
        {
            Cursor cursor = getActivity().getContentResolver().query(
                    BabyLogContract.Measurement.MAX_TIMESTAMP,
                    projection,
                    null,
                    selectionArgument,
                    null);
            cursor.moveToFirst();
            String time = DateUtils.getRelativeTimeSpanString(Long.parseLong(cursor.getString(0))).toString();
            return FormatUtils.formatLastActivity(getActivity(), BabyLogContract.Measurement.table, time);
        }
        catch (Exception e)
        {
            // do nothing
        }
        return FormatUtils.formatLastActivity(getActivity(),
                BabyLogContract.Measurement.table, getResources().getString(R.string.no_activity));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
        String[] args = {String.valueOf(ActiveContext.getActiveBaby(getActivity()).getID())};
        CursorLoader cl = new CursorLoader(getActivity(), BabyLogContract.Activity.CONTENT_URI,
                BabyLogContract.Activity.Query.PROJECTION,
                BabyLogContract.BABY_SELECTION_ARG,
                args,
                BabyLogContract.Activity.Query.SORT_BY_TIMESTAMP_DESC);
        return cl;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cl, Cursor cursor)
    {
        if (cursor.getCount() > 0)
        {
            // show last inserted row
            cursor.moveToFirst();
            mAdapter.swapCursor(cursor);
        }
        else
        {
            mAdapter.swapCursor(null);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cl)
    {
        mAdapter.swapCursor(null);

    }
}
