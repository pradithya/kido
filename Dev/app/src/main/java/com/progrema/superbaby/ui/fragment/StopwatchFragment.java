package com.progrema.superbaby.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.progrema.superbaby.R;
import com.progrema.superbaby.widget.stopwatch.Stopwatch;

/**
 * Created by iqbalpakeh on 29/1/14.
 */
public class StopwatchFragment extends Fragment implements View.OnClickListener
{

    private static StopwatchFragment singletonStopwatchFragment = null;
    private Stopwatch stopwatch;
    private Button startButton;
    private Button stopButton;
    private Button resetButton;
    private Button doneButton;
    private TextView durationView;

    public StopwatchFragment(Context context)
    {
        /** empty constructor */
    }

    public static synchronized StopwatchFragment getInstance(Context context)
    {
        if (singletonStopwatchFragment == null)
        {
            singletonStopwatchFragment = new StopwatchFragment(context.getApplicationContext());
        }
        return singletonStopwatchFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        /** inflate fragment layout */
        View rootView = inflater.inflate(R.layout.fragment_stopwatch, container, false);

        /** get object from fragment layout */
        startButton = (Button) rootView.findViewById(R.id.button_stopwatch_start);
        stopButton = (Button) rootView.findViewById(R.id.button_stopwatch_stop);
        resetButton = (Button) rootView.findViewById(R.id.button_stopwatch_reset);
        doneButton = (Button) rootView.findViewById(R.id.button_stopwatch_done);
        durationView = (TextView) rootView.findViewById(R.id.stopwatch_duration_view);

        /** set onClickListener to button */
        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);

        /** get stopwatch */
        stopwatch = (Stopwatch) rootView.findViewById(R.id.chronometer_widget);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.button_stopwatch_start:
                handleStartButton();
                return;

            case R.id.button_stopwatch_stop:
                handleStopButton();
                return;

            case  R.id.button_stopwatch_reset:
                handleResetButton();
                return;

            case  R.id.button_stopwatch_done:
                handleDoneButton();
                return;
        }
    }

    private void handleStartButton()
    {
        stopwatch.start();
    }

    private void handleStopButton()
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

        /** Go back to timeline fragment */
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_activity_container, TimelineLogFragment.getInstance(getActivity()));
        fragmentTransaction.commit();
    }

}
