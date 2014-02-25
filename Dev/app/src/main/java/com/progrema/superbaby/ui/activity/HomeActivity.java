package com.progrema.superbaby.ui.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.LinearLayout;

import com.progrema.superbaby.R;
import com.progrema.superbaby.models.Diaper;
import com.progrema.superbaby.ui.fragment.dialog.DiaperDialogFragment;
import com.progrema.superbaby.ui.fragment.dialog.NursingDialogFragment;
import com.progrema.superbaby.ui.fragment.home.DiaperLogFragment;
import com.progrema.superbaby.ui.fragment.home.NavigationFragment;
import com.progrema.superbaby.ui.fragment.home.NursingLogFragment;
import com.progrema.superbaby.ui.fragment.home.SleepLogFragment;
import com.progrema.superbaby.ui.fragment.home.StopwatchFragment;
import com.progrema.superbaby.ui.fragment.home.TimeLineLogFragment;
import com.progrema.superbaby.util.ActiveContext;
import com.progrema.superbaby.widget.customview.ObserveAbleListView;

import java.util.Calendar;

public class HomeActivity extends FragmentActivity
        implements NavigationFragment.NavigationDrawerCallbacks, View.OnClickListener,
        ObserveAbleListView.Callbacks, NursingDialogFragment.Callbacks, DiaperDialogFragment.Callbacks
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

    public final static int REQUEST_DIAPER = 0;
    public final static int REQUEST_SLEEP = 1;
    public final static int REQUEST_NURSING = 2;
    public final static int RESULT_OK = 0;

    private Button buttonQuickSleep;
    private Button buttonQuickDiaper;
    private Button buttonQuickNursing;

    private ObserveAbleListView historyList;

    public final static String ACTIVITY_TRIGGER_KEY = "trigger";

    public enum Trigger
    {
        SLEEP("sleep"),
        NURSING("nursing"),
        DIAPER("diaper");

        private String title;

        Trigger(String title)
        {
            this.title = title;
        }

        public String getTitle()
        {
            return this.title;
        }
    }

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
    public View onCreateView(String name, Context context, AttributeSet attrs)
    {
        // find quick action button in new inflated view
        buttonQuickSleep = (Button) findViewById(R.id.quick_button_sleep);
        buttonQuickNursing = (Button) findViewById(R.id.quick_button_nursing);
        buttonQuickDiaper = (Button) findViewById(R.id.quick_button_diaper);
        historyList = (ObserveAbleListView) findViewById(R.id.activity_list);

        if (buttonQuickDiaper != null && buttonQuickNursing != null
                && buttonQuickSleep != null && historyList != null)
        {
            // if it's not there don't attach any callback
            buttonQuickSleep.setOnClickListener(this);
            buttonQuickNursing.setOnClickListener(this);
            buttonQuickDiaper.setOnClickListener(this);
            historyList.setCallbacks(this);
        }
        return super.onCreateView(name, context, attrs);
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
                module = NursingLogFragment.getInstance();
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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId())
        {
            case R.id.action_settings:
                return true;

            case R.id.action_new_baby:
                handleNewBaby();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.quick_button_sleep:
                handleQuickSleep();
                break;

            case R.id.quick_button_nursing:
                handleQuickNursing();
                break;

            case R.id.quick_button_diaper:
                handleQuickDiaper();
                break;
        }
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

    private void handleQuickSleep()
    {
        // Jump to stopwatch fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        // Inform the stopwatch to start counting for sleep
        Bundle bundle = new Bundle();
        bundle.putString(ACTIVITY_TRIGGER_KEY, Trigger.SLEEP.getTitle());
        StopwatchFragment frStopWatch = StopwatchFragment.getInstance();
        frStopWatch.setArguments(bundle);

        fragmentTransaction.replace(R.id.home_activity_container, frStopWatch);
        fragmentTransaction.commit();
    }

    private void handleQuickDiaper()
    {
        // Jump to stopwatch fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DiaperDialogFragment diaperChoiceBox = DiaperDialogFragment.getInstance();
        TimeLineLogFragment timeLine = TimeLineLogFragment.getInstance();
        diaperChoiceBox.setCallbacks(this);
        diaperChoiceBox.show(fragmentTransaction, "diaper_dialog");
    }

    private void handleQuickNursing()
    {
        // Jump to stopwatch fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        NursingDialogFragment nursingChoiceBox = NursingDialogFragment.getInstance();
        TimeLineLogFragment timeLine = TimeLineLogFragment.getInstance();
        nursingChoiceBox.setCallbacks(this);
        nursingChoiceBox.show(fragmentTransaction, "nursing_dialog");
    }

    @Override
    public void onScrollDown()
    {
        LinearLayout overlayLayout = (LinearLayout) findViewById(R.id.timeline_quick_button);
        ViewPropertyAnimator animator = overlayLayout.animate();
        animator.cancel();
        animator.translationY(overlayLayout.getHeight()).setDuration(175).start();
    }

    @Override
    public void onScrollUp()
    {
        LinearLayout overlayLayout = (LinearLayout) findViewById(R.id.timeline_quick_button);
        ViewPropertyAnimator animator = overlayLayout.animate();
        animator.cancel();
        animator.translationY(0).setDuration(175).start();
    }

    @Override
    public void onNursingChoiceSelected(int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            // get input data passed from dialog
            Bundle bundle = data.getExtras();
            // add extra key to notify stopwatch which activity triggers it
            bundle.putString(HomeActivity.ACTIVITY_TRIGGER_KEY, HomeActivity.Trigger.NURSING.getTitle());
            StopwatchFragment frStopWatch = StopwatchFragment.getInstance();
            frStopWatch.setArguments(bundle);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.home_activity_container, frStopWatch);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onDiaperChoiceSelected(int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            Calendar currentTime = Calendar.getInstance();
            Bundle recData = data.getExtras();
            String diaperType = (String) recData.get(Diaper.DIAPER_TYPE_KEY);

            Diaper diaper = new Diaper();
            diaper.setBabyID(ActiveContext.getActiveBaby(this).getID());
            diaper.setTimeStamp(String.valueOf(currentTime.getTimeInMillis()));
            diaper.setType(Diaper.DiaperType.valueOf(diaperType));
            diaper.insert(this);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.home_activity_container, TimeLineLogFragment.getInstance());
            fragmentTransaction.commit();
        }
    }
}
