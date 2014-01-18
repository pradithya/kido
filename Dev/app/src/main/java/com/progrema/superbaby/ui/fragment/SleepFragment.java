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
public class SleepFragment extends Fragment{

    private static SleepFragment singletonSleepFragment = null;

    public SleepFragment(Context context){

    }

    public static synchronized SleepFragment getInstance(Context context){
        if (singletonSleepFragment == null) {
            singletonSleepFragment = new SleepFragment(context.getApplicationContext());
        }
        return singletonSleepFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sleep, container, false);
        return rootView;
    }

}
