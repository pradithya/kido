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
public class HomeFragment extends Fragment{

    private static HomeFragment singletonHomeFragment = null;

    public HomeFragment(Context context){

    }

    public static synchronized HomeFragment getInstance(Context context){
        if (singletonHomeFragment == null) {
            singletonHomeFragment = new HomeFragment(context.getApplicationContext());
        }
        return singletonHomeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }

}
