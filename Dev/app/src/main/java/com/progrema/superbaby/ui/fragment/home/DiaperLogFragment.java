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
import com.progrema.superbaby.adapter.diaperhistory.DiaperHistoryAdapter;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.ActiveContext;

/**
 * Created by iqbalpakeh on 18/1/14.
 * @author aria
 */
public class DiaperLogFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>
{

    private static DiaperLogFragment singletonDiaperLogFragment = null;
    private static LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
    private static final int LOADER_ID = 2;
    private ListView diaperHistoryList;
    private DiaperHistoryAdapter mAdapter;

    public static synchronized DiaperLogFragment getInstance()
    {
        if (singletonDiaperLogFragment == null)
        {
            singletonDiaperLogFragment = new DiaperLogFragment();
        }else{
            if (singletonDiaperLogFragment.isAdded()){
                singletonDiaperLogFragment.getLoaderManager().restartLoader(LOADER_ID,null, mCallbacks );
            }
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
        diaperHistoryList.setAdapter(mAdapter);

        // prepare loader
        mCallbacks = this;
        android.support.v4.app.LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_ID, null, mCallbacks);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle){

        String[] args = {String.valueOf(ActiveContext.getActiveBaby(getActivity()).getID())};
        CursorLoader cl = new CursorLoader(getActivity(), BabyLogContract.Diaper.CONTENT_URI,
                BabyLogContract.Diaper.Query.PROJECTION,
                BabyLogContract.BABY_SELECTION_ARG,
                args,
                BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);
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
