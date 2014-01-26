package com.progrema.superbaby.ui.fragment;

import android.content.ContentValues;
import android.content.Context;
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
import com.progrema.superbaby.provider.BabyLogContract;

/**
 * Created by iqbalpakeh on 20/1/14.
 */
public class SleepInputFragment extends Fragment implements View.OnClickListener{

    private static SleepInputFragment singletonSleepInputFragment = null;
    private Button done;
    private EditText activityIdInput;
    private EditText babyIdInput;
    private EditText timestampInput;
    private EditText durationInput;

    public SleepInputFragment(Context context){}

    public static synchronized SleepInputFragment getInstance(Context context){
        if (singletonSleepInputFragment == null) {
            singletonSleepInputFragment = new SleepInputFragment(context.getApplicationContext());
        }
        return singletonSleepInputFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate fragment layout
        View rootView = inflater.inflate(R.layout.fragment_sleep_input, container, false);

        // set onClickListener to button
        done = (Button) rootView.findViewById(R.id.button_done);
        done.setOnClickListener(this);

        // get edit text object
        activityIdInput = (EditText) rootView.findViewById(R.id.activity_id_input);
        babyIdInput = (EditText) rootView.findViewById(R.id.baby_id_input);
        timestampInput = (EditText) rootView.findViewById(R.id.timestamp_input);
        durationInput = (EditText) rootView.findViewById(R.id.duration_input);

        return rootView;
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){

            case R.id.button_done:
                handleDoneButton();
                break;

        }

    }

    private void handleDoneButton(){

        String activityIdInputBuffer;
        String babyIdInputBuffer;
        String timestampInputBuffer;
        String durationInputBuffer;

        /** Get data from UI */
        activityIdInputBuffer = activityIdInput.getText().toString();
        babyIdInputBuffer = babyIdInput.getText().toString();
        timestampInputBuffer = timestampInput.getText().toString();
        durationInputBuffer = durationInput.getText().toString();

        /** Store to DB */
        Sleep sleep = new Sleep();
        sleep.setTimeStamp(timestampInputBuffer);
        sleep.setDuration(Long.parseLong(durationInputBuffer));

        ContentValues values = new ContentValues();
        values.put(BabyLogContract.Sleep.ACTIVITY_ID, activityIdInputBuffer);
        values.put(BabyLogContract.Sleep.BABY_ID, babyIdInputBuffer);
        values.put(BabyLogContract.Sleep.TIMESTAMP, sleep.getTimeStampInString());
        values.put(BabyLogContract.Sleep.DURATION, sleep.getDuration());

        getActivity().getContentResolver().insert(BabyLogContract.Sleep.CONTENT_URI, values);

        /** Go back to sleep log fragment */
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_activity_container, SleepLogFragment.getInstance(getActivity()));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}
