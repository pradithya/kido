package com.progrema.superbaby.ui.fragment.history;

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

import com.progrema.superbaby.R;
import com.progrema.superbaby.adapter.measurement.MeasurementAdapter;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.ui.activity.HomeActivity;
import com.progrema.superbaby.util.ActiveContext;
import com.progrema.superbaby.widget.customlistview.ObserveableListView;

import java.util.Calendar;

public class MeasurementFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_LIST_VIEW = 0;
    private ObserveableListView olvMeasurementHistoryList;
    private MeasurementAdapter maAdapter;

    public static MeasurementFragment getInstance() {
        return new MeasurementFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate fragment layout
        View vRoot = inflater.inflate(R.layout.fragment_measurement, container, false);

        // set action bar icon and title
        ActionBar abActionBar = getActivity().getActionBar();
        abActionBar.setIcon(getResources().getDrawable(R.drawable.ic_measurement_top));

        // set adapter to list view
        olvMeasurementHistoryList = (ObserveableListView) vRoot.findViewById(R.id.activity_list);
        maAdapter = new MeasurementAdapter(getActivity(), null, 0);
        olvMeasurementHistoryList.setAdapter(maAdapter);

        // prepare loader
        LoaderManager lmLoaderManager = getLoaderManager();
        lmLoaderManager.initLoader(LOADER_LIST_VIEW, getArguments(), this);
        return vRoot;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bBundle) {

        String sStart;
        String sEnd;

        if (bBundle != null) {
            sStart = bBundle.getString(HomeActivity.TimeFilter.START.getTitle());
            sEnd = bBundle.getString(HomeActivity.TimeFilter.END.getTitle());

        } else {
            Calendar cStart = Calendar.getInstance();
            sEnd = String.valueOf(cStart.getTimeInMillis()); //now, for now

            cStart.set(Calendar.HOUR_OF_DAY, 0);
            cStart.set(Calendar.MINUTE, 0);
            cStart.set(Calendar.SECOND, 0);
            cStart.set(Calendar.MILLISECOND, 0);
            sStart = String.valueOf(cStart.getTimeInMillis());
        }

        String[] aTimeFilterArg = {
                String.valueOf(ActiveContext.getActiveBaby(getActivity()).getID()),
                sStart,
                sEnd
        };

        return new CursorLoader(getActivity(), BabyLogContract.Measurement.CONTENT_URI,
                BabyLogContract.Measurement.Query.PROJECTION,
                "baby_id = ? AND timestamp >= ? AND timestamp <= ?",
                aTimeFilterArg,
                BabyLogContract.Measurement.Query.SORT_BY_TIMESTAMP_DESC);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            maAdapter.swapCursor(cursor);
        } 
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        maAdapter.swapCursor(null);
    }
}
