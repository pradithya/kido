package com.progrema.superbaby.ui.fragment.home;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import com.progrema.superbaby.R;
import com.progrema.superbaby.adapter.SleepHistoryCursorAdapter;
import com.progrema.superbaby.provider.BabyLogContract;

/**
 * Fragment to log all sleep activity
 *
 */
public class SleepLogFragment extends Fragment
        implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>
{

    private static SleepLogFragment singletonSleepLogFragment = null;
    private Button startButton;
    private ListView sleepHistoryList;
    private SleepHistoryCursorAdapter mAdapter;
    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
    private static final int LOADER_ID = 1;

    private interface SleepQuery
    {
        String[] PROJECTION  =
                {
                        BaseColumns._ID,
                        BabyLogContract.Sleep.ACTIVITY_ID,
                        BabyLogContract.Sleep.BABY_ID,
                        BabyLogContract.Sleep.TIMESTAMP,
                        BabyLogContract.Sleep.DURATION
                };
    }

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

        // set onClickListener to button
        startButton = (Button)rootView.findViewById(R.id.button_start);
        startButton.setOnClickListener(this);

        // register content observer and set adapter to list view
        sleepHistoryList = (ListView) rootView.findViewById(R.id.sleep_activity_list);
        mAdapter = new SleepHistoryCursorAdapter(getActivity(), null, 0);
        mAdapter.setLayout(R.layout.sleep_history_item);
        sleepHistoryList.setAdapter(mAdapter);

        // prepare loader
        mCallbacks = this;
        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_ID, null, mCallbacks);

        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case  R.id.button_start:
                handleStartButton();
                break;
        }
    }

    private void handleStartButton()
    {
        // jump to sleep input fragment
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_activity_container, SleepInputFragment.getInstance());
        fragmentTransaction.commit();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
        CursorLoader cl = new CursorLoader(getActivity(),
                                           BabyLogContract.Sleep.CONTENT_URI,
                                           SleepQuery.PROJECTION,
                                           null,
                                           null,
                                           BabyLogContract.Sleep._ID);
        return cl;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor)
    {
        if ( cursor.getCount() > 0)
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
