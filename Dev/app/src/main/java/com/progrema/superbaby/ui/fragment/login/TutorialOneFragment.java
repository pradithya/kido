package com.progrema.superbaby.ui.fragment.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.progrema.superbaby.R;

/**
 * Created by iqbalpakeh on 5/2/14.
 */
public class TutorialOneFragment extends Fragment implements View.OnClickListener
{
    private static TutorialOneFragment singletonTutorialOneFragment = null;
    private Button nextButton;

    public static synchronized TutorialOneFragment getInstance()
    {
        if (singletonTutorialOneFragment == null)
        {
            singletonTutorialOneFragment = new TutorialOneFragment();
        }
        return singletonTutorialOneFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_tutorial_one, container, false);

        nextButton = (Button) rootView.findViewById(R.id.fragment_tutorial_one_button_next);
        nextButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        // move to login fragment
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.login_activity_container, TutorialTwoFragment.getInstance());
        fragmentTransaction.commit();
    }

}
