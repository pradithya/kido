package com.progrema.superbaby.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import com.progrema.superbaby.R;

/**
 * Created by iqbalpakeh on 29/1/14.
 */
public class StopwatchFragment extends Fragment implements View.OnClickListener
{

    private static StopwatchFragment singletonStopwatchFragment = null;
    private Chronometer chronometer;
    private Button startButton;
    private Button pauseButton;
    private Button stopButton;

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

        /** set onClickListener to button */
        startButton = (Button) rootView.findViewById(R.id.button_stopwatch_start);
        startButton.setOnClickListener(this);
        pauseButton = (Button) rootView.findViewById(R.id.button_stopwatch_pause);
        pauseButton.setOnClickListener(this);
        stopButton = (Button) rootView.findViewById(R.id.button_stopwatch_stop);
        stopButton.setOnClickListener(this);

        /** get chronometer */
        chronometer = (Chronometer) rootView.findViewById(R.id.chronometer_widget);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.button_stopwatch_start:
                handleStartButton();
                return;

            case R.id.button_stopwatch_pause:
                handlePauseButton();
                return;

            case  R.id.button_stopwatch_stop:
                handleStopButton();
                return;
        }
    }

    private void handleStartButton()
    {
        chronometer.start();
    }

    private void handlePauseButton()
    {
        //TODO: implement pause!
    }

    private void handleStopButton()
    {

        chronometer.stop();

        /** Go back to timeline fragment */
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_activity_container, TimelineLogFragment.getInstance(getActivity()));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
