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
import com.progrema.superbaby.adapter.sleephistory.DiaperHistoryAdapter;
import com.progrema.superbaby.provider.BabyLogContract;

/**
 * Created by iqbalpakeh on 18/1/14.
 * @author aria
 */
public class DiaperLogFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>
{

    private static DiaperLogFragment singletonDiaperLogFragment = null;
    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
    private static final int LOADER_ID = 2;
    private ListView diaperHistoryList;
    private DiaperHistoryAdapter mAdapter;

    public static synchronized DiaperLogFragment getInstance()
    {
        if (singletonDiaperLogFragment == null)
        {
            singletonDiaperLogFragment = new DiaperLogFragment();
        }
        return singletonDiaperLogFragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_diaper_log, container, false);

        // set adapter to list view
        diaperHistoryList = (ListView) rootView.findViewById(R.id.history_list_diaper);
        mAdapter = new DiaperHistoryAdapter(getActivity(), null, 0);
        mAdapter.setLayout(R.layout.history_item_diaper);
        diaperHistoryList.setAdapter(mAdapter);

        // prepare loader
        mCallbacks = this;
        android.support.v4.app.LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_ID, null, mCallbacks);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle){

        CursorLoader cl = new CursorLoader(getActivity(), BabyLogContract.Diaper.CONTENT_URI,
                BabyLogContract.Diaper.Query.PROJECTION,
                null,
                null,
                BabyLogContract.Diaper._ID);
        return cl;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor){
        if (cursor.getCount() > 0)
        {
            /** show last inserted row */
            cursor.moveToFirst();
            mAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader){
        mAdapter.swapCursor(null);
    }
}
