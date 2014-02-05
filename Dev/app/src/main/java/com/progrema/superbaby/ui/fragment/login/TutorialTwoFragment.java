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
public class TutorialTwoFragment extends Fragment implements View.OnClickListener
{
    private static TutorialTwoFragment singletonTutorialTwoFragment = null;
    private Button nextButton;

    public static synchronized TutorialTwoFragment getInstance()
    {
        if (singletonTutorialTwoFragment == null)
        {
            singletonTutorialTwoFragment = new TutorialTwoFragment();
        }
        return singletonTutorialTwoFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_tutorial_two, container, false);

        nextButton = (Button) rootView.findViewById(R.id.fragment_tutorial_two_button_next);
        nextButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        // Go to HomeActivity
        startActivity(new Intent(getActivity(), HomeActivity.class));
    }
}
