package com.progrema.superbaby.ui.fragment;

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
public class FoodInputFragment extends Fragment {

    public static final String INTENT = "intent_food_input_fragment";

    private static FoodInputFragment singletonFoodInputFragment = null;

    public FoodInputFragment(Context context){

    }

    public static synchronized FoodInputFragment getInstance(Context context){
        if (singletonFoodInputFragment == null) {
            singletonFoodInputFragment = new FoodInputFragment(context.getApplicationContext());
        }
        return singletonFoodInputFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food_input, container, false);
        return rootView;
    }

}
