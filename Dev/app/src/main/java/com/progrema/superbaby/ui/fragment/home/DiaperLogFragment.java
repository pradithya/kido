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
public class DiaperLogFragment extends Fragment
{

    private static DiaperLogFragment singletonDiaperLogFragment = null;

    public static synchronized DiaperLogFragment getInstance()
    {
        if (singletonDiaperLogFragment == null)
        {
            singletonDiaperLogFragment = new DiaperLogFragment();
        }
        return singletonDiaperLogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_diaper_log, container, false);
        return rootView;
    }

}
