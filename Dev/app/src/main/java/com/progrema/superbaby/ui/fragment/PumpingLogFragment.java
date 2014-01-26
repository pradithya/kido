package com.progrema.superbaby.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.progrema.superbaby.R;

/**
 * Created by iqbalpakeh on 18/1/14.
 */
public class PumpingLogFragment extends Fragment {

    private static PumpingLogFragment singletonPumpingLogFragment = null;

    public PumpingLogFragment(Context context){}

    public static synchronized PumpingLogFragment getInstance(Context context){
        if (singletonPumpingLogFragment == null) {
            singletonPumpingLogFragment = new PumpingLogFragment(context.getApplicationContext());
        }
        return singletonPumpingLogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pumping_log, container, false);
        return rootView;
    }

}
