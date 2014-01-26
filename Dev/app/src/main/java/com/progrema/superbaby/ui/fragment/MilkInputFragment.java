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
public class MilkInputFragment extends Fragment {

    private static MilkInputFragment singletonMilkInputFragment = null;

    public MilkInputFragment(Context context){}

    public static synchronized MilkInputFragment getInstance(Context context){
        if (singletonMilkInputFragment == null) {
            singletonMilkInputFragment = new MilkInputFragment(context.getApplicationContext());
        }
        return singletonMilkInputFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_milk_input, container, false);
        return rootView;
    }

}
