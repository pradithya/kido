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
import android.widget.TextView;

import com.progrema.superbaby.R;
import com.progrema.superbaby.adapter.timelinehistory.TimeLineHistoryAdapter;
import com.progrema.superbaby.models.Baby;
import com.progrema.superbaby.models.Diaper;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.ui.fragment.dialog.DiaperDialogFragment;
import com.progrema.superbaby.ui.fragment.dialog.NursingDialogFragment;
import com.progrema.superbaby.util.ActiveContext;
import com.progrema.superbaby.widget.customview.ObserveAbleListView;

import java.util.Calendar;

/**
 * Created by iqbalpakeh on 18/1/14.
 */
public class TimeLineLogFragment extends Fragment implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>, ObserveAbleListView.Callbacks
{
    public final static int REQUEST_DIAPER = 0;
    public final static int REQUEST_SLEEP = 1;
    public final static int REQUEST_NURSING = 2;
    public final static int RESULT_OK = 0;
    public static final int LOADER_ID = 3;
    public final static String ACTIVITY_TRIGGER_KEY = "trigger";
    public static LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
    private Button buttonQuickSleep;
    private Button buttonQuickDiaper;
    private Button buttonQuickNursing;
    private TimeLineHistoryAdapter mAdapter;
    private ObserveAbleListView historyList;
    private TextView headerBabyName;
    private TextView headerBabyAge;
    private TextView headerBabySex;
    private TextView headerLastNursing;
    private TextView headerLastSleep;
    private TextView headerLastDiaper;

    public static TimeLineLogFragment getInstance()
    {
        return new TimeLineLogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // inflate fragment layout
        View rootView = inflater.inflate(R.layout.fragment_timeline, container, false);

        // get ui object
        buttonQuickSleep = (Button) rootView.findViewById(R.id.quick_button_sleep);
        buttonQuickNursing = (Button) rootView.findViewById(R.id.quick_button_nursing);
        buttonQuickDiaper = (Button) rootView.findViewById(R.id.quick_button_diaper);
        headerBabyName = (TextView) rootView.findViewById(R.id.timeline_header_baby_name);
        headerBabyAge = (TextView) rootView.findViewById(R.id.timeline_header_baby_age);
        headerBabySex = (TextView) rootView.findViewById(R.id.timeline_header_baby_sex);
        headerLastNursing = (TextView) rootView.findViewById(R.id.timeline_header_last_nursing);
        headerLastSleep = (TextView) rootView.findViewById(R.id.timeline_header_last_sleep);
        headerLastDiaper = (TextView) rootView.findViewById(R.id.timeline_header_last_diaper);

        // set onClickListener to button
        buttonQuickSleep.setOnClickListener(this);
        buttonQuickNursing.setOnClickListener(this);
        buttonQuickDiaper.setOnClickListener(this);

        // set adapter to list view
        historyList = (ObserveAbleListView) rootView.findViewById(R.id.activity_list);
        historyList.setCallbacks(this);
        mAdapter = new TimeLineHistoryAdapter(getActivity(), null, 0);
        historyList.setAdapter(mAdapter);

        // prepare loader
        mCallbacks = this;
        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_ID, null, mCallbacks);

        return rootView;
    }

    @Override
    public void onScrollDown()
    {
        LinearLayout overlayLayout = (LinearLayout) getActivity().findViewById(R.id.timeline_quick_button);
        ViewPropertyAnimator animator = overlayLayout.animate();
        animator.cancel();
        animator.translationY(overlayLayout.getHeight()).setDuration(175).start();
    }

    @Override
    public void onScrollUp()
    {
        LinearLayout overlayLayout = (LinearLayout) getActivity().findViewById(R.id.timeline_quick_button);
        ViewPropertyAnimator animator = overlayLayout.animate();
        animator.cancel();
        animator.translationY(0).setDuration(175).start();
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
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        DiaperDialogFragment diaperChoiceBox = DiaperDialogFragment.getInstance();
        diaperChoiceBox.setTargetFragment(this, REQUEST_DIAPER);

        diaperChoiceBox.show(fragmentTransaction, "diaper_dialog");
    }

    private void handleQuickNursing()
    {
        // Jump to stopwatch fragment
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        NursingDialogFragment nursingChoiceBox = NursingDialogFragment.getInstance();
        nursingChoiceBox.setTargetFragment(this, REQUEST_NURSING);

        nursingChoiceBox.show(fragmentTransaction, "nursing_dialog");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case REQUEST_DIAPER:
                    Calendar currentTime = Calendar.getInstance();
                    Bundle recData = data.getExtras();
                    String diaperType = (String) recData.get(Diaper.DIAPER_TYPE_KEY);

                    Diaper diaper = new Diaper();
                    diaper.setBabyID(ActiveContext.getActiveBaby(getActivity()).getID());
                    diaper.setTimeStamp(String.valueOf(currentTime.getTimeInMillis()));
                    diaper.setType(Diaper.DiaperType.valueOf(diaperType));
                    diaper.insert(getActivity());
                    break;

                case REQUEST_NURSING:
                    // get input data passed from dialog
                    Bundle bundle = data.getExtras();
                    // add extra key to notify stopwatch which activity triggers it
                    bundle.putString(ACTIVITY_TRIGGER_KEY, Trigger.NURSING.getTitle());
                    StopwatchFragment frStopWatch = StopwatchFragment.getInstance();
                    frStopWatch.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.home_activity_container, frStopWatch);
                    fragmentTransaction.commit();
                    break;

            }
            getLoaderManager().restartLoader(LOADER_ID, null, mCallbacks);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Baby baby = ActiveContext.getActiveBaby(getActivity());
        headerBabyName.setText(baby.getName());
        headerBabyAge.setText(baby.getBirthdayInString()); //TODO: create getAge() function!!
        headerBabySex.setText(baby.getSex().getTitle());
        headerLastNursing.setText(queryLastNursing(String.valueOf(baby.getID())));
        headerLastSleep.setText(queryLastSleep(String.valueOf(baby.getID())));
        headerLastDiaper.setText(queryLastDiaper(String.valueOf(baby.getID())));
    }

    private String queryLastNursing(String babyId)
    {
        String[] selectionArgument = {babyId};
        String[] projection = {BabyLogContract.Nursing.TIMESTAMP};
        try
        {
            Cursor cursor = getActivity().getContentResolver().query(
                    BabyLogContract.Nursing.MAX_TIMESTAMP,
                    projection,
                    null,
                    selectionArgument,
                    null);
            cursor.moveToFirst();
            return cursor.getString(0);
        }
        catch (Exception e)
        {
            // do nothing
        }
        return "No activity yet";
    }

    private String queryLastSleep(String babyId)
    {
        String[] selectionArgument = {babyId};
        String[] projection = {BabyLogContract.Sleep.TIMESTAMP};
        try
        {
            Cursor cursor = getActivity().getContentResolver().query(
                    BabyLogContract.Sleep.MAX_TIMESTAMP,
                    projection,
                    null,
                    selectionArgument,
                    null);
            cursor.moveToFirst();
            return cursor.getString(0);
        }
        catch (Exception e)
        {
            // do nothing
        }
        return "No activity yet";
    }

    private String queryLastDiaper(String babyId)
    {
        String[] selectionArgument = {babyId};
        String[] projection = {BabyLogContract.Diaper.TIMESTAMP};
        try
        {
            Cursor cursor = getActivity().getContentResolver().query(
                    BabyLogContract.Diaper.MAX_TIMESTAMP,
                    projection,
                    null,
                    selectionArgument,
                    null);
            cursor.moveToFirst();
            return cursor.getString(0);
        }
        catch (Exception e)
        {
            // do nothing
        }
        return "No activity yet";
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
        String[] args = {String.valueOf(ActiveContext.getActiveBaby(getActivity()).getID())};
        CursorLoader cl = new CursorLoader(getActivity(), BabyLogContract.Activity.CONTENT_URI,
                BabyLogContract.Activity.Query.PROJECTION,
                BabyLogContract.BABY_SELECTION_ARG,
                args,
                BabyLogContract.Activity.Query.SORT_BY_TIMESTAMP_DESC);
        return cl;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cl, Cursor cursor)
    {
        if (cursor.getCount() > 0)
        {
            // show last inserted row
            cursor.moveToFirst();
            mAdapter.swapCursor(cursor);
        }
        else
        {
            mAdapter.swapCursor(null);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cl)
    {
        mAdapter.swapCursor(null);

    }

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
}
