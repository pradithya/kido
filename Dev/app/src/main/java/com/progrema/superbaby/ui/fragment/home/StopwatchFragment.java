package com.progrema.superbaby.ui.fragment.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.progrema.superbaby.R;
import com.progrema.superbaby.models.Sleep;
import com.progrema.superbaby.widget.stopwatch.Stopwatch;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by iqbalpakeh on 29/1/14.
 */
public class StopwatchFragment extends Fragment implements View.OnClickListener
{

    private static StopwatchFragment singletonStopwatchFragment = null;
    private Stopwatch stopwatch;
    private TextView titleView;
    private Button startButton;
    private Button pauseButton;
    private Button resetButton;
    private Button doneButton;
    private String sourceTrigger;
    private TextView durationView;

    public static synchronized StopwatchFragment getInstance()
    {
        if (singletonStopwatchFragment == null)
        {
            singletonStopwatchFragment = new StopwatchFragment();
        }
        return singletonStopwatchFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        Bundle args = getArguments();
        if (args != null && args.containsKey("source"))
        {
            sourceTrigger = args.getString("source");
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

        // set onClickListener to button
        startButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);

        // get stopwatch
        stopwatch = (Stopwatch) rootView.findViewById(R.id.chronometer_widget);

        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
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

    private void handleStartButton()
    {
        stopwatch.start();
    }

    private void handlePauseButton()
    {
        stopwatch.stop();

        String sDuration;
        sDuration = String.valueOf(stopwatch.getDuration());
        durationView.setText(sDuration);
    }

    private void handleResetButton()
    {
        stopwatch.reset();
    }

    private void handleDoneButton()
    {

        stopwatch.stop();
        long duration = stopwatch.getDuration();

        Calendar currentTime = Calendar.getInstance();

        if (sourceTrigger.compareTo("Sleep") == 0)
        {
            Sleep sleep = new Sleep();
            /*test with baby ID  = 1*/
            sleep.setTimeStamp(String.valueOf(currentTime.getTimeInMillis()));
            sleep.setBabyID(1);
            sleep.setDuration(TimeUnit.SECONDS.toMillis(duration));
            sleep.insert(getActivity());
        }

        // Go back to timeline fragment
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_activity_container, TimeLineLogFragment.getInstance());
        fragmentTransaction.commit();
    }

}
