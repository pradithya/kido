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
public class VaccineLogFragment extends Fragment{

    private static VaccineLogFragment singletonVaccineLogFragment = null;

    public VaccineLogFragment(Context context){}

    public static synchronized VaccineLogFragment getInstance(Context context){
        if (singletonVaccineLogFragment == null) {
            singletonVaccineLogFragment = new VaccineLogFragment(context.getApplicationContext());
        }
        return singletonVaccineLogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vaccine_log, container, false);
        return rootView;
    }

}
