package com.progrema.superbaby.ui.fragment.home;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.progrema.superbaby.R;
import com.progrema.superbaby.adapter.timelinehistory.TimeLineHistoryAdapter;
import com.progrema.superbaby.models.Baby;
import com.progrema.superbaby.models.Diaper;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.ui.activity.HomeActivity;
import com.progrema.superbaby.util.ActiveContext;
import com.progrema.superbaby.widget.customview.ObserveAbleListView;

import java.util.Calendar;

/**
 * Created by iqbalpakeh on 18/1/14.
 */
public class TimeLineLogFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>
{

    public static LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
    public static final int LOADER_ID = 3;
    private TimeLineHistoryAdapter mAdapter;
    private ObserveAbleListView historyList;

    private TextView headerBabyName;
    private TextView headerBabyAge;
    private TextView headerBabySex;
    private TextView headerLastNursing;
    private TextView headerLastSleep;
    private TextView headerLastDiaper;


    public static TimeLineLogFragment getInstance()
    {
        return new TimeLineLogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // inflate fragment layout
        View rootView = inflater.inflate(R.layout.fragment_timeline, container, false);

        // get ui object

        headerBabyName = (TextView) rootView.findViewById(R.id.timeline_header_baby_name);
        headerBabyAge = (TextView) rootView.findViewById(R.id.timeline_header_baby_age);
        headerBabySex = (TextView) rootView.findViewById(R.id.timeline_header_baby_sex);
        headerLastNursing = (TextView) rootView.findViewById(R.id.timeline_header_last_nursing);
        headerLastSleep = (TextView) rootView.findViewById(R.id.timeline_header_last_sleep);
        headerLastDiaper = (TextView) rootView.findViewById(R.id.timeline_header_last_diaper);

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
        headerBabyAge.setText(baby.getBirthdayInString()); //TODO: create getAge() function!!
        headerBabySex.setText(baby.getSex().getTitle());
        headerLastNursing.setText(queryLastNursing(String.valueOf(baby.getID())));
        headerLastSleep.setText(queryLastSleep(String.valueOf(baby.getID())));
        headerLastDiaper.setText(queryLastDiaper(String.valueOf(baby.getID())));
    }

    private String queryLastNursing(String babyId)
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
            return cursor.getString(0);
        }
        catch (Exception e)
        {
            // do nothing
        }
        return "No activity yet";
    }

    private String queryLastSleep(String babyId)
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
            return cursor.getString(0);
        }
        catch (Exception e)
        {
            // do nothing
        }
        return "No activity yet";
    }

    private String queryLastDiaper(String babyId)
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
            return cursor.getString(0);
        }
        catch (Exception e)
        {
            // do nothing
        }
        return "No activity yet";
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
