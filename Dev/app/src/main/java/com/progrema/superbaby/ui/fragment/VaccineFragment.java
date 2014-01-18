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
public class VaccineFragment extends Fragment{

    private static VaccineFragment singletonVaccineFragment = null;

    public VaccineFragment(Context context){

    }

    public static synchronized VaccineFragment getInstance(Context context){
        if (singletonVaccineFragment == null) {
            singletonVaccineFragment = new VaccineFragment(context.getApplicationContext());
        }
        return singletonVaccineFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vaccine, container, false);
        return rootView;
    }

}
