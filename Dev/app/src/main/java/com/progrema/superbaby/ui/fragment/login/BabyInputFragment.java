package com.progrema.superbaby.ui.fragment.login;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.progrema.superbaby.R;
import com.progrema.superbaby.models.Baby;
import com.progrema.superbaby.models.BaseActor;
import com.progrema.superbaby.ui.activity.HomeActivity;
import com.progrema.superbaby.ui.activity.LoginActivity;
import com.progrema.superbaby.util.ActiveContext;
import com.progrema.superbaby.util.FormatUtils;

import java.util.Calendar;

public class BabyInputFragment extends Fragment implements
        View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private EditText babyNameInput;
    private Button babyBirthdayInput;
    private Spinner babySexTypeInput;
    private ImageButton doneButton;
    private int year, month, date;

    public static BabyInputFragment getInstance() {
        return new BabyInputFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_baby_input_login, container, false);

        doneButton = (ImageButton) rootView.findViewById(R.id.fragment_baby_input_button_done);
        babyNameInput = (EditText) rootView.findViewById(R.id.fragment_baby_input_name);

        Calendar now = Calendar.getInstance();
        year = now.get(Calendar.YEAR);
        month = now.get(Calendar.MONTH);
        date = now.get(Calendar.DATE);

        babyBirthdayInput = (Button) rootView.findViewById(R.id.fragment_baby_input_birthday);
        babyBirthdayInput.setText(FormatUtils.fmtDate(getActivity(),
                String.valueOf(Calendar.getInstance().getTimeInMillis())));
        babyBirthdayInput.setOnClickListener(this);

        babySexTypeInput = (Spinner) rootView.findViewById(R.id.fragment_baby_input_sex_type);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.gender_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        babySexTypeInput.setAdapter(adapter);

        doneButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fragment_baby_input_birthday: {

                DatePickerDialog dateChooser =
                        new DatePickerDialog(getActivity(), this, year, month, date);
                dateChooser.show();
                break;
            }
            case R.id.fragment_baby_input_button_done: {
                String babyName, babyBirthday, babySexType;
                babyName = babyNameInput.getText().toString();
                Calendar dob = Calendar.getInstance();
                dob.set(year, month, date);
                babyBirthday = String.valueOf(dob.getTimeInMillis());
                babySexType = (String) babySexTypeInput.
                        getAdapter().getItem(babySexTypeInput.getSelectedItemPosition());

                // get new baby info and store it to DB
                Baby baby = new Baby();
                baby.setName(babyName);
                baby.setBirthday(babyBirthday);
                if (babySexType.equals(BaseActor.Sex.MALE.getTitle())) {
                    baby.setSex(BaseActor.Sex.MALE);
                } else if (babySexType.equals(BaseActor.Sex.FEMALE.getTitle())) {
                    baby.setSex(BaseActor.Sex.FEMALE);
                }
                baby.insert(getActivity());

                // save active baby in preference
                ActiveContext.setActiveBaby(getActivity(), babyName);

                // skip login for the next application startup
                SharedPreferences setting = getActivity().getSharedPreferences(LoginActivity.PREF_LOGIN, 0);
                SharedPreferences.Editor editor = setting.edit();
                editor.putBoolean(LoginActivity.PREF_SKIP_LOGIN, true);
                editor.commit();

                // Go to HomeActivity
                startActivity(new Intent(getActivity(), HomeActivity.class));
                getActivity().finish();
                break;
            }
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.date = dayOfMonth;

        Calendar dob = Calendar.getInstance();
        dob.set(year, month, date);
        babyBirthdayInput.setText(FormatUtils.fmtDate(getActivity(), String.valueOf(dob.getTimeInMillis())));
    }

}
