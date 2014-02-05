package com.progrema.superbaby.ui.fragment.home;

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
public class DiaperInputFragment extends Fragment
{

    private static DiaperInputFragment singletonDiaperInputFragment = null;

    public static synchronized DiaperInputFragment getInstance(Context context)
    {
        if (singletonDiaperInputFragment == null)
        {
            singletonDiaperInputFragment = new DiaperInputFragment();
        }
        return singletonDiaperInputFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_diaper_input, container, false);
        return rootView;
    }

}
