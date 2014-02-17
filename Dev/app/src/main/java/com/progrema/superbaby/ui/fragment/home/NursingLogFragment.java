package com.progrema.superbaby.ui.fragment.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.LinearLayout;

import com.progrema.superbaby.R;

/**
 * Created by iqbalpakeh on 18/1/14.
 */
public class NursingLogFragment extends Fragment implements View.OnClickListener
{
    private static NursingLogFragment singletonNursingLogFragment = null;
    private boolean isDisplay = true;
    private Button testOverlay;

    public static synchronized NursingLogFragment getInstance()
    {
        if (singletonNursingLogFragment == null)
        {
            singletonNursingLogFragment = new NursingLogFragment();
        }
        return singletonNursingLogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_nursing_log, container, false);
        testOverlay = (Button) rootView.findViewById(R.id.overlay_button_test);
        testOverlay.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.overlay_button_test:
                handleButtonOverlay();
                break;
        }
    }

    private void handleButtonOverlay()
    {
        if(isDisplay)
        {
            animateOverlayLayout(false);
            isDisplay = false;
        }
        else
        {
            animateOverlayLayout(true);
            isDisplay = true;
        }
    }

    private void animateOverlayLayout(boolean visible)
    {
        LinearLayout overlayLayout = (LinearLayout) getActivity().findViewById(R.id.overlay_layout);
        ViewPropertyAnimator animator = overlayLayout.animate();
        animator.translationY(visible ? 0 : overlayLayout.getHeight())
                .setDuration(250)
                .start();
    }
}
