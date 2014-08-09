package com.progrema.superbaby.ui.fragment.history;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private Button startButton;
    private Button pauseButton;
    private Button resetButton;
    private Button doneButton;
    private Button switchButton;
    private TextView durationView;
    private LinearLayout containerStopWatch2;
    private Calendar startTime;
    private String sourceTrigger;
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

        if (args.containsKey(ActivityNursing.NURSING_TYPE_KEY)) {
            nursingType = args.getString(ActivityNursing.NURSING_TYPE_KEY);
            isTwoStopWatch = true;
        }

        if (args.containsKey(ActivityNursing.FORMULA_VOLUME_KEY)) {
            formulaVolume = args.getString(ActivityNursing.FORMULA_VOLUME_KEY);
            isTwoStopWatch = false;
        }

        // inflate fragment layout
        View rootView = inflater.inflate(R.layout.fragment_stopwatch, container, false);

        // get object from fragment layout
        titleView = (TextView) rootView.findViewById(R.id.stopwatch_title_view);
        titleView.setText(sourceTrigger);

        ActionBar abActionBar = getActivity().getActionBar();
        abActionBar.setDisplayShowTitleEnabled(true);
        abActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        abActionBar.setTitle(sourceTrigger);

        startButton = (Button) rootView.findViewById(R.id.button_stopwatch_start);
        pauseButton = (Button) rootView.findViewById(R.id.button_stopwatch_pause);
        resetButton = (Button) rootView.findViewById(R.id.button_stopwatch_reset);
        doneButton = (Button) rootView.findViewById(R.id.button_stopwatch_done);
        durationView = (TextView) rootView.findViewById(R.id.stopwatch_duration_view);
        containerStopWatch2 = (LinearLayout) rootView.findViewById(R.id.container_stopwatch2);
        switchButton = (Button) rootView.findViewById(R.id.button_stopwatch_switch);

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
        String sDuration;
        sDuration = String.valueOf(activeStopWatch.getDuration());
        durationView.setText(sDuration);
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
            ActivitySleep activitySleep = new ActivitySleep();
            activitySleep.setTimeStamp(String.valueOf(startTime.getTimeInMillis()));
            activitySleep.setBabyID(ActiveContext.getActiveBaby(getActivity()).getActivityId());
            activitySleep.setDuration(TimeUnit.SECONDS.toMillis(firstDuration));
            activitySleep.insert(getActivity());

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
                    activityNursing.setDuration(TimeUnit.SECONDS.toMillis(firstDuration));
                    activityNursing.setType(ActivityNursing.NursingType.LEFT);
                    activityNursing.insert(getActivity());
                }
                if (secondDuration != 0) {
                    activityNursing.setDuration(TimeUnit.SECONDS.toMillis(secondDuration));
                    activityNursing.setType(ActivityNursing.NursingType.RIGHT);
                    activityNursing.insert(getActivity());
                }
            } else {
                // formula
                activityNursing.setDuration(TimeUnit.SECONDS.toMillis(firstDuration));
                activityNursing.setType(ActivityNursing.NursingType.valueOf(nursingType));
                activityNursing.setVolume(Long.parseLong(formulaVolume, 10));
                activityNursing.insert(getActivity());
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
