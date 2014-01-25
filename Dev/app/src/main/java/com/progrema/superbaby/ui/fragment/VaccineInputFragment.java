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
public class VaccineInputFragment extends Fragment{

    private static VaccineInputFragment singletonVaccineInputFragment = null;

    public VaccineInputFragment(Context context){}

    public static synchronized VaccineInputFragment getInstance(Context context){
        if (singletonVaccineInputFragment == null) {
            singletonVaccineInputFragment = new VaccineInputFragment(context.getApplicationContext());
        }
        return singletonVaccineInputFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vaccine_input, container, false);
        return rootView;
    }

}
