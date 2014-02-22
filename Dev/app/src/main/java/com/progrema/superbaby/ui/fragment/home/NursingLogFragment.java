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
import com.progrema.superbaby.adapter.nursinghistory.NursingHistoryAdapter;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.ActiveContext;

/**
 * Created by iqbalpakeh on 18/1/14.
 */
public class NursingLogFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static NursingLogFragment singletonNursingLogFragment = null;
    private static final int LOADER_ID = 4;
    private static LoaderManager.LoaderCallbacks mCallbacks;
    private NursingHistoryAdapter mAdapter;
    private ListView nursingHistoryList;

    public static synchronized NursingLogFragment getInstance()
    {
        if (singletonNursingLogFragment == null)
        {
            singletonNursingLogFragment = new NursingLogFragment();
        }
        else
        {
            if (singletonNursingLogFragment.isAdded())
            {
                singletonNursingLogFragment.getLoaderManager().restartLoader(LOADER_ID, null, mCallbacks);
            }
        }
        return singletonNursingLogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_nursing_log, container, false);

        // set adapter to list view
        nursingHistoryList = (ListView) rootView.findViewById(R.id.history_list_nursing);
        mAdapter = new NursingHistoryAdapter(getActivity(), null, 0);
        nursingHistoryList.setAdapter(mAdapter);

        // prepare loader
        mCallbacks = this;
        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_ID, null, mCallbacks);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundel)
    {

        String[] args = {String.valueOf(ActiveContext.getActiveBaby(getActivity()).getID())};
        CursorLoader cl = new CursorLoader(getActivity(), BabyLogContract.Nursing.CONTENT_URI,
                BabyLogContract.Nursing.Query.PROJECTION,
                BabyLogContract.BABY_SELECTION_ARG,
                args,
                BabyLogContract.Nursing.Query.SORT_BY_TIMESTAMP_DESC);
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
