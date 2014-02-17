package com.progrema.superbaby.ui.fragment.home;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.progrema.superbaby.R;

/**
 * Created by iqbalpakeh on 20/1/14.
 */
public class NursingInputFragment extends Fragment
{

    private static NursingInputFragment singletonNursingInputFragment = null;

    public static synchronized NursingInputFragment getInstance()
    {
        if (singletonNursingInputFragment == null)
        {
            singletonNursingInputFragment = new NursingInputFragment();
        }
        return singletonNursingInputFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_nursing_input, container, false);
        return rootView;
    }

}
