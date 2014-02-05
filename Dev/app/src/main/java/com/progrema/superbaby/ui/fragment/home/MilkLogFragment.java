package com.progrema.superbaby.ui.fragment.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.progrema.superbaby.R;

/**
 * Created by iqbalpakeh on 18/1/14.
 */
public class MilkLogFragment extends Fragment
{

    private static MilkLogFragment singletonMilkLogFragment = null;

    public static synchronized MilkLogFragment getInstance()
    {
        if (singletonMilkLogFragment == null)
        {
            singletonMilkLogFragment = new MilkLogFragment();
        }
        return singletonMilkLogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_milk_log, container, false);
        return rootView;
    }

}
