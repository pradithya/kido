package com.progrema.superbaby.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.progrema.superbaby.R;

/**
 * Created by iqbalpakeh on 20/1/14.
 */
public class SleepInputFragment extends Fragment{

    public static final String INTENT = "intent_sleep_input_fragment";

    private static SleepInputFragment singletonSleepInputFragment = null;

    public SleepInputFragment(Context context){

    }

    public static synchronized SleepInputFragment getInstance(Context context){
        if (singletonSleepInputFragment == null) {
            singletonSleepInputFragment = new SleepInputFragment(context.getApplicationContext());
        }
        return singletonSleepInputFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sleep_input, container, false);
        return rootView;
    }

}
