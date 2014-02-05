package com.progrema.superbaby.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.progrema.superbaby.R;
import com.progrema.superbaby.models.Sleep;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by iqbalpakeh on 20/1/14.
 */
public class SleepInputFragment extends Fragment implements View.OnClickListener
{

    private static SleepInputFragment singletonSleepInputFragment = null;
    private Button done;
    private EditText babyIdInput;
    private EditText durationInput;

    public static synchronized SleepInputFragment getInstance()
    {
        if (singletonSleepInputFragment == null)
        {
            singletonSleepInputFragment = new SleepInputFragment();
        }
        return singletonSleepInputFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // inflate fragment layout
        View rootView = inflater.inflate(R.layout.fragment_sleep_input, container, false);

        // set onClickListener to button
        done = (Button) rootView.findViewById(R.id.button_done);
        done.setOnClickListener(this);

        // get edit text object
        babyIdInput = (EditText) rootView.findViewById(R.id.baby_id_input);
        durationInput = (EditText) rootView.findViewById(R.id.duration_input);

        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.button_done:
                handleDoneButton();
                break;
        }
    }

    private void handleDoneButton()
    {
        String babyIdInputBuffer;
        String durationInputBuffer;
        Calendar currentTime;

        currentTime = Calendar.getInstance();

        // Get data from UI
        babyIdInputBuffer = babyIdInput.getText().toString();
        durationInputBuffer = durationInput.getText().toString();

        // Store to DB
        Sleep sleep = new Sleep();
        sleep.setTimeStamp(String.valueOf(currentTime.getTimeInMillis()));
        sleep.setBabyID(Long.parseLong(babyIdInputBuffer));
        sleep.setDuration(TimeUnit.SECONDS.toMillis(Long.parseLong(durationInputBuffer)));
        sleep.insert(getActivity());

        // Go back to sleep log fragment
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_activity_container, SleepLogFragment.getInstance());
        fragmentTransaction.commit();

    }
}
