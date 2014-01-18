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
public class MilkFragment extends Fragment{

    private static MilkFragment singletonMilkFragment = null;

    public MilkFragment(Context context){

    }

    public static synchronized MilkFragment getInstance(Context context){
        if (singletonMilkFragment == null) {
            singletonMilkFragment = new MilkFragment(context.getApplicationContext());
        }
        return singletonMilkFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_milk, container, false);
        return rootView;
    }

}
