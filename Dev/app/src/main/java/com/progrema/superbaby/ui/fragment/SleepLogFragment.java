package com.progrema.superbaby.ui.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.progrema.superbaby.R;

/**
 * Created by iqbalpakeh on 18/1/14.
 */
public class SleepLogFragment extends Fragment implements View.OnClickListener{

    private static SleepLogFragment singletonSleepLogFragment = null;
    private Button startButton;

    public SleepLogFragment(Context context){}

    public static synchronized SleepLogFragment getInstance(Context context){
        if (singletonSleepLogFragment == null) {
            singletonSleepLogFragment = new SleepLogFragment(context.getApplicationContext());
        }
        return singletonSleepLogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate fragment layout
        View rootView = inflater.inflate(R.layout.fragment_sleep_log, container, false);

        // set onClickListener to button
        startButton = (Button)rootView.findViewById(R.id.button_start);
        startButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){

            case  R.id.button_start:
                handleStartButton();
                break;

        }
    }

    private void handleStartButton(){

        // jump to sleep input fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
            .replace(R.id.home_activity_container, SleepInputFragment.getInstance(getActivity())).commit();

    }

}
