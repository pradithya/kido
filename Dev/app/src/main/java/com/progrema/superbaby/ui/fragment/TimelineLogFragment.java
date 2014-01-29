package com.progrema.superbaby.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.progrema.superbaby.R;

/**
 * Created by iqbalpakeh on 18/1/14.
 */
public class TimelineLogFragment extends Fragment implements View.OnClickListener
{

    private static TimelineLogFragment singletonTimelineLogFragment = null;
    private Button buttonQuickSleep;

    public TimelineLogFragment(Context context)
    {
        /** Empty constructor */
    }

    public static synchronized TimelineLogFragment getInstance(Context context)
    {
        if (singletonTimelineLogFragment == null)
        {
            singletonTimelineLogFragment = new TimelineLogFragment(context.getApplicationContext());
        }
        return singletonTimelineLogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        /** inflate fragment layout */
        View rootView = inflater.inflate(R.layout.fragment_timeline, container, false);

        /** set onClickListener to button */
        buttonQuickSleep = (Button) rootView.findViewById(R.id.quick_button_sleep);
        buttonQuickSleep.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case  R.id.quick_button_sleep:
                handleQuickSleep();
                break;
        }
    }

    private void handleQuickSleep()
    {
        /** Jump to stopwatch fragment */
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_activity_container, StopwatchFragment.getInstance(getActivity()));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
