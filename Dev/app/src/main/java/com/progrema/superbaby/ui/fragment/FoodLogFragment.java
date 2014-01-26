package com.progrema.superbaby.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.progrema.superbaby.R;

/**
 * Created by iqbalpakeh on 18/1/14.
 */
public class FoodLogFragment extends Fragment {

    private static FoodLogFragment singletonFoodLogFragment = null;

    public FoodLogFragment(Context context){}

    public static synchronized FoodLogFragment getInstance(Context context){
        if (singletonFoodLogFragment == null) {
            singletonFoodLogFragment = new FoodLogFragment(context.getApplicationContext());
        }
        return singletonFoodLogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food_log, container, false);
        return rootView;
    }

}
