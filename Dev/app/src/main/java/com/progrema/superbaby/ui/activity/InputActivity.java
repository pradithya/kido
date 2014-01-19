package com.progrema.superbaby.ui.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import com.progrema.superbaby.R;
import com.progrema.superbaby.ui.fragment.*;

/**
 * Created by iqbalpakeh on 20/1/14.
 */
public class InputActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();

        // fragment module
        Fragment module = null;

        // get intent message from caller entity
        String intentMessage = getIntent().getStringExtra(HomeActivity.INTENT_SELECT_FRAGMENT);
        if(intentMessage.equals(BabyInputFragment.INTENT)){
            module = BabyInputFragment.getInstance(this);
        }
        else if(intentMessage.equals(DiaperInputFragment.INTENT)){
            module = DiaperInputFragment.getInstance(this);
        }
        else if(intentMessage.equals(FoodInputFragment.INTENT)){
            module = DiaperInputFragment.getInstance(this);
        }
        else if(intentMessage.equals(MilkInputFragment.INTENT)){
            module = MilkInputFragment.getInstance(this);
        }
        else if(intentMessage.equals((PumpingInputFragment.INTENT))){
            module = PumpingInputFragment.getInstance(this);
        }
        else if(intentMessage.equals(SleepInputFragment.INTENT)){
            module = SleepInputFragment.getInstance(this);
        }
        else if(intentMessage.equals(VaccineInputFragment.INTENT)){
            module = VaccineInputFragment.getInstance(this);
        }

        // activate the fragment
        fragmentManager.beginTransaction().replace(R.id.input_activity_container, module).commit();

    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
    }
}
