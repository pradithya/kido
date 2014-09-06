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

    private Stopwatch firstStopwatch;
    private Stopwatch secondStopwatch;
    private Stopwatch activeStopWatch;
    private Stopwatch inActiveStopWatch;
    private TextView titleView;
    private ImageButton startButton;
    private ImageButton pauseButton;
    private ImageButton resetButton;
    private ImageButton doneButton;
    private ImageButton switchButton;
    private LinearLayout containerStopWatch2;
    private Calendar startTime;
    private String sourceTrigger;
    private String editTrigger;
    private String currentEntryTag;
    private String nursingType;
    private String formulaVolume;
    private boolean isTwoStopWatch;

    public static StopwatchFragment getInstance() {
        return new StopwatchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle args = getArguments();
        if (args != null && args.containsKey(HomeActivity.ACTIVITY_TRIGGER_KEY)) {
            sourceTrigger = args.getString(HomeActivity.ACTIVITY_TRIGGER_KEY);
        }

        if (args != null && args.containsKey(HomeActivity.ACTIVITY_EDIT_KEY)) {
            editTrigger = args.getString(HomeActivity.ACTIVITY_EDIT_KEY);
        }

        if (args != null && args.containsKey(HomeActivity.ACTIVITY_ENTRY_TAG_KEY)) {
            currentEntryTag = args.getString(HomeActivity.ACTIVITY_ENTRY_TAG_KEY);
        }

        if (args != null && args.containsKey(ActivityNursing.NURSING_TYPE_KEY)) {
            nursingType = args.getString(ActivityNursing.NURSING_TYPE_KEY);
            isTwoStopWatch = true;
        }

        if (args != null && args.containsKey(ActivityNursing.FORMULA_VOLUME_KEY)) {
            formulaVolume = args.getString(ActivityNursing.FORMULA_VOLUME_KEY);
            isTwoStopWatch = false;
        }

        // inflate fragment layout
        View rootView = inflater.inflate(R.layout.fragment_stopwatch, container, false);

        ActionBar abActionBar = getActivity().getActionBar();
        abActionBar.setDisplayShowTitleEnabled(true);
        abActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        abActionBar.setTitle(titleConversion(sourceTrigger));

        startButton = (ImageButton) rootView.findViewById(R.id.button_stopwatch_start);
        pauseButton = (ImageButton) rootView.findViewById(R.id.button_stopwatch_pause);
        resetButton = (ImageButton) rootView.findViewById(R.id.button_stopwatch_reset);
        doneButton = (ImageButton) rootView.findViewById(R.id.button_stopwatch_done);
        containerStopWatch2 = (LinearLayout) rootView.findViewById(R.id.container_stopwatch2);
        switchButton = (ImageButton) rootView.findViewById(R.id.button_stopwatch_switch);

        // set onClickListener to button
        startButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);
        switchButton.setOnClickListener(this);

        // get firstStopwatch & start
        firstStopwatch = (Stopwatch) rootView.findViewById(R.id.chronometer_widget_a);
        secondStopwatch = (Stopwatch) rootView.findViewById(R.id.chronometer_widget_b);

        if (isTwoStopWatch) {
            containerStopWatch2.setVisibility(View.VISIBLE);
            switchButton.setVisibility(View.VISIBLE);

            if (nursingType.equals(ActivityNursing.NursingType.LEFT.getTitle())) {
                activeStopWatch = firstStopwatch;
                inActiveStopWatch = secondStopwatch;
            } else {
                activeStopWatch = secondStopwatch;
                inActiveStopWatch = firstStopwatch;
            }
        } else {
            containerStopWatch2.setVisibility(View.GONE);
            switchButton.setVisibility(View.GONE);
            activeStopWatch = firstStopwatch;
        }

        startTime = Calendar.getInstance();
        activeStopWatch.start();

        return rootView;
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
        if (activityName.equals(HomeActivity.Trigger.NURSING.getTitle())) {
            return "Nursing Timer";
        } else if (activityName.equals(HomeActivity.Trigger.SLEEP.getTitle())) {
            return "Sleep Timer";
        }
        return "";
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
        firstStopwatch.reset();
        secondStopwatch.reset();
    }

    private void handleDoneButton() {

        activeStopWatch.stop();
        long firstDuration = firstStopwatch.getDuration();
        long secondDuration = secondStopwatch.getDuration();

        if (sourceTrigger.compareTo(HomeActivity.Trigger.SLEEP.getTitle()) == 0) {
            if (editTrigger != null &&
                    (editTrigger.compareTo(getResources().getString(R.string.menu_edit)) == 0)) {
                ActivitySleep activitySleep = new ActivitySleep();
                activitySleep.setActivityId(Long.valueOf(currentEntryTag));
                activitySleep.setDuration(TimeUnit.SECONDS.toMillis(firstDuration));
                activitySleep.edit(getActivity());
            } else {
                ActivitySleep activitySleep = new ActivitySleep();
                activitySleep.setTimeStamp(String.valueOf(startTime.getTimeInMillis()));
                activitySleep.setBabyID(ActiveContext.getActiveBaby(getActivity()).getActivityId());
                activitySleep.setDuration(TimeUnit.SECONDS.toMillis(firstDuration));
                activitySleep.insert(getActivity());
            }
            // Go back to timeLine fragment
            ActionBar actionBar = getActivity().getActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
            actionBar.setDisplayShowTitleEnabled(false);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.home_activity_container, SleepFragment.getInstance());
            fragmentTransaction.commit();
        } else if (sourceTrigger.compareTo(HomeActivity.Trigger.NURSING.getTitle()) == 0) {
            ActivityNursing activityNursing = new ActivityNursing();
            activityNursing.setTimeStamp(String.valueOf(startTime.getTimeInMillis()));
            activityNursing.setBabyID(ActiveContext.getActiveBaby(getActivity()).getActivityId());
            if (isTwoStopWatch) {
                if (firstDuration != 0) {
                    if (editTrigger != null &&
                            editTrigger.compareTo(getResources().getString(R.string.menu_edit)) == 0) {
                        //TODO: implement nursing update operation
                        ActivityNursing editNursingEntry = new ActivityNursing();
                        editNursingEntry.setActivityId(Long.valueOf(currentEntryTag));
                        editNursingEntry.setDuration(TimeUnit.SECONDS.toMillis(firstDuration));
                        editNursingEntry.setType(ActivityNursing.NursingType.LEFT);
                        editNursingEntry.edit(getActivity());
                    } else {
                        activityNursing.setDuration(TimeUnit.SECONDS.toMillis(firstDuration));
                        activityNursing.setType(ActivityNursing.NursingType.LEFT);
                        activityNursing.insert(getActivity());
                    }
                }
                if (secondDuration != 0) {
                    if (editTrigger != null &&
                            editTrigger.compareTo(getResources().getString(R.string.menu_edit)) == 0) {
                        //TODO: implement nursing update operation
                        ActivityNursing editNursingEntry = new ActivityNursing();
                        editNursingEntry.setActivityId(Long.valueOf(currentEntryTag));
                        editNursingEntry.setDuration(TimeUnit.SECONDS.toMillis(secondDuration));
                        editNursingEntry.setType(ActivityNursing.NursingType.RIGHT);
                        editNursingEntry.edit(getActivity());
                    } else {
                        activityNursing.setDuration(TimeUnit.SECONDS.toMillis(secondDuration));
                        activityNursing.setType(ActivityNursing.NursingType.RIGHT);
                        activityNursing.insert(getActivity());
                    }
                }
            } else {
                // formula
                if (editTrigger != null &&
                        editTrigger.compareTo(getResources().getString(R.string.menu_edit)) == 0) {
                    //TODO: implement nursing update operation
                    ActivityNursing editNursingEntry = new ActivityNursing();
                    editNursingEntry.setActivityId(Long.valueOf(currentEntryTag));
                    editNursingEntry.setDuration(TimeUnit.SECONDS.toMillis(firstDuration));
                    editNursingEntry.setType(ActivityNursing.NursingType.FORMULA);
                    editNursingEntry.setVolume(Long.parseLong(formulaVolume, 10));
                    editNursingEntry.edit(getActivity());
                } else {
                    activityNursing.setDuration(TimeUnit.SECONDS.toMillis(firstDuration));
                    activityNursing.setType(ActivityNursing.NursingType.FORMULA);
                    activityNursing.setVolume(Long.parseLong(formulaVolume, 10));
                    activityNursing.insert(getActivity());
                }
            }
            // Go back to timeLine fragment
            ActionBar actionBar = getActivity().getActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
            actionBar.setDisplayShowTitleEnabled(false);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.home_activity_container, NursingFragment.getInstance());
            fragmentTransaction.commit();
        }
    }
}
