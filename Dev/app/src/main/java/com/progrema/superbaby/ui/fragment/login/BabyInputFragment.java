package com.progrema.superbaby.ui.fragment.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.progrema.superbaby.R;
import com.progrema.superbaby.models.Baby;
import com.progrema.superbaby.models.BaseActor;
import com.progrema.superbaby.ui.activity.HomeActivity;

/**
 * Created by iqbalpakeh on 5/2/14.
 */
public class BabyInputFragment extends Fragment implements View.OnClickListener
{
    private static BabyInputFragment singletonBabyInputFragment = null;
    private EditText babyNameInput;
    private EditText babyBirthdayInput;
    private EditText babySexTypeInput;
    private Button doneButton;

    public static synchronized BabyInputFragment getInstance()
    {
        if (singletonBabyInputFragment == null)
        {
            singletonBabyInputFragment = new BabyInputFragment();
        }
        return singletonBabyInputFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_baby_input_login, container, false);

        doneButton = (Button) rootView.findViewById(R.id.fragment_baby_input_button_done);
        babyNameInput = (EditText) rootView.findViewById(R.id.fragment_baby_input_name);
        babyBirthdayInput = (EditText) rootView.findViewById(R.id.fragment_baby_input_birthday);
        babySexTypeInput = (EditText) rootView.findViewById(R.id.fragment_baby_input_sex_type);

        doneButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        String babyName, babyBirthday, babySextType;
        babyName = babyNameInput.getText().toString();
        babyBirthday = babyBirthdayInput.getText().toString();
        babySextType = babySexTypeInput.getText().toString();

        // get new baby info and store it to DB
        Baby baby = new Baby();
        baby.setName(babyName);
        baby.setBirthday(babyBirthday);
        if (babySextType.equals("male"))
        {
            baby.setSex(BaseActor.Sex.MALE);
        }
        else
        {
            baby.setSex(BaseActor.Sex.FEMALE);
        }
        baby.insert(getActivity());

        // Go to HomeActivity
        startActivity(new Intent(getActivity(), HomeActivity.class));
    }

}
