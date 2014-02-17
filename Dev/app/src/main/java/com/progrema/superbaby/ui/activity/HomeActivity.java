package com.progrema.superbaby.ui.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.progrema.superbaby.R;
import com.progrema.superbaby.ui.fragment.home.DiaperLogFragment;
import com.progrema.superbaby.ui.fragment.home.MilkLogFragment;
import com.progrema.superbaby.ui.fragment.home.NavigationFragment;
import com.progrema.superbaby.ui.fragment.home.SleepLogFragment;
import com.progrema.superbaby.ui.fragment.home.TimeLineLogFragment;

public class HomeActivity extends FragmentActivity
        implements NavigationFragment.NavigationDrawerCallbacks
{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationFragment mNavigationFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    /**
     * Used to locate the fragment position
     */
    private static final int POSITION_HOME_FRAGMENT = 0;
    private static final int POSITION_MILK_FRAGMENT = 1;
    private static final int POSITION_DIAPER_FRAGMENT = 2;
    private static final int POSITION_SLEEP_FRAGMENT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mNavigationFragment = (NavigationFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position, int calibration)
    {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        // fragment module
        Fragment module = null;

        switch (position - calibration)
        {
            case POSITION_HOME_FRAGMENT:
                module = TimeLineLogFragment.getInstance();
                break;
            case POSITION_MILK_FRAGMENT:
                module = MilkLogFragment.getInstance();
                break;
            case POSITION_DIAPER_FRAGMENT:
                module = DiaperLogFragment.getInstance();
                break;
            case POSITION_SLEEP_FRAGMENT:
                module = SleepLogFragment.getInstance();
                break;
        }

        if (module != null)
        {
            fragmentManager.beginTransaction().replace(R.id.home_activity_container, module).commit();
        }

    }

    public void restoreActionBar()
    {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if (!mNavigationFragment.isDrawerOpen())
        {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.home, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId())
        {
            case R.id.action_example:
                return true;

            case R.id.action_settings:
                return true;

            case R.id.action_new_baby:
                handleNewBaby();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        Intent goBackToAndroidLauncher = new Intent(Intent.ACTION_MAIN);
        goBackToAndroidLauncher.addCategory(Intent.CATEGORY_HOME);
        goBackToAndroidLauncher.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goBackToAndroidLauncher);
    }

    private void handleNewBaby()
    {
        // Go to Baby input fragment in LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(LoginActivity.INTENT_NEW_BABY_REQUEST, true);
        startActivity(intent);
        finish();
    }
}
