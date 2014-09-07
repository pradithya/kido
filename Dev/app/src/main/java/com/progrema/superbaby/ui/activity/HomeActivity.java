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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.progrema.superbaby.R;
import com.progrema.superbaby.adapter.actionbar.ActionBarDropDownAdapter;
import com.progrema.superbaby.models.ActivityDiaper;
import com.progrema.superbaby.models.ActivityMeasurement;
import com.progrema.superbaby.ui.fragment.dialog.DiaperDialog;
import com.progrema.superbaby.ui.fragment.dialog.MeasurementDialog;
import com.progrema.superbaby.ui.fragment.dialog.NursingDialog;
import com.progrema.superbaby.ui.fragment.history.DiaperFragment;
import com.progrema.superbaby.ui.fragment.history.MeasurementFragment;
import com.progrema.superbaby.ui.fragment.history.NavigationFragment;
import com.progrema.superbaby.ui.fragment.history.NursingFragment;
import com.progrema.superbaby.ui.fragment.history.SleepFragment;
import com.progrema.superbaby.ui.fragment.history.StopwatchFragment;
import com.progrema.superbaby.ui.fragment.history.TimelineFragment;
import com.progrema.superbaby.util.ActiveContext;
import com.progrema.superbaby.widget.customlistview.ObserveableListView;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends FragmentActivity
        implements NavigationFragment.NavigationDrawerCallbacks, View.OnClickListener,
        ObserveableListView.Callback, NursingDialog.Callback, DiaperDialog.Callback,
        MeasurementDialog.Callback, ActionBar.OnNavigationListener {

    public final static int RESULT_OK = 0;
    public final static String ACTIVITY_TRIGGER_KEY = "triggerKey";
    public final static String ACTIVITY_EDIT_KEY = "editKey";
    public final static String ACTIVITY_ENTRY_TAG_KEY = "entryTag";

    /**
     * Used to locate the fragment position
     */
    private static final int POSITION_HOME_FRAGMENT = 0;
    private static final int POSITION_NURSING_FRAGMENT = 1;
    private static final int POSITION_DIAPER_FRAGMENT = 2;
    private static final int POSITION_SLEEP_FRAGMENT = 3;
    private static final int POSITION_MEASUREMENT_FRAGMENT = 4;
    private static final int TIME_FILTER_POSITION_TODAY = 0;
    private static final int TIME_FILTER_POSITION_THIS_WEEK = 1;
    private static final int TIME_FILTER_POSITION_THIS_MONTH = 2;
    private static final int TIME_FILTER_POSITION_ALL = 3;
    private int currentFragment;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationFragment mNavigationFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private String title;
    private RelativeLayout sleepQuickButton;
    private RelativeLayout diaperQuickButton;
    private RelativeLayout nursingQuickButton;
    private RelativeLayout measurementQuickButton;
    private ObserveableListView historyList;
    private ActionBarDropDownAdapter spinnerAdapter;
    private Bundle previousQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mNavigationFragment = (NavigationFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        title = getTitle().toString();
        mNavigationFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        String[] items = getResources().getStringArray(R.array.time_selection);
        spinnerAdapter = new ActionBarDropDownAdapter(this, R.layout.action_bar_spinner, items, "");
        getActionBar().setListNavigationCallbacks(spinnerAdapter, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationFragment.isDrawerOpen()) {
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
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        sleepQuickButton = (RelativeLayout) findViewById(R.id.button_sleep);
        nursingQuickButton = (RelativeLayout) findViewById(R.id.button_nursing);
        diaperQuickButton = (RelativeLayout) findViewById(R.id.button_diaper);
        measurementQuickButton = (RelativeLayout) findViewById(R.id.button_measurement);
        historyList = (ObserveableListView) findViewById(R.id.activity_list);
        if (diaperQuickButton != null && nursingQuickButton != null
                && sleepQuickButton != null && historyList != null) {
            // if it's not there don't attach any callback
            sleepQuickButton.setOnClickListener(this);
            nursingQuickButton.setOnClickListener(this);
            diaperQuickButton.setOnClickListener(this);
            measurementQuickButton.setOnClickListener(this);
            historyList.setCallbacks(this);
        }
        return super.onCreateView(name, context, attrs);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position, int calibration) {
        currentFragment = position - calibration;
        switchFragment(currentFragment, previousQuery);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setDisplayShowTitleEnabled(false);
        spinnerAdapter.setTitleAndNotify(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_new_baby:
                handleNewBaby();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_sleep:
                handleQuickButtonSleep();
                break;
            case R.id.button_nursing:
                handleQuickButtonNursing();
                break;
            case R.id.button_diaper:
                handleQuickButtonDiaper();
                break;
            case R.id.button_measurement:
                handleQuickButtonMeasurement();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent goBackToAndroidLauncher = new Intent(Intent.ACTION_MAIN);
        goBackToAndroidLauncher.addCategory(Intent.CATEGORY_HOME);
        goBackToAndroidLauncher.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goBackToAndroidLauncher);
    }

    private void handleNewBaby() {
        // Go to Baby input fragment in LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(LoginActivity.INTENT_NEW_BABY_REQUEST, true);
        startActivity(intent);
        finish();
    }

    private void handleQuickButtonSleep() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // Inform the stopwatch to start counting for sleep
        Bundle bundle = new Bundle();
        bundle.putString(ACTIVITY_TRIGGER_KEY, Trigger.SLEEP.getTitle());
        StopwatchFragment frStopWatch = StopwatchFragment.getInstance();
        frStopWatch.setArguments(bundle);
        fragmentTransaction.replace(R.id.home_activity_container, frStopWatch);
        fragmentTransaction.commit();
    }

    private void handleQuickButtonDiaper() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DiaperDialog diaperChoiceBox = DiaperDialog.getInstance();
        diaperChoiceBox.setCallback(this);
        diaperChoiceBox.show(fragmentTransaction, "diaper_dialog");
    }

    private void handleQuickButtonNursing() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        NursingDialog nursingChoiceBox = NursingDialog.getInstance();
        nursingChoiceBox.setCallback(this);
        nursingChoiceBox.show(fragmentTransaction, "nursing_dialog");
    }

    private void handleQuickButtonMeasurement() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        MeasurementDialog measurementChoiceBox = MeasurementDialog.getInstance();
        measurementChoiceBox.setCallback(this);
        measurementChoiceBox.show(fragmentTransaction, "measurement_dialog");
    }

    @Override
    public void onScrollUp() {
        LinearLayout overlayLayout = (LinearLayout) findViewById(R.id.timeline_quick_button);
        ViewPropertyAnimator animator = overlayLayout.animate();
        animator.cancel();
        animator.translationY(overlayLayout.getHeight()).setDuration(175).start();
    }

    @Override
    public void onScrollDown() {
        LinearLayout overlayLayout = (LinearLayout) findViewById(R.id.timeline_quick_button);
        ViewPropertyAnimator animator = overlayLayout.animate();
        animator.cancel();
        animator.translationY(0).setDuration(175).start();
    }

    @Override
    public void onNursingChoiceSelected(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            bundle.putString(HomeActivity.ACTIVITY_TRIGGER_KEY, HomeActivity.Trigger.NURSING.getTitle());
            StopwatchFragment stopwatchFragment = StopwatchFragment.getInstance();
            stopwatchFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.home_activity_container, stopwatchFragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onDiaperChoiceSelected(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Calendar currentTime = Calendar.getInstance();
            Bundle recordData = data.getExtras();
            String diaperType = (String) recordData.get(ActivityDiaper.DIAPER_TYPE_KEY);
            ActivityDiaper activityDiaper = new ActivityDiaper();
            activityDiaper.setBabyID(ActiveContext.getActiveBaby(this).getActivityId());
            activityDiaper.setTimeStamp(String.valueOf(currentTime.getTimeInMillis()));
            activityDiaper.setType(ActivityDiaper.DiaperType.valueOf(diaperType));
            activityDiaper.insert(this);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.home_activity_container, DiaperFragment.getInstance());
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onMeasurementChoiceSelected(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Calendar currentTime = Calendar.getInstance();
            Bundle recordData = data.getExtras();
            ActivityMeasurement activityMeasurement = new ActivityMeasurement();
            activityMeasurement.setBabyID(ActiveContext.getActiveBaby(this).getActivityId());
            activityMeasurement.setTimeStamp(String.valueOf(currentTime.getTimeInMillis()));
            activityMeasurement.setHeight(Float.valueOf(recordData.getString(ActivityMeasurement.HEIGHT_KEY)));
            activityMeasurement.setWeight(Float.valueOf(recordData.getString(ActivityMeasurement.WEIGHT_KEY)));
            activityMeasurement.insert(this);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.home_activity_container, MeasurementFragment.getInstance());
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(int position, long itemId) {
        //TODO: change query of active fragment
        Bundle timeFilterBundler = createTimeFilter(position);
        previousQuery = timeFilterBundler;
        switchFragment(currentFragment, timeFilterBundler);
        return true;
    }

    private void switchFragment(int fragmentPosition, Bundle argument) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;
        switch (fragmentPosition) {
            case POSITION_HOME_FRAGMENT:
                fragment = TimelineFragment.getInstance();
                title = getString(R.string.title_timeline_fragment);
                break;
            case POSITION_NURSING_FRAGMENT:
                fragment = NursingFragment.getInstance();
                title = getString(R.string.title_nursing_fragment);
                break;
            case POSITION_DIAPER_FRAGMENT:
                fragment = DiaperFragment.getInstance();
                title = getString(R.string.title_diaper_fragment);
                break;
            case POSITION_SLEEP_FRAGMENT:
                fragment = SleepFragment.getInstance();
                title = getString(R.string.title_sleep_fragment);
                break;
            case POSITION_MEASUREMENT_FRAGMENT:
                fragment = MeasurementFragment.getInstance();
                title = getString(R.string.title_measure_fragment);
        }
        if (fragment != null) {
            fragment.setArguments(argument);
            fragmentManager.beginTransaction()
                    .replace(R.id.home_activity_container, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
    }

    private Bundle createTimeFilter(int position) {
        /**
         * as stated here: http://developer.android.com/reference/java/util/Calendar.html
         * 24:00:00 "belongs" to the following day.
         * That is, 23:59 on Dec 31, 1969 < 24:00 on Jan 1, 1970 < 24:01:00 on Jan 1, 1970
         * form a sequence of three consecutive minutes in time.
         */
        Calendar startCalendar = Calendar.getInstance();
        String endTime = String.valueOf(startCalendar.getTimeInMillis());
        String startTime;
        String timeFilterType = null;
        long oneWeekDuration = TimeUnit.DAYS.toMillis(7);
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        switch (position) {
            case TIME_FILTER_POSITION_TODAY:
                timeFilterType = TimeFilter.FILTER_TODAY.getTitle();
                break;
            case TIME_FILTER_POSITION_THIS_WEEK:
                startCalendar.setTimeInMillis(startCalendar.getTimeInMillis() - oneWeekDuration);
                timeFilterType = TimeFilter.FILTER_THIS_WEEK.getTitle();
                break;
            case TIME_FILTER_POSITION_THIS_MONTH:
                startCalendar.set(Calendar.DAY_OF_MONTH, 1);
                timeFilterType = TimeFilter.FILTER_THIS_MONTH.getTitle();
                break;
            case TIME_FILTER_POSITION_ALL:
                startCalendar.set(Calendar.YEAR, 1); //year 1?
                timeFilterType = TimeFilter.FILTER_ALL.getTitle();
                break;
        }
        startTime = String.valueOf(startCalendar.getTimeInMillis());
        Bundle timeFilterBundle = new Bundle();
        timeFilterBundle.putString(TimeFilter.START.getTitle(), startTime);
        timeFilterBundle.putString(TimeFilter.END.getTitle(), endTime);
        timeFilterBundle.putString(TimeFilter.FILTER_TYPE.getTitle(), timeFilterType);
        return timeFilterBundle;
    }

    public enum Trigger {
        SLEEP("ActivitySleep"),
        NURSING("ActivityNursing"),
        DIAPER("ActivityDiaper");
        private String title;

        Trigger(String title) {
            this.title = title;
        }

        public String getTitle() {
            return this.title;
        }
    }

    public enum TimeFilter {
        START("start"),
        END("end"),
        FILTER_TYPE("filter_type"),
        FILTER_TODAY("filter_today"),
        FILTER_THIS_WEEK("filter_this_week"),
        FILTER_THIS_MONTH("filter_this_month"),
        FILTER_ALL("filter_all");
        private String title;

        TimeFilter(String title) {
            this.title = title;
        }

        public String getTitle() {
            return this.title;
        }
    }
}
