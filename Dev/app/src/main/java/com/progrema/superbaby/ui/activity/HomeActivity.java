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
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.progrema.superbaby.R;
import com.progrema.superbaby.adapter.ActionBarDropDownAdapter;
import com.progrema.superbaby.models.Diaper;
import com.progrema.superbaby.models.Measurement;
import com.progrema.superbaby.ui.fragment.dialog.DiaperDialog;
import com.progrema.superbaby.ui.fragment.dialog.MeasurementDialog;
import com.progrema.superbaby.ui.fragment.dialog.NursingDialog;
import com.progrema.superbaby.ui.fragment.home.DiaperFragment;
import com.progrema.superbaby.ui.fragment.home.MeasurementFragment;
import com.progrema.superbaby.ui.fragment.home.NavigationFragment;
import com.progrema.superbaby.ui.fragment.home.NursingFragment;
import com.progrema.superbaby.ui.fragment.home.SleepFragment;
import com.progrema.superbaby.ui.fragment.home.StopwatchFragment;
import com.progrema.superbaby.ui.fragment.home.TimeLineFragment;
import com.progrema.superbaby.util.ActiveContext;
import com.progrema.superbaby.widget.customview.ObserveableListView;

import java.util.Calendar;

public class HomeActivity extends FragmentActivity
        implements NavigationFragment.NavigationDrawerCallbacks, View.OnClickListener,
        ObserveableListView.Callbacks, NursingDialog.Callbacks, DiaperDialog.Callbacks,
        MeasurementDialog.Callbacks, ActionBar.OnNavigationListener {

    public final static int RESULT_OK = 0;
    public final static String ACTIVITY_TRIGGER_KEY = "trigger";

    /**
     * Used to locate the fragment position
     */
    private static final int POSITION_HOME_FRAGMENT = 0;
    private static final int POSITION_MILK_FRAGMENT = 1;
    private static final int POSITION_DIAPER_FRAGMENT = 2;
    private static final int POSITION_SLEEP_FRAGMENT = 3;
    private static final int POSITION_MEASUREMENT_FRAGMENT = 4;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationFragment mNavigationFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private String mTitle;
    private RelativeLayout rlSleepButton;
    private RelativeLayout rlDiaperButton;
    private RelativeLayout rlNursingButton;
    private RelativeLayout rlMeasurementButton;
    private ObserveableListView historyList;
    private ActionBarDropDownAdapter mSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mNavigationFragment = (NavigationFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle().toString();

        // Set up the drawer.
        mNavigationFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        String[] items = getResources().getStringArray(R.array.time_selection);
        mSpinnerAdapter = new ActionBarDropDownAdapter(this, R.layout.action_bar_spinner,
                                                                  items,"");

        getActionBar().setListNavigationCallbacks(mSpinnerAdapter, this);

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
        // find quick action button in new inflated view
        rlSleepButton = (RelativeLayout) findViewById(R.id.quick_button_sleep);
        rlNursingButton = (RelativeLayout) findViewById(R.id.quick_button_nursing);
        rlDiaperButton = (RelativeLayout) findViewById(R.id.quick_button_diaper);
        rlMeasurementButton = (RelativeLayout) findViewById(R.id.quick_button_measurement);
        historyList = (ObserveableListView) findViewById(R.id.activity_list);

        if (rlDiaperButton != null && rlNursingButton != null
                && rlSleepButton != null && historyList != null) {
            // if it's not there don't attach any callback
            rlSleepButton.setOnClickListener(this);
            rlNursingButton.setOnClickListener(this);
            rlDiaperButton.setOnClickListener(this);
            rlMeasurementButton.setOnClickListener(this);
            historyList.setCallbacks(this);
        }
        return super.onCreateView(name, context, attrs);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position, int calibration) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        // fragment module
        Fragment module = null;

        switch (position - calibration) {
            case POSITION_HOME_FRAGMENT:
                module = TimeLineFragment.getInstance();
                mTitle = getString(R.string.title_timeline_fragment);
                break;
            case POSITION_MILK_FRAGMENT:
                module = NursingFragment.getInstance();
                mTitle = getString(R.string.title_nursing_fragment);
                break;
            case POSITION_DIAPER_FRAGMENT:
                module = DiaperFragment.getInstance();
                mTitle = getString(R.string.title_diaper_fragment);
                break;
            case POSITION_SLEEP_FRAGMENT:
                module = SleepFragment.getInstance();
                mTitle = getString(R.string.title_sleep_fragment);
                break;
            case POSITION_MEASUREMENT_FRAGMENT:
                module = MeasurementFragment.getInstance();
                mTitle = getString(R.string.title_measure_fragment);
        }

        if (module != null) {
            fragmentManager.beginTransaction().replace(R.id.home_activity_container, module).commit();
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setDisplayShowTitleEnabled(false);
        mSpinnerAdapter.setTitle(mTitle);
        //actionBar.setTitle(mTitle);


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
            case R.id.quick_button_sleep:
                handleQuickSleep();
                break;

            case R.id.quick_button_nursing:
                handleQuickNursing();
                break;

            case R.id.quick_button_diaper:
                handleQuickDiaper();
                break;

            case R.id.quick_button_measurement:
                handleQuickMeasurement();
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

    private void handleQuickSleep() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        // Inform the stopwatch to start counting for sleep
        Bundle bundle = new Bundle();
        bundle.putString(ACTIVITY_TRIGGER_KEY, Trigger.SLEEP.getTitle());
        StopwatchFragment frStopWatch = StopwatchFragment.getInstance();
        frStopWatch.setArguments(bundle);

        fragmentTransaction.replace(R.id.home_activity_container, frStopWatch);
        fragmentTransaction.commit();
    }

    private void handleQuickDiaper() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DiaperDialog diaperChoiceBox = DiaperDialog.getInstance();
        diaperChoiceBox.setCallbacks(this);
        diaperChoiceBox.show(fragmentTransaction, "diaper_dialog");
    }

    private void handleQuickNursing() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        NursingDialog nursingChoiceBox = NursingDialog.getInstance();
        nursingChoiceBox.setCallbacks(this);
        nursingChoiceBox.show(fragmentTransaction, "nursing_dialog");
    }

    private void handleQuickMeasurement() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        MeasurementDialog measurementChoiceBox = MeasurementDialog.getInstance();
        measurementChoiceBox.setCallbacks(this);
        measurementChoiceBox.show(fragmentTransaction, "measurement_dialog");
    }

    @Override
    public void onScrollDown() {
        LinearLayout overlayLayout = (LinearLayout) findViewById(R.id.timeline_quick_button);
        ViewPropertyAnimator animator = overlayLayout.animate();
        animator.cancel();
        animator.translationY(overlayLayout.getHeight()).setDuration(175).start();
    }

    @Override
    public void onScrollUp() {
        LinearLayout overlayLayout = (LinearLayout) findViewById(R.id.timeline_quick_button);
        ViewPropertyAnimator animator = overlayLayout.animate();
        animator.cancel();
        animator.translationY(0).setDuration(175).start();
    }

    @Override
    public void onNursingChoiceSelected(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
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
    public void onDiaperChoiceSelected(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Calendar currentTime = Calendar.getInstance();
            Bundle recData = data.getExtras();
            String diaperType = (String) recData.get(Diaper.DIAPER_TYPE_KEY);

            Diaper diaper = new Diaper();
            diaper.setBabyID(ActiveContext.getActiveBaby(this).getID());
            diaper.setTimeStamp(String.valueOf(currentTime.getTimeInMillis()));
            diaper.setType(Diaper.DiaperType.valueOf(diaperType));
            diaper.insert(this);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.home_activity_container, TimeLineFragment.getInstance());
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onMeasurementChoiceSelected(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Calendar currentTime = Calendar.getInstance();
            Bundle recData = data.getExtras();

            Measurement measurement = new Measurement();
            measurement.setBabyID(ActiveContext.getActiveBaby(this).getID());
            measurement.setTimeStamp(String.valueOf(currentTime.getTimeInMillis()));
            measurement.setHeight(Float.valueOf(recData.getString(Measurement.HEIGHT_KEY)));
            measurement.setWeight(Float.valueOf(recData.getString(Measurement.WEIGHT_KEY)));
            measurement.insert(this);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.home_activity_container, TimeLineFragment.getInstance());
            fragmentTransaction.commit();
        }
    }

    public enum Trigger {
        SLEEP("Sleep"),
        NURSING("Nursing"),
        DIAPER("Diaper");

        private String title;

        Trigger(String title) {
            this.title = title;
        }

        public String getTitle() {
            return this.title;
        }
    }

    @Override
    public boolean onNavigationItemSelected(int arg0, long arg1){
        //TODO: change query of active fragment
        return true;
    }
}
