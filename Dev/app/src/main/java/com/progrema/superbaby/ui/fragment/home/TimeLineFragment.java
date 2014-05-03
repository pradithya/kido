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

public class TimeLineFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_LIST_VIEW = 0;
    private static final int LOADER_LAST_NURSING = 1;
    private static final int LOADER_LAST_SLEEP = 2;
    private static final int LOADER_LAST_DIAPER = 3;
    private static final int LOADER_LAST_MEASUREMENT = 4;
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

    public static TimeLineFragment getInstance() {
        return new TimeLineFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_LIST_VIEW, null, this);
        lm.initLoader(LOADER_LAST_NURSING, null, this);
        lm.initLoader(LOADER_LAST_SLEEP, null, this);
        lm.initLoader(LOADER_LAST_DIAPER, null, this);
        lm.initLoader(LOADER_LAST_MEASUREMENT, null, this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Baby baby = ActiveContext.getActiveBaby(getActivity());
        headerBabyName.setText(baby.getName());
        headerBabyBirthday.setText(baby.getBirthdayInReadableFormat(getActivity()));
        headerBabyAge.setText(baby.getAgeInReadableFormat(getActivity()));
        headerBabySex.setText(baby.getSex().getTitle());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        String[] argumentSelection =
                {String.valueOf(ActiveContext.getActiveBaby(getActivity()).getID())};
        switch (loaderId) {
            case LOADER_LIST_VIEW: {
                return new CursorLoader(getActivity(),
                        BabyLogContract.Activity.CONTENT_URI,
                        BabyLogContract.Activity.Query.PROJECTION,
                        BabyLogContract.BABY_SELECTION_ARG,
                        argumentSelection,
                        BabyLogContract.Activity.Query.SORT_BY_TIMESTAMP_DESC);
            }
            case LOADER_LAST_NURSING: {
                String[] projection = {BabyLogContract.Nursing.TIMESTAMP};
                return new CursorLoader(getActivity(),
                        BabyLogContract.Nursing.MAX_TIMESTAMP,
                        projection,
                        BabyLogContract.BABY_SELECTION_ARG,
                        argumentSelection,
                        null);
            }
            case LOADER_LAST_SLEEP: {
                String[] projection = {BabyLogContract.Sleep.TIMESTAMP};
                return new CursorLoader(getActivity(),
                        BabyLogContract.Sleep.MAX_TIMESTAMP,
                        projection,
                        BabyLogContract.BABY_SELECTION_ARG,
                        argumentSelection,
                        null);
            }
            case LOADER_LAST_DIAPER: {
                String[] projection = {BabyLogContract.Diaper.TIMESTAMP};
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.MAX_TIMESTAMP,
                        projection,
                        BabyLogContract.BABY_SELECTION_ARG,
                        argumentSelection,
                        null);
            }
            case LOADER_LAST_MEASUREMENT: {
                String[] projection = {BabyLogContract.Measurement.TIMESTAMP};
                return new CursorLoader(getActivity(),
                        BabyLogContract.Measurement.MAX_TIMESTAMP,
                        projection,
                        BabyLogContract.BABY_SELECTION_ARG,
                        argumentSelection,
                        null);
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cl, Cursor cursor) {
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            switch (cl.getId()) {
                case LOADER_LIST_VIEW: {
                    mAdapter.swapCursor(cursor);
                    break;
                }
                case LOADER_LAST_NURSING: {
                    String timestamp = cursor.getString(0);
                    if (timestamp != null) {
                        String time = DateUtils.
                                getRelativeTimeSpanString(Long.parseLong(timestamp)).toString();
                        headerLastNursing.setText(
                                FormatUtils.formatLastActivity(getActivity(),
                                        BabyLogContract.Nursing.table, time)
                        );
                    }
                    break;
                }
                case LOADER_LAST_SLEEP: {
                    String timestamp = cursor.getString(0);
                    if (timestamp != null) {
                        String time = DateUtils.
                                getRelativeTimeSpanString(Long.parseLong(timestamp)).toString();
                        headerLastSleep.setText(
                                FormatUtils.formatLastActivity(getActivity(),
                                        BabyLogContract.Sleep.table, time)
                        );
                    }
                    break;
                }
                case LOADER_LAST_DIAPER: {
                    String timestamp = cursor.getString(0);
                    if (timestamp != null) {
                        String time = DateUtils.
                                getRelativeTimeSpanString(Long.parseLong(timestamp)).toString();
                        headerLastDiaper.setText(
                                FormatUtils.formatLastActivity(getActivity(),
                                        BabyLogContract.Diaper.table, time)
                        );
                    }
                    break;
                }
                case LOADER_LAST_MEASUREMENT: {
                    String timestamp = cursor.getString(0);
                    if (timestamp != null) {
                        String time = DateUtils.
                                getRelativeTimeSpanString(Long.parseLong(timestamp)).toString();
                        headerLastMeasurement.setText(
                                FormatUtils.formatLastActivity(getActivity(),
                                        BabyLogContract.Measurement.table, time)
                        );
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cl) {
        if (cl.getId() == LOADER_LIST_VIEW) {
            mAdapter.swapCursor(null);
        }
    }
}
