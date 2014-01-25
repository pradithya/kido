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
 * Created by iqbalpakeh on 20/1/14.
 */
public class SleepInputFragment extends Fragment implements View.OnClickListener{

    private static SleepInputFragment singletonSleepInputFragment = null;
    private Button doneButton;

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
        doneButton = (Button) rootView.findViewById(R.id.button_done);
        doneButton.setOnClickListener(this);

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

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.home_activity_container, SleepLogFragment.getInstance(getActivity())).commit();


    }
}
