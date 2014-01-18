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
public class DiaperFragment extends Fragment{

    private static DiaperFragment singletonDiaperFragment = null;

    public DiaperFragment(Context context){

    }

    public static synchronized DiaperFragment getInstance(Context context){
        if (singletonDiaperFragment == null) {
            singletonDiaperFragment = new DiaperFragment(context.getApplicationContext());
        }
        return singletonDiaperFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_diaper, container, false);
        return rootView;
    }

}
