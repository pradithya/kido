package com.progrema.superbaby.ui.fragment.home;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.LinearLayout;

import com.progrema.superbaby.R;
import com.progrema.superbaby.adapter.timelinehistory.TimelineHistoryAdapter;
import com.progrema.superbaby.models.Diaper;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.ui.fragment.dialog.DiaperDialogFragment;
import com.progrema.superbaby.util.ActiveContext;
import com.progrema.superbaby.widget.customview.ObserveableListView;

import java.util.Calendar;

/**
 * Created by iqbalpakeh on 18/1/14.
 */
public class TimeLineLogFragment extends Fragment implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>, ObserveableListView.Callbacks
{

    public final static int REQUEST_DIAPER = 0;
    public final static int REQUEST_SLEEP = 1;
    public final static int REQUEST_NURSING = 2;

    public final static int RESULT_OK = 0;

    private static TimeLineLogFragment singletonTimeLineLogFragment = null;
    private Button buttonQuickSleep;
    private Button buttonQuickDiaper;
    private Button buttonQuickNursing;
    private TimelineHistoryAdapter mAdapter;
    private ObserveableListView historyList;

    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
    public static final int LOADER_ID = 3;

    private boolean isScrollUp;

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

    public static synchronized TimeLineLogFragment getInstance()
    {
        if (singletonTimeLineLogFragment == null)
        {
            singletonTimeLineLogFragment = new TimeLineLogFragment();
        }
        return singletonTimeLineLogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // inflate fragment layout
        View rootView = inflater.inflate(R.layout.fragment_timeline, container, false);

        // set onClickListener to button
        buttonQuickSleep = (Button) rootView.findViewById(R.id.quick_button_sleep);
        buttonQuickSleep.setOnClickListener(this);

        // set onClickListener to button
        buttonQuickNursing = (Button) rootView.findViewById(R.id.quick_button_nursing);
        buttonQuickNursing.setOnClickListener(this);

        // set onClickListener to button
        buttonQuickDiaper = (Button) rootView.findViewById(R.id.quick_button_diaper);
        buttonQuickDiaper.setOnClickListener(this);

        // set adapter to list view
        historyList = (ObserveableListView) rootView.findViewById(R.id.activity_list);
        historyList.setCallbacks(this);
        mAdapter = new TimelineHistoryAdapter(getActivity(), null, 0);
        historyList.setAdapter(mAdapter);

        // prepare loader
        mCallbacks = this;
        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_ID, null, mCallbacks);

        return rootView;

    }

    @Override
    public void onScrollChanged(int scrollY, int oldScrollY)
    {
        if(scrollY > oldScrollY)
        {
            isScrollUp = true;
        }
        else
        {
            isScrollUp = false;
        }
    }

    @Override
    public void onDownMotionEvent()
    {
        LinearLayout overlayLayout = (LinearLayout) getActivity().findViewById(R.id.timeline_quick_button);
        ViewPropertyAnimator animator = overlayLayout.animate();

        if(isScrollUp)
        {
            // hide button
            animator.cancel();
            animator.translationY(0).setDuration(200).start();
        }
        else
        {
            // show button
            animator.cancel();
            animator.translationY(overlayLayout.getHeight()).setDuration(200).start();
        }
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

    private void handleQuickSleep()
    {
        // Jump to stopwatch fragment
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        StopwatchFragment frStopWatch = StopwatchFragment.getInstance();

        /** inform the stopwatch to start counting for sleep*/
        Bundle bundle = new Bundle();
        bundle.putString(ACTIVITY_TRIGGER_KEY, Trigger.SLEEP.getTitle());
        frStopWatch.setArguments(bundle);

        fragmentTransaction.replace(R.id.home_activity_container, frStopWatch);
        fragmentTransaction.commit();
    }

    private void handleQuickDiaper()
    {
        // Jump to stopwatch fragment
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        DiaperDialogFragment diaperChoiceBox = DiaperDialogFragment.getInstance();
        diaperChoiceBox.setTargetFragment(this,REQUEST_DIAPER);

        diaperChoiceBox.show(fragmentTransaction, "dialog");
    }

    private void handleQuickNursing()
    {
        // Jump to stopwatch fragment
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        StopwatchFragment frStopWatch = StopwatchFragment.getInstance();

        /** inform the stopwatch to start counting for sleep*/
        Bundle bundle = new Bundle();
        bundle.putString(ACTIVITY_TRIGGER_KEY, Trigger.SLEEP.getTitle());
        frStopWatch.setArguments(bundle);

        fragmentTransaction.replace(R.id.home_activity_container, StopwatchFragment.getInstance());
        fragmentTransaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_DIAPER:
                    Calendar currentTime = Calendar.getInstance();
                    Bundle recData = data.getExtras();
                    String diaperType = (String) recData.get(Diaper.DIAPER_TYPE_KEY);
                    Diaper addedActivity = new Diaper();
                    addedActivity.setBabyID(ActiveContext.getActiveBaby(getActivity()).getID());
                    addedActivity.setTimeStamp(String.valueOf(currentTime.getTimeInMillis()));
                    addedActivity.setType(Diaper.DiaperType.valueOf(diaperType));
                    addedActivity.insert(getActivity());
                    getLoaderManager().restartLoader(LOADER_ID, null, this);
                    break;
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader (int i, Bundle bundle){
        String[] args = {String.valueOf(ActiveContext.getActiveBaby(getActivity()).getID())};
        CursorLoader cl = new CursorLoader(getActivity(), BabyLogContract.Activity.CONTENT_URI,
                BabyLogContract.Activity.Query.PROJECTION,
                BabyLogContract.BABY_SELECTION_ARG,
                args,
                BabyLogContract.Activity._ID);
        return cl;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cl, Cursor cursor){
        if (cursor.getCount() > 0)
        {
            /** show last inserted row */
            cursor.moveToFirst();
            mAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cl){
       mAdapter.swapCursor(null);
    }
}
