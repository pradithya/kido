package com.progrema.superbaby.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import com.progrema.superbaby.R;
import com.progrema.superbaby.ui.fragment.login.SplashScreenFragment;

/**
 * Created by iqbalpakeh on 5/2/14.
 */
public class LoginActivity extends FragmentActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment module = SplashScreenFragment.getInstance();
        fragmentManager.beginTransaction().replace(R.id.login_activity_container, module).commit();

    }

}
