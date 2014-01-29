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

/**
 * Created by iqbalpakeh on 29/1/14.
 */
public class StopwatchFragment extends Fragment implements View.OnClickListener
{

    private static StopwatchFragment singletonStopwatchFragment = null;
    private TextView digitalWatch;
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
        stopButton = (Button) rootView.findViewById(R.id.button_stopwatch_stop);
        stopButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case  R.id.button_stopwatch_stop:
                handleStopButton();
                break;
        }
    }

    private void handleStopButton()
    {
        /** Go back to timeline fragment */
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_activity_container, TimelineLogFragment.getInstance(getActivity()));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
