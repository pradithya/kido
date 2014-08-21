package com.progrema.superbaby.ui.fragment.history;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.progrema.superbaby.R;
import com.progrema.superbaby.adapter.timeline.TimelineAdapter;
import com.progrema.superbaby.models.ActivityDiaper;
import com.progrema.superbaby.models.ActivityMeasurement;
import com.progrema.superbaby.models.Baby;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.ui.activity.HomeActivity;
import com.progrema.superbaby.ui.fragment.dialog.DiaperDialog;
import com.progrema.superbaby.ui.fragment.dialog.MeasurementDialog;
import com.progrema.superbaby.ui.fragment.dialog.NursingDialog;
import com.progrema.superbaby.util.ActiveContext;
import com.progrema.superbaby.widget.customfragment.HistoryFragment;
import com.progrema.superbaby.widget.customlistview.ObserveableListView;

public class TimelineFragment extends HistoryFragment
        implements LoaderManager.LoaderCallbacks<Cursor>, HistoryFragmentServices,
        TimelineAdapter.Callbacks, DiaperDialog.Callbacks, NursingDialog.Callbacks ,
        MeasurementDialog.Callbacks{

    private static final int LOADER_LIST_VIEW = 0;
    private static final int RESULT_OK = 0;
    private TextView nameHandler;
    private TextView birthdayHandler;
    private TextView ageHandler;
    private TextView sexHandler;
    private TimelineAdapter adapter;
    private ObserveableListView timelineHistoryList;
    private View root;
    private View placeholder;
    private String activityId;

    public static TimelineFragment getInstance() {
        return new TimelineFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        prepareFragment(inflater, container);
        prepareHandler();
        prepareListView();
        prepareLoaderManager();
        return root;
    }

    @Override
    public void prepareFragment(LayoutInflater inflater, ViewGroup container) {
        root = inflater.inflate(R.layout.fragment_timeline, container, false);
        placeholder = inflater.inflate(R.layout.placeholder_header, null);
        super.attachQuickReturnView(root, R.id.header_container);
        super.attachPlaceHolderLayout(placeholder, R.id.placeholder_header);
        ActionBar abActionBar = getActivity().getActionBar();
        abActionBar.setIcon(getResources().getDrawable(R.drawable.ic_timeline_top));
    }

    @Override
    public void prepareHandler() {
        nameHandler = (TextView) root.findViewById(R.id.name_content);
        birthdayHandler = (TextView) root.findViewById(R.id.birthday_content);
        ageHandler = (TextView) root.findViewById(R.id.age_content);
        sexHandler = (TextView) root.findViewById(R.id.sex_content);
    }

    @Override
    public void prepareListView() {
        timelineHistoryList = (ObserveableListView) root.findViewById(R.id.activity_list);
        adapter = new TimelineAdapter(getActivity(), null, 0);
        adapter.setCallbacks(this);
        timelineHistoryList.addHeaderView(placeholder);
        timelineHistoryList.setAdapter(adapter);
        super.attachListView(timelineHistoryList);
    }

    @Override
    public void onTimelineEntryEditSelected(View entry) {
        Cursor cursor = (Cursor) entry.getTag();
        String activityId = cursor.getString(BabyLogContract.Activity.Query.OFFSET_ID);
        String type = cursor.getString(BabyLogContract.Activity.Query.OFFSET_ACTIVITY_TYPE);
        if (type.equals(BabyLogContract.Activity.TYPE_SLEEP)) {
            updateSleepEntry(activityId);
        } else if (type.equals(BabyLogContract.Activity.TYPE_DIAPER)) {
            updateDiaperEntry(activityId);
        } else if (type.equals(BabyLogContract.Activity.TYPE_NURSING)) {
            updateNursingEntry(activityId);
        } else if (type.equals(BabyLogContract.Activity.TYPE_MEASUREMENT)) {
            updateMeasurementEntry(activityId);
        }
    }

    private void updateSleepEntry(String id) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(HomeActivity.ACTIVITY_TRIGGER_KEY, HomeActivity.Trigger.SLEEP.getTitle());
        bundle.putString(HomeActivity.ACTIVITY_EDIT_KEY, getResources().getString(R.string.menu_edit));
        bundle.putString(HomeActivity.ACTIVITY_ENTRY_TAG_KEY, id);
        StopwatchFragment frStopWatch = StopwatchFragment.getInstance();
        frStopWatch.setArguments(bundle);
        fragmentTransaction.replace(R.id.home_activity_container, frStopWatch);
        fragmentTransaction.commit();
    }

    private void updateDiaperEntry(String id) {
        activityId = id;
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        DiaperDialog diaperChoiceBox = DiaperDialog.getInstance();
        diaperChoiceBox.setCallbacks(this);
        diaperChoiceBox.show(fragmentTransaction, "diaper_dialog");
    }

    @Override
    public void onDiaperChoiceSelected(int result, Intent data) {
        if (result == RESULT_OK) {
            Bundle recordData = data.getExtras();
            String diaperType = (String) recordData.get(ActivityDiaper.DIAPER_TYPE_KEY);
            ActivityDiaper activityDiaper = new ActivityDiaper();
            activityDiaper.setActivityId(Long.valueOf(activityId));
            activityDiaper.setType(ActivityDiaper.DiaperType.valueOf(diaperType));
            activityDiaper.edit(getActivity());
        }
    }

    private void updateNursingEntry(String id) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        activityId = id;
        NursingDialog nursingChoiceBox = NursingDialog.getInstance();
        nursingChoiceBox.setCallbacks(this);
        nursingChoiceBox.show(fragmentTransaction, "nursing_dialog");
    }

    @Override
    public void onNursingChoiceSelected(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            bundle.putString(HomeActivity.ACTIVITY_TRIGGER_KEY, HomeActivity.Trigger.NURSING.getTitle());
            bundle.putString(HomeActivity.ACTIVITY_EDIT_KEY, getResources().getString(R.string.menu_edit));
            bundle.putString(HomeActivity.ACTIVITY_ENTRY_TAG_KEY, activityId);
            //TODO: for update operation, we can't use stopwatch. Use simpler UI method instead.
            StopwatchFragment stopwatchFragment = StopwatchFragment.getInstance();
            stopwatchFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.home_activity_container, stopwatchFragment);
            fragmentTransaction.commit();
        }
    }

    private void updateMeasurementEntry(String id) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        activityId = id;
        MeasurementDialog measurementChoiceBox = MeasurementDialog.getInstance();
        measurementChoiceBox.setCallbacks(this);
        measurementChoiceBox.show(fragmentTransaction, "measurement_dialog");
    }

    @Override
    public void onMeasurementChoiceSelected(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle recordData = data.getExtras();
            ActivityMeasurement activityMeasurement = new ActivityMeasurement();
            activityMeasurement.setActivityId(Long.valueOf(activityId));
            activityMeasurement.setHeight(Float.valueOf(recordData.getString(ActivityMeasurement.HEIGHT_KEY)));
            activityMeasurement.setWeight(Float.valueOf(recordData.getString(ActivityMeasurement.WEIGHT_KEY)));
            activityMeasurement.edit(getActivity());
        }
    }

    @Override
    public void prepareLoaderManager() {
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_LIST_VIEW, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Baby baby = ActiveContext.getActiveBaby(getActivity());
        nameHandler.setText(baby.getName());
        birthdayHandler.setText(baby.getBirthdayInReadableFormat(getActivity()));
        ageHandler.setText(baby.getAgeInReadableFormat(getActivity()));
        sexHandler.setText(baby.getSex().getTitle());
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
                    adapter.swapCursor(cursor);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cl) {
        if (cl.getId() == LOADER_LIST_VIEW) {
            adapter.swapCursor(null);
        }
    }
}
