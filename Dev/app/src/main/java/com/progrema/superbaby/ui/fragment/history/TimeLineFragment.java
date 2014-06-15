package com.progrema.superbaby.ui.fragment.history;

import android.app.ActionBar;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.progrema.superbaby.R;
import com.progrema.superbaby.adapter.timeline.TimelineAdapter;
import com.progrema.superbaby.models.Baby;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.ActiveContext;
import com.progrema.superbaby.util.FormatUtils;
import com.progrema.superbaby.widget.customfragment.HistoryFragment;
import com.progrema.superbaby.widget.customlistview.ObserveableListView;

public class TimelineFragment extends HistoryFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /*
     * Loader type used for asynchronous cursor loading
     */
    private static final int LOADER_LIST_VIEW = 0;
    private static final int LOADER_LAST_NURSING = 1;
    private static final int LOADER_LAST_SLEEP = 2;
    private static final int LOADER_LAST_DIAPER = 3;
    private static final int LOADER_LAST_MEASUREMENT = 4;

    /*
     * View object for header information
     */
    private TextView tvName;
    private TextView tvBirthday;
    private TextView tvAge;
    private TextView tvSex;
    private TextView tvNursing;
    private TextView tvSleep;
    private TextView tvDiaper;
    private TextView tvMeasurement;

    /*
     * List and adapter to manage list view
     */
    private TimelineAdapter taAdapter;
    private ObserveableListView olvTimelineList;

    public static TimelineFragment getInstance() {
        return new TimelineFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate fragment layout
        View vRoot = inflater.inflate(R.layout.fragment_timeline, container, false);
        View vPlaceholderRoot = inflater.inflate(R.layout.placeholder_header, null);
        super.attachQuickReturnView(vRoot, R.id.header_container);
        super.attachPlaceHolderLayout(vPlaceholderRoot, R.id.placeholder_header);

        // set action bar icon and title
        ActionBar abActionBar = getActivity().getActionBar();
        abActionBar.setIcon(getResources().getDrawable(R.drawable.ic_timeline_top));

        // get ui object
        tvName = (TextView) vRoot.findViewById(R.id.name_content);
        tvBirthday = (TextView) vRoot.findViewById(R.id.birthday_content);
        tvAge = (TextView) vRoot.findViewById(R.id.age_content);
        tvSex = (TextView) vRoot.findViewById(R.id.sex_content);
        tvNursing = (TextView) vRoot.findViewById(R.id.nursing_content);
        tvSleep = (TextView) vRoot.findViewById(R.id.sleep_content);
        tvDiaper = (TextView) vRoot.findViewById(R.id.diaper_content);
        tvMeasurement = (TextView) vRoot.findViewById(R.id.measurement_content);

        // prepare adapter
        olvTimelineList = (ObserveableListView) vRoot.findViewById(R.id.activity_list);
        taAdapter = new TimelineAdapter(getActivity(), null, 0);
        olvTimelineList.addHeaderView(vPlaceholderRoot);
        olvTimelineList.setAdapter(taAdapter);
        super.attachListView(olvTimelineList);

        // prepare loader
        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_LIST_VIEW, null, this);
        lm.initLoader(LOADER_LAST_NURSING, null, this);
        lm.initLoader(LOADER_LAST_SLEEP, null, this);
        lm.initLoader(LOADER_LAST_DIAPER, null, this);
        lm.initLoader(LOADER_LAST_MEASUREMENT, null, this);

        return vRoot;
    }

    @Override
    public void onResume() {
        super.onResume();
        Baby baby = ActiveContext.getActiveBaby(getActivity());
        tvName.setText(baby.getName());
        tvBirthday.setText(baby.getBirthdayInReadableFormat(getActivity()));
        tvAge.setText(baby.getAgeInReadableFormat(getActivity()));
        tvSex.setText(baby.getSex().getTitle());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        String[] aBabyIdArg = {String.valueOf(ActiveContext.getActiveBaby(getActivity()).getID())};
        String[] aProjectionNursing = {BabyLogContract.Nursing.TIMESTAMP};
        String[] aProjectionSleep = {BabyLogContract.Sleep.TIMESTAMP};
        String[] aProjectionDiaper = {BabyLogContract.Diaper.TIMESTAMP};
        String[] aProjectionMeasurement = {BabyLogContract.Measurement.TIMESTAMP};

        switch (loaderId) {
            case LOADER_LIST_VIEW:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Activity.CONTENT_URI,
                        BabyLogContract.Activity.Query.PROJECTION,
                        BabyLogContract.BABY_SELECTION_ARG,
                        aBabyIdArg,
                        BabyLogContract.Activity.Query.SORT_BY_TIMESTAMP_DESC);

            case LOADER_LAST_NURSING:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Nursing.MAX_TIMESTAMP,
                        aProjectionNursing,
                        BabyLogContract.BABY_SELECTION_ARG,
                        aBabyIdArg,
                        null);

            case LOADER_LAST_SLEEP:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Sleep.MAX_TIMESTAMP,
                        aProjectionSleep,
                        BabyLogContract.BABY_SELECTION_ARG,
                        aBabyIdArg,
                        null);

            case LOADER_LAST_DIAPER:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Diaper.MAX_TIMESTAMP,
                        aProjectionDiaper,
                        BabyLogContract.BABY_SELECTION_ARG,
                        aBabyIdArg,
                        null);

            case LOADER_LAST_MEASUREMENT:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Measurement.MAX_TIMESTAMP,
                        aProjectionMeasurement,
                        BabyLogContract.BABY_SELECTION_ARG,
                        aBabyIdArg,
                        null);

            default:
                return null;

        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cl, Cursor cursor) {
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String timestamp = cursor.getString(0);
            switch (cl.getId()) {
                case LOADER_LIST_VIEW:
                    taAdapter.swapCursor(cursor);
                    break;

                case LOADER_LAST_NURSING:
                    if (timestamp != null) {
                        String time = DateUtils.
                                getRelativeTimeSpanString(Long.parseLong(timestamp)).toString();
                        tvNursing.setText(FormatUtils.fmtLastActivity(getActivity(), time)
                        );
                    }
                    break;

                case LOADER_LAST_SLEEP:
                    if (timestamp != null) {
                        String time = DateUtils.
                                getRelativeTimeSpanString(Long.parseLong(timestamp)).toString();
                        tvSleep.setText(FormatUtils.fmtLastActivity(getActivity(), time)
                        );
                    }
                    break;

                case LOADER_LAST_DIAPER:
                    if (timestamp != null) {
                        String time = DateUtils.
                                getRelativeTimeSpanString(Long.parseLong(timestamp)).toString();
                        tvDiaper.setText(FormatUtils.fmtLastActivity(getActivity(), time)
                        );
                    }
                    break;

                case LOADER_LAST_MEASUREMENT:
                    if (timestamp != null) {
                        String time = DateUtils.
                                getRelativeTimeSpanString(Long.parseLong(timestamp)).toString();
                        tvMeasurement.setText(FormatUtils.fmtLastActivity(getActivity(), time)
                        );
                    }
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cl) {
        if (cl.getId() == LOADER_LIST_VIEW) {
            taAdapter.swapCursor(null);
        }
    }
}
