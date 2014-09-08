package com.progrema.superbaby.ui.fragment.history;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.progrema.superbaby.R;
import com.progrema.superbaby.models.ActivityNursing;
import com.progrema.superbaby.models.ActivitySleep;
import com.progrema.superbaby.ui.activity.HomeActivity;
import com.progrema.superbaby.util.ActiveContext;
import com.progrema.superbaby.widget.stopwatch.Stopwatch;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class StopwatchFragment extends Fragment implements View.OnClickListener {

    private Stopwatch firstStopwatchHandler;
    private Stopwatch secondStopwatchHandler;
    private Stopwatch activeStopWatch;
    private Stopwatch inActiveStopWatch;
    private TextView titleHandler;
    private ImageButton startHandler;
    private ImageButton pauseHandler;
    private ImageButton resetHandler;
    private ImageButton doneHandler;
    private ImageButton switchHandler;
    private LinearLayout stopwatchTwoContainer;
    private Calendar startTime;
    private String sourceTrigger;
    private String editTrigger;
    private String currentEntryTag;
    private String nursingType;
    private String formulaVolume;
    private View root;
    private boolean isTwoStopWatch;
    private long firstStopwatchDuration;
    private long secondStopwatchDuration;

    public static StopwatchFragment getInstance() {
        return new StopwatchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        intentCheck();
        prepareActionBar();
        prepareHandler(inflater, container);
        setOnClickListenerToHandler();
        prepareStopwatch();
        return root;
    }

    private void prepareHandler(LayoutInflater inflater, ViewGroup container) {
        root = inflater.inflate(R.layout.fragment_stopwatch, container, false);
        startHandler = (ImageButton) root.findViewById(R.id.button_stopwatch_start);
        pauseHandler = (ImageButton) root.findViewById(R.id.button_stopwatch_pause);
        resetHandler = (ImageButton) root.findViewById(R.id.button_stopwatch_reset);
        doneHandler = (ImageButton) root.findViewById(R.id.button_stopwatch_done);
        stopwatchTwoContainer = (LinearLayout) root.findViewById(R.id.stopwatch_two_container);
        switchHandler = (ImageButton) root.findViewById(R.id.button_stopwatch_switch);
        titleHandler = (TextView) root.findViewById(R.id.stopwatch_title);
        firstStopwatchHandler = (Stopwatch) root.findViewById(R.id.chronometer_widget_a);
        secondStopwatchHandler = (Stopwatch) root.findViewById(R.id.chronometer_widget_b);
    }

    private void prepareStopwatch() {
        //set stop watch name
        if(isStopwatchForNursing()) titleHandler.setText(getString(R.string.nursing_stopwatch));
        else if (isStopwatchForSleeping()) titleHandler.setText(getString(R.string.sleep_stopwatch));
        //set active stopwatch
        if (isTwoStopWatch) activateTwoStopwatch();
        else activateOneStopwatch();
        startTime = Calendar.getInstance();
        activeStopWatch.start();
    }

    private void activateTwoStopwatch() {
        stopwatchTwoContainer.setVisibility(View.VISIBLE);
        switchHandler.setVisibility(View.VISIBLE);
        if (isStopwatchForNursing()) {
            activeStopWatch = firstStopwatchHandler;
            inActiveStopWatch = secondStopwatchHandler;
        } else {
            activeStopWatch = secondStopwatchHandler;
            inActiveStopWatch = firstStopwatchHandler;
        }
    }

    private boolean isStopwatchForNursing() {
        return (sourceTrigger.compareTo(HomeActivity.Trigger.NURSING.getTitle()) == 0);
    }

    private boolean isStopwatchForSleeping() {
        return (sourceTrigger.compareTo(HomeActivity.Trigger.SLEEP.getTitle()) == 0);
    }

    private void activateOneStopwatch() {
        stopwatchTwoContainer.setVisibility(View.GONE);
        switchHandler.setVisibility(View.GONE);
        activeStopWatch = firstStopwatchHandler;
    }

    private void setOnClickListenerToHandler() {
        startHandler.setOnClickListener(this);
        pauseHandler.setOnClickListener(this);
        resetHandler.setOnClickListener(this);
        doneHandler.setOnClickListener(this);
        switchHandler.setOnClickListener(this);
    }

    private void intentCheck() {
        Bundle bundle = getArguments();
        if ((bundle != null) && bundle.containsKey(HomeActivity.ACTIVITY_TRIGGER_KEY)) {
            sourceTrigger = bundle.getString(HomeActivity.ACTIVITY_TRIGGER_KEY);
        }
        if ((bundle != null) && (bundle.containsKey(HomeActivity.ACTIVITY_EDIT_KEY))) {
            editTrigger = bundle.getString(HomeActivity.ACTIVITY_EDIT_KEY);
        }
        if ((bundle != null) && bundle.containsKey(HomeActivity.ACTIVITY_ENTRY_TAG_KEY)) {
            currentEntryTag = bundle.getString(HomeActivity.ACTIVITY_ENTRY_TAG_KEY);
        }
        if ((bundle != null) && bundle.containsKey(ActivityNursing.NURSING_TYPE_KEY)) {
            nursingType = bundle.getString(ActivityNursing.NURSING_TYPE_KEY);
            isTwoStopWatch = true;
        }
        if ((bundle != null) && bundle.containsKey(ActivityNursing.FORMULA_VOLUME_KEY)) {
            formulaVolume = bundle.getString(ActivityNursing.FORMULA_VOLUME_KEY);
            isTwoStopWatch = false;
        }
    }

    private void prepareActionBar() {
        ActionBar actionbar = getActivity().getActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionbar.setTitle(titleConversion(sourceTrigger));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_stopwatch_switch:
                handleSwitchButton();
                return;
            case R.id.button_stopwatch_start:
                handleStartButton();
                return;
            case R.id.button_stopwatch_pause:
                handlePauseButton();
                return;
            case R.id.button_stopwatch_reset:
                handleResetButton();
                return;
            case R.id.button_stopwatch_done:
                handleDoneButton();
                return;
        }
    }

    private String titleConversion(String activityName) {
        if (activityName.equals(HomeActivity.Trigger.NURSING.getTitle())) return ("Nursing Timer");
        else if (activityName.equals(HomeActivity.Trigger.SLEEP.getTitle())) return ("Sleep Timer");
        else return null;
    }

    private void handleSwitchButton() {
        activeStopWatch.stop();
        Stopwatch temp = activeStopWatch;
        activeStopWatch = inActiveStopWatch;
        inActiveStopWatch = temp;
        activeStopWatch.start();
    }

    private void handleStartButton() {
        activeStopWatch.start();
    }

    private void handlePauseButton() {
        activeStopWatch.stop();
    }

    private void handleResetButton() {
        firstStopwatchHandler.reset();
        secondStopwatchHandler.reset();
    }

    private void handleDoneButton() {
        activeStopWatch.stop();
        firstStopwatchDuration = firstStopwatchHandler.getDuration();
        secondStopwatchDuration = secondStopwatchHandler.getDuration();
        if (isStopwatchForSleeping()) proceedWithSleepStopwatch();
        else if (isStopwatchForNursing()) proceedWithNursingStopwatch();
        jumpBackToTimelineFragment();
    }

    private void proceedWithNursingStopwatch() {
        if (isTwoStopWatch) {
            if (firstStopwatchDuration != 0) editStoreLeftSideEntry();
            if (secondStopwatchDuration != 0) editStoreRightSideEntry();
        } else editStoreFormulaEntry();
    }

    private void editStoreLeftSideEntry() {
        if (editTrigger.compareTo(getResources().getString(R.string.menu_edit)) == 0) {
            ActivityNursing editNursingEntry = new ActivityNursing();
            editNursingEntry.setActivityId(Long.valueOf(currentEntryTag));
            editNursingEntry.setDuration(TimeUnit.SECONDS.toMillis(firstStopwatchDuration));
            editNursingEntry.setType(ActivityNursing.NursingType.LEFT);
            editNursingEntry.edit(getActivity());
        } else if (editTrigger.compareTo(getResources().getString(R.string.new_content)) == 0) {
            ActivityNursing activityNursing = new ActivityNursing();
            activityNursing.setTimeStamp(String.valueOf(startTime.getTimeInMillis()));
            activityNursing.setBabyID(ActiveContext.getActiveBaby(getActivity()).getActivityId());
            activityNursing.setDuration(TimeUnit.SECONDS.toMillis(firstStopwatchDuration));
            activityNursing.setType(ActivityNursing.NursingType.LEFT);
            activityNursing.insert(getActivity());
        }
    }

    private void editStoreRightSideEntry() {
        if (editTrigger.compareTo(getResources().getString(R.string.menu_edit)) == 0) {
            ActivityNursing editNursingEntry = new ActivityNursing();
            editNursingEntry.setActivityId(Long.valueOf(currentEntryTag));
            editNursingEntry.setDuration(TimeUnit.SECONDS.toMillis(secondStopwatchDuration));
            editNursingEntry.setType(ActivityNursing.NursingType.RIGHT);
            editNursingEntry.edit(getActivity());
        } else if (editTrigger.compareTo(getResources().getString(R.string.new_content)) == 0) {
            ActivityNursing activityNursing = new ActivityNursing();
            activityNursing.setTimeStamp(String.valueOf(startTime.getTimeInMillis()));
            activityNursing.setBabyID(ActiveContext.getActiveBaby(getActivity()).getActivityId());
            activityNursing.setDuration(TimeUnit.SECONDS.toMillis(secondStopwatchDuration));
            activityNursing.setType(ActivityNursing.NursingType.RIGHT);
            activityNursing.insert(getActivity());
        }
    }

    private void editStoreFormulaEntry() {
        if (editTrigger.compareTo(getResources().getString(R.string.menu_edit)) == 0) {
            ActivityNursing editNursingEntry = new ActivityNursing();
            editNursingEntry.setActivityId(Long.valueOf(currentEntryTag));
            editNursingEntry.setDuration(TimeUnit.SECONDS.toMillis(firstStopwatchDuration));
            editNursingEntry.setType(ActivityNursing.NursingType.FORMULA);
            editNursingEntry.setVolume(Long.parseLong(formulaVolume, 10));
            editNursingEntry.edit(getActivity());
        } else if (editTrigger.compareTo(getResources().getString(R.string.new_content)) == 0) {
            ActivityNursing activityNursing = new ActivityNursing();
            activityNursing.setTimeStamp(String.valueOf(startTime.getTimeInMillis()));
            activityNursing.setBabyID(ActiveContext.getActiveBaby(getActivity()).getActivityId());
            activityNursing.setDuration(TimeUnit.SECONDS.toMillis(firstStopwatchDuration));
            activityNursing.setType(ActivityNursing.NursingType.FORMULA);
            activityNursing.setVolume(Long.parseLong(formulaVolume, 10));
            activityNursing.insert(getActivity());
        }
    }

    private void proceedWithSleepStopwatch() {
        if (editTrigger.compareTo(getResources().getString(R.string.menu_edit)) == 0) {
            ActivitySleep activitySleep = new ActivitySleep();
            activitySleep.setActivityId(Long.valueOf(currentEntryTag));
            activitySleep.setDuration(TimeUnit.SECONDS.toMillis(firstStopwatchDuration));
            activitySleep.edit(getActivity());
        } else if (editTrigger.compareTo(getResources().getString(R.string.new_content)) == 0) {
            ActivitySleep activitySleep = new ActivitySleep();
            activitySleep.setTimeStamp(String.valueOf(startTime.getTimeInMillis()));
            activitySleep.setBabyID(ActiveContext.getActiveBaby(getActivity()).getActivityId());
            activitySleep.setDuration(TimeUnit.SECONDS.toMillis(firstStopwatchDuration));
            activitySleep.insert(getActivity());
        }
    }

    private void jumpBackToTimelineFragment() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setDisplayShowTitleEnabled(false);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_activity_container, TimelineFragment.getInstance());
        fragmentTransaction.commit();
    }
}
