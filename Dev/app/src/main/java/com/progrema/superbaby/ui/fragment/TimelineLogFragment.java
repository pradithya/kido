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
public class TimelineLogFragment extends Fragment{

    private static TimelineLogFragment singletonTimelineLogFragment = null;

    public TimelineLogFragment(Context context){}

    public static synchronized TimelineLogFragment getInstance(Context context){
        if (singletonTimelineLogFragment == null) {
            singletonTimelineLogFragment = new TimelineLogFragment(context.getApplicationContext());
        }
        return singletonTimelineLogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timeline, container, false);
        return rootView;
    }

}
