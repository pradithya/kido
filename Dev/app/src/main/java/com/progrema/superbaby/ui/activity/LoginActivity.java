package com.progrema.superbaby.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.progrema.superbaby.R;
import com.progrema.superbaby.ui.fragment.login.BabyInputFragment;
import com.progrema.superbaby.ui.fragment.login.SplashScreenFragment;

public class LoginActivity extends FragmentActivity {
    public static final String PREF_LOGIN = "PrefLogin";
    public static final String PREF_SKIP_LOGIN = "SkipLogin";
    public static final String INTENT_NEW_BABY_REQUEST = "IntentNewBabyRequest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // decide whether we skip login or not
        SharedPreferences setting = getSharedPreferences(PREF_LOGIN, 0);
        boolean isSkipLogin = setting.getBoolean(PREF_SKIP_LOGIN, false);
        boolean newBabyRequest = getIntent().getBooleanExtra(INTENT_NEW_BABY_REQUEST, false);

        if (isSkipLogin && !newBabyRequest) {
            // Go to HomeActivity
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else if (isSkipLogin && newBabyRequest) {
            // move to baby input fragment
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.login_activity_container, BabyInputFragment.getInstance());
            fragmentTransaction.commit();
        } else {
            // Go to LoginPage
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment module = SplashScreenFragment.getInstance();
            fragmentManager.beginTransaction().replace(R.id.login_activity_container, module).commit();
        }
    }
}
