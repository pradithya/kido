package com.progrema.superbaby.ui.fragment.history;

import android.app.ActionBar;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.progrema.superbaby.R;
import com.progrema.superbaby.adapter.timeline.TimelineAdapter;
import com.progrema.superbaby.models.Baby;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.util.ActiveContext;
import com.progrema.superbaby.widget.customfragment.HistoryFragment;
import com.progrema.superbaby.widget.customlistview.ObserveableListView;

public class TimelineFragment extends HistoryFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /*
     * Loader type used for asynchronous cursor loading
     */
    private static final int LOADER_LIST_VIEW = 0;

    /*
     * View object for header information
     */
    private TextView tvName;
    private TextView tvBirthday;
    private TextView tvAge;
    private TextView tvSex;

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

        // prepare adapter
        olvTimelineList = (ObserveableListView) vRoot.findViewById(R.id.activity_list);
        taAdapter = new TimelineAdapter(getActivity(), null, 0);
        olvTimelineList.addHeaderView(vPlaceholderRoot);
        olvTimelineList.setAdapter(taAdapter);
        super.attachListView(olvTimelineList);

        // prepare loader
        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_LIST_VIEW, null, this);

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

        String[] aBabyIdArg = {String.valueOf(ActiveContext.getActiveBaby(getActivity()).getActivityId())};
        switch (loaderId) {
            case LOADER_LIST_VIEW:
                return new CursorLoader(getActivity(),
                        BabyLogContract.Activity.CONTENT_URI,
                        BabyLogContract.Activity.Query.PROJECTION,
                        BabyLogContract.BABY_SELECTION_ARG,
                        aBabyIdArg,
                        BabyLogContract.Activity.Query.SORT_BY_TIMESTAMP_DESC);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cl, Cursor cursor) {
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            switch (cl.getId()) {
                case LOADER_LIST_VIEW:
                    taAdapter.swapCursor(cursor);
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
