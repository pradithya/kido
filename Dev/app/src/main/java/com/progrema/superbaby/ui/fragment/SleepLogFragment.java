package com.progrema.superbaby.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.progrema.superbaby.R;

/**
 * Created by iqbalpakeh on 18/1/14.
 */
public class SleepLogFragment extends Fragment{

    private static SleepLogFragment singletonSleepLogFragment = null;

    public SleepLogFragment(Context context){

    }

    public static synchronized SleepLogFragment getInstance(Context context){
        if (singletonSleepLogFragment == null) {
            singletonSleepLogFragment = new SleepLogFragment(context.getApplicationContext());
        }
        return singletonSleepLogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sleep_log, container, false);
        return rootView;
    }

}
