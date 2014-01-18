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
public class FoodFragment extends Fragment {

    private static FoodFragment singletonFoodFragment = null;

    public FoodFragment(Context context){

    }

    public static synchronized FoodFragment getInstance(Context context){
        if (singletonFoodFragment == null) {
            singletonFoodFragment = new FoodFragment(context.getApplicationContext());
        }
        return singletonFoodFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food, container, false);
        return rootView;
    }

}
