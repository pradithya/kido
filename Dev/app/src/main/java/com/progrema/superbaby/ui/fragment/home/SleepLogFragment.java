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
import android.widget.ListView;

import com.progrema.superbaby.R;
import com.progrema.superbaby.adapter.sleephistory.SleepHistoryAdapter;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.ActiveContext;

/**
 * Fragment to log all sleep activity
 */
public class SleepLogFragment extends Fragment
        implements  LoaderManager.LoaderCallbacks<Cursor>
{

    private static SleepLogFragment singletonSleepLogFragment = null;
    private ListView sleepHistoryList;
    private SleepHistoryAdapter mAdapter;
    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
    private static final int LOADER_ID = 1;

    public static synchronized SleepLogFragment getInstance()
    {
        if (singletonSleepLogFragment == null)
        {
            singletonSleepLogFragment = new SleepLogFragment();
        }
        return singletonSleepLogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // inflate fragment layout
        View rootView = inflater.inflate(R.layout.fragment_sleep_log, container, false);

        // set adapter to list view
        sleepHistoryList = (ListView) rootView.findViewById(R.id.sleep_activity_list);
        mAdapter = new SleepHistoryAdapter(getActivity(), null, 0);
        sleepHistoryList.setAdapter(mAdapter);

        // prepare loader
        mCallbacks = this;
        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_ID, null, mCallbacks);

        return rootView;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
        String[] args = {String.valueOf(ActiveContext.getActiveBaby(getActivity()).getID())};
        CursorLoader cl = new CursorLoader(getActivity(),
                BabyLogContract.Sleep.CONTENT_URI,
                BabyLogContract.Sleep.Query.PROJECTION,
                BabyLogContract.BABY_SELECTION_ARG,
                args,
                BabyLogContract.Sleep._ID);
        return cl;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor)
    {
        if (cursor.getCount() > 0)
        {
            /** show last inserted row */
            cursor.moveToFirst();
            mAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader)
    {
        mAdapter.swapCursor(null);
    }
}
