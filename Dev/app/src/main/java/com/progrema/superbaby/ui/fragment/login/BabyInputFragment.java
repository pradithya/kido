package com.progrema.superbaby.ui.fragment.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.progrema.superbaby.R;
import com.progrema.superbaby.ui.activity.HomeActivity;

/**
 * Created by iqbalpakeh on 5/2/14.
 */
public class BabyInputFragment extends Fragment implements View.OnClickListener
{
    private static BabyInputFragment singletonBabyInputFragment = null;
    private Button nextButton;

    public static synchronized BabyInputFragment getInstance()
    {
        if (singletonBabyInputFragment == null)
        {
            singletonBabyInputFragment = new BabyInputFragment();
        }
        return singletonBabyInputFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_baby_input_login, container, false);

        nextButton = (Button) rootView.findViewById(R.id.fragment_baby_input_button_done);
        nextButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        // Go to HomeActivity
        startActivity(new Intent(getActivity(), HomeActivity.class));
    }

}
