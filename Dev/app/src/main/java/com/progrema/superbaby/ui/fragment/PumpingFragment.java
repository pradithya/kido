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
public class PumpingFragment extends Fragment{

    private static PumpingFragment singletonPumpingFragment = null;

    public PumpingFragment(Context context){

    }

    public static synchronized PumpingFragment getInstance(Context context){
        if (singletonPumpingFragment == null) {
            singletonPumpingFragment = new PumpingFragment(context.getApplicationContext());
        }
        return singletonPumpingFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pumping, container, false);
        return rootView;
    }

}
