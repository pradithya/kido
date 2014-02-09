package com.progrema.superbaby.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
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
    public static final String PREF_SKIP_LOGIN = "skipLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // decide whether we skip login or not
        SharedPreferences setting = getPreferences(0);
        boolean isSkipLogin = setting.getBoolean(PREF_SKIP_LOGIN, false);

        if (isSkipLogin)
        {
            // Go to HomeActivity
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
        else
        {
            // Go to LoginPage
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment module = SplashScreenFragment.getInstance();
            fragmentManager.beginTransaction().replace(R.id.login_activity_container, module).commit();
        }
    }
}
