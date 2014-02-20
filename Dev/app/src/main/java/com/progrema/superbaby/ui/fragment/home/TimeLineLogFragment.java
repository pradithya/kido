package com.progrema.superbaby.ui.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.progrema.superbaby.R;
import com.progrema.superbaby.models.Diaper;
import com.progrema.superbaby.ui.fragment.dialog.DiaperDialogFragment;

import java.util.Calendar;

/**
 * Created by iqbalpakeh on 18/1/14.
 */
public class TimeLineLogFragment extends Fragment implements View.OnClickListener
{

    public final static int REQUEST_DIAPER = 0;
    public final static int REQUEST_SLEEP = 1;
    public final static int REQUEST_NURSING = 2;

    public final static int RESULT_OK = 0;

    private static TimeLineLogFragment singletonTimeLineLogFragment = null;
    private Button buttonQuickSleep;
    private Button buttonQuickDiaper;
    private Button buttonQuickNursing;

    public final static String ACTIVITY_TRIGGER_KEY = "trigger";
    public enum Trigger
    {
        SLEEP("sleep"),
        NURSING("nursing"),
        DIAPER("diaper");

        private String title;

        Trigger(String title)
        {
            this.title = title;
        }

        public String getTitle()
        {
            return this.title;
        }
    }

    public static synchronized TimeLineLogFragment getInstance()
    {
        if (singletonTimeLineLogFragment == null)
        {
            singletonTimeLineLogFragment = new TimeLineLogFragment();
        }
        return singletonTimeLineLogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // inflate fragment layout
        View rootView = inflater.inflate(R.layout.fragment_timeline, container, false);

        // set onClickListener to button
        buttonQuickSleep = (Button) rootView.findViewById(R.id.quick_button_sleep);
        buttonQuickSleep.setOnClickListener(this);

        // set onClickListener to button
        buttonQuickNursing = (Button) rootView.findViewById(R.id.quick_button_nursing);
        buttonQuickNursing.setOnClickListener(this);

        // set onClickListener to button
        buttonQuickDiaper = (Button) rootView.findViewById(R.id.quick_button_diaper);
        buttonQuickDiaper.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.quick_button_sleep:
                handleQuickSleep();
                break;

            case R.id.quick_button_nursing:
                handleQuickNursing();
                break;

            case R.id.quick_button_diaper:
                handleQuickDiaper();
                break;

        }
    }

    private void handleQuickSleep()
    {
        // Jump to stopwatch fragment
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        StopwatchFragment frStopWatch = StopwatchFragment.getInstance();

        /** inform the stopwatch to start counting for sleep*/
        Bundle bundle = new Bundle();
        bundle.putString(ACTIVITY_TRIGGER_KEY, Trigger.SLEEP.getTitle());
        frStopWatch.setArguments(bundle);

        fragmentTransaction.replace(R.id.home_activity_container, frStopWatch);
        fragmentTransaction.commit();
    }

    private void handleQuickDiaper()
    {
        // Jump to stopwatch fragment
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        DiaperDialogFragment diaperChoiceBox = DiaperDialogFragment.getInstance();
        diaperChoiceBox.setTargetFragment(this,REQUEST_DIAPER);

        diaperChoiceBox.show(fragmentTransaction,"dialog");
    }

    private void handleQuickNursing()
    {
        // Jump to stopwatch fragment
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        StopwatchFragment frStopWatch = StopwatchFragment.getInstance();

        /** inform the stopwatch to start counting for sleep*/
        Bundle bundle = new Bundle();
        bundle.putString(ACTIVITY_TRIGGER_KEY, Trigger.SLEEP.getTitle());
        frStopWatch.setArguments(bundle);

        fragmentTransaction.replace(R.id.home_activity_container, StopwatchFragment.getInstance());
        fragmentTransaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_DIAPER:
                    Calendar currentTime = Calendar.getInstance();
                    Bundle recData = data.getExtras();
                    String diaperType = (String) recData.get(Diaper.DIAPER_TYPE_KEY);
                    Diaper addedActivity = new Diaper();
                    addedActivity.setTimeStamp(String.valueOf(currentTime.getTimeInMillis()));
                    addedActivity.setType(Diaper.DiaperType.valueOf(diaperType));
                    addedActivity.insert(getActivity());

                    /** go to sleep log*/
                    break;
            }
        }
    }
}
