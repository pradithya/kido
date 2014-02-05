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
public class BabyInputFragment extends Fragment
{

    private static BabyInputFragment singletonBabyInputFragment = null;

    public static synchronized BabyInputFragment getInstance()
    {
        if (singletonBabyInputFragment == null)
        {
            singletonBabyInputFragment = new BabyInputFragment();
        }
        return singletonBabyInputFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_baby_input, container, false);
        return rootView;
    }

}
