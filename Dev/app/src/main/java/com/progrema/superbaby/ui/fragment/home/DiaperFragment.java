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
import com.progrema.superbaby.adapter.diaperhistory.DiaperHistoryAdapter;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.ActiveContext;
import com.progrema.superbaby.widget.customview.ObserveAbleListView;

/**
 * Created by iqbalpakeh on 18/1/14.
 *
 * @author aria
 */
public class DiaperFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_DIAPER_LIST_VIEW = 0;
    private static final int LOADER_DIAPER_FROM_TIME_REFERENCE = 1;
    private ObserveAbleListView diaperHistoryList;
    private DiaperHistoryAdapter mAdapter;
    private TextView WetAverage;
    private TextView WetLast;
    private TextView DryAverage;
    private TextView DryLast;
    private TextView MixAverage;
    private TextView MixLast;

    public static DiaperFragment getInstance() {
        return new DiaperFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate fragment layout
        View rootView = inflater.inflate(R.layout.fragment_diaper, container, false);

        // get Header UI object
        WetAverage = (TextView) rootView.findViewById(R.id.wet_average);
        DryAverage = (TextView) rootView.findViewById(R.id.dry_average);
        MixAverage = (TextView) rootView.findViewById(R.id.mix_average);
        WetLast = (TextView) rootView.findViewById(R.id.wet_last);
        DryLast = (TextView) rootView.findViewById(R.id.dry_last);
        MixLast = (TextView) rootView.findViewById(R.id.mix_last);

        // set adapter to list view
        diaperHistoryList = (ObserveAbleListView) rootView.findViewById(R.id.activity_list);
        mAdapter = new DiaperHistoryAdapter(getActivity(), null, 0);
        diaperHistoryList.setAdapter(mAdapter);

        // prepare loader
        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_DIAPER_LIST_VIEW, null, this);
        lm.initLoader(LOADER_DIAPER_FROM_TIME_REFERENCE, null, this);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        switch (loaderId){

            case LOADER_DIAPER_LIST_VIEW:
                String[] args = {String.valueOf(ActiveContext.getActiveBaby(getActivity()).getID())};
                CursorLoader cl = new CursorLoader(getActivity(), BabyLogContract.Diaper.CONTENT_URI,
                        BabyLogContract.Diaper.Query.PROJECTION,
                        BabyLogContract.BABY_SELECTION_ARG,
                        args,
                        BabyLogContract.Diaper.Query.SORT_BY_TIMESTAMP_DESC);
                return cl;

            case LOADER_DIAPER_FROM_TIME_REFERENCE:
                // TODO: add loader creation
                return null;

            default:
                return null;

        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

        if (cursor.getCount() > 0) {
            //show last inserted row
            cursor.moveToFirst();

            switch (cursorLoader.getId()) {
                case LOADER_DIAPER_LIST_VIEW:
                    mAdapter.swapCursor(cursor);
                    break;

                case LOADER_DIAPER_FROM_TIME_REFERENCE:
                    // TODO: add loader finished handling
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }
}
