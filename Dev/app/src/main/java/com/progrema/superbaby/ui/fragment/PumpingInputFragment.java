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
public class PumpingInputFragment extends Fragment{

    public static final String INTENT = "intent_pumping_input_fragment";

    private static PumpingInputFragment singletonPumpingInputFragment = null;

    public PumpingInputFragment(Context context){

    }

    public static synchronized PumpingInputFragment getInstance(Context context){
        if (singletonPumpingInputFragment == null) {
            singletonPumpingInputFragment = new PumpingInputFragment(context.getApplicationContext());
        }
        return singletonPumpingInputFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pumping_input, container, false);
        return rootView;
    }

}
