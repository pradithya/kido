package com.progrema.superbaby.ui.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import com.progrema.superbaby.R;
import com.progrema.superbaby.ui.fragment.*;

public class HomeActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks
{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

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

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position)
    {

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        // fragment module
        Fragment module = null;

        switch(position){
            case POSITION_HOME_FRAGMENT:
                module = TimelineLogFragment.getInstance(this);
                break;
            case POSITION_MILK_FRAGMENT:
                module = MilkLogFragment.getInstance(this);
                break;
            case POSITION_DIAPER_FRAGMENT:
                module = DiaperLogFragment.getInstance(this);
                break;
            case POSITION_SLEEP_FRAGMENT:
                module = SleepLogFragment.getInstance(this);
                break;
        }

        if(module != null)
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
        if (!mNavigationDrawerFragment.isDrawerOpen())
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
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
