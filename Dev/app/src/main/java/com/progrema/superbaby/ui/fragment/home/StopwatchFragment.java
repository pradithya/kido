package com.progrema.superbaby.ui.fragment.home;

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
import com.progrema.superbaby.models.Nursing;
import com.progrema.superbaby.models.Sleep;
import com.progrema.superbaby.ui.activity.HomeActivity;
import com.progrema.superbaby.util.ActiveContext;
import com.progrema.superbaby.widget.stopwatch.Stopwatch;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by iqbalpakeh on 29/1/14.
 *
 * @author aria
 */
public class StopwatchFragment extends Fragment implements View.OnClickListener {
    private Stopwatch stopwatch;
    private Stopwatch stopwatch2;
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

        if (args.containsKey(Nursing.NURSING_TYPE_KEY)) {
            nursingType = args.getString(Nursing.NURSING_TYPE_KEY);
            isTwoStopWatch = true;
        }

        if (args.containsKey(Nursing.FORMULA_VOLUME_KEY)) {
            formulaVolume = args.getString(Nursing.FORMULA_VOLUME_KEY);
            isTwoStopWatch = false;
        }

        // inflate fragment layout
        View rootView = inflater.inflate(R.layout.fragment_stopwatch, container, false);

        // get object from fragment layout
        titleView = (TextView) rootView.findViewById(R.id.stopwatch_title_view);
        titleView.setText(sourceTrigger);

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

        // get stopwatch & start
        stopwatch = (Stopwatch) rootView.findViewById(R.id.chronometer_widget_a);
        stopwatch2 = (Stopwatch) rootView.findViewById(R.id.chronometer_widget_b);

        if (isTwoStopWatch) {
            containerStopWatch2.setVisibility(View.VISIBLE);
            switchButton.setVisibility(View.VISIBLE);

            if (nursingType.equals(Nursing.NursingType.LEFT.getTitle())) {
                activeStopWatch = stopwatch;
                inActiveStopWatch = stopwatch2;
            } else {
                activeStopWatch = stopwatch2;
                inActiveStopWatch = stopwatch;
            }
        } else {
            containerStopWatch2.setVisibility(View.GONE);
            switchButton.setVisibility(View.GONE);
            activeStopWatch = stopwatch;
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
        stopwatch.reset();
        stopwatch2.reset();
    }

    private void handleDoneButton() {
        activeStopWatch.stop();
        long duration = stopwatch.getDuration();
        long duration2 = stopwatch2.getDuration();

        if (sourceTrigger.compareTo(HomeActivity.Trigger.SLEEP.getTitle()) == 0) {
            Sleep sleep = new Sleep();
            sleep.setTimeStamp(String.valueOf(startTime.getTimeInMillis()));
            sleep.setBabyID(ActiveContext.getActiveBaby(getActivity()).getID());
            sleep.setDuration(TimeUnit.SECONDS.toMillis(duration));
            sleep.insert(getActivity());
        } else if (sourceTrigger.compareTo(HomeActivity.Trigger.NURSING.getTitle()) == 0) {
            Nursing nursing = new Nursing();
            nursing.setTimeStamp(String.valueOf(startTime.getTimeInMillis()));
            nursing.setBabyID(ActiveContext.getActiveBaby(getActivity()).getID());
            if (isTwoStopWatch) {
                if (duration != 0) {
                    nursing.setDuration(TimeUnit.SECONDS.toMillis(duration));
                    nursing.setType(Nursing.NursingType.LEFT);
                    nursing.insert(getActivity());
                }
                if (duration2 != 0) {
                    nursing.setDuration(TimeUnit.SECONDS.toMillis(duration2));
                    nursing.setType(Nursing.NursingType.RIGHT);
                    nursing.insert(getActivity());
                }
            } else {
                // formula
                nursing.setDuration(TimeUnit.SECONDS.toMillis(duration));
                nursing.setType(Nursing.NursingType.valueOf(nursingType));
                nursing.setVolume(Long.parseLong(formulaVolume, 10));
                nursing.insert(getActivity());
            }
        }
        // Go back to timeLine fragment
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_activity_container, TimeLineFragment.getInstance());
        fragmentTransaction.commit();
    }

}
