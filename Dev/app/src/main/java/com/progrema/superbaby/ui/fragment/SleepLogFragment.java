package com.progrema.superbaby.ui.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.progrema.superbaby.R;
import com.progrema.superbaby.provider.BabyLogContract;

/**
 * Created by iqbalpakeh on 18/1/14.
 */
public class SleepLogFragment extends Fragment
        implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>{

    private static SleepLogFragment singletonSleepLogFragment = null;
    private Button startButton;
    private TextView showActivityId;
    private TextView showBabyId;
    private TextView showLastTimeInput;
    private TextView showLastDurationInput;

    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
    private static final int LOADER_ID = 1;

    private interface SleepQuery{
        String[] PROJECTION  = {
                BaseColumns._ID,
                BabyLogContract.Sleep.ACTIVITY_ID,
                BabyLogContract.Sleep.BABY_ID,
                BabyLogContract.Sleep.TIMESTAMP,
                BabyLogContract.Sleep.DURATION
        };
    }

    public SleepLogFragment(Context context){}

    public static synchronized SleepLogFragment getInstance(Context context){
        if (singletonSleepLogFragment == null) {
            singletonSleepLogFragment = new SleepLogFragment(context.getApplicationContext());
        }
        return singletonSleepLogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate fragment layout
        View rootView = inflater.inflate(R.layout.fragment_sleep_log, container, false);

        // set onClickListener to button
        startButton = (Button)rootView.findViewById(R.id.button_start);
        startButton.setOnClickListener(this);

        // get TextView object
        showActivityId = (TextView) rootView.findViewById(R.id.show_last_activity_id_input);
        showBabyId = (TextView) rootView.findViewById(R.id.show_last_baby_id_input);
        showLastTimeInput = (TextView) rootView.findViewById(R.id.show_last_timestamp_input);
        showLastDurationInput = (TextView) rootView.findViewById(R.id.show_last_duration_input);

        // prepare loader
        mCallbacks = this;
        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_ID, null, mCallbacks);

        return rootView;
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){

            case  R.id.button_start:
                handleStartButton();
                break;

        }
    }

    private void handleStartButton(){

        // jump to sleep input fragment
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_activity_container, SleepInputFragment.getInstance(getActivity()));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        CursorLoader cl = new CursorLoader(getActivity(),
                                           BabyLogContract.Sleep.CONTENT_URI,
                                           SleepQuery.PROJECTION,
                                           null,
                                           null,
                                           BabyLogContract.Sleep.ACTIVITY_ID);
        return cl;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if(cursor.getCount() > 0){
            showActivityId.setText(cursor.getString(0));
            showBabyId.setText(cursor.getString(1));
            showLastTimeInput.setText(cursor.getString(2));
            showLastDurationInput.setText(cursor.getString(3));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
}
