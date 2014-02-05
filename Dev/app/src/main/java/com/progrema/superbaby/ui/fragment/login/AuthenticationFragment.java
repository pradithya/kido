package com.progrema.superbaby.ui.fragment.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.progrema.superbaby.R;

/**
 * Created by iqbalpakeh on 5/2/14.
 */
public class AuthenticationFragment extends Fragment implements View.OnClickListener
{
    private static AuthenticationFragment singletonAuthenticationFragment = null;
    private Button loginButton;
    private Button registerButton;
    private Button finishRegisterButton;
    private EditText userName;
    private EditText userPassword;
    private EditText securityQuestion;
    private EditText securityAnswer;

    public static synchronized AuthenticationFragment getInstance()
    {
        if (singletonAuthenticationFragment == null)
        {
            singletonAuthenticationFragment = new AuthenticationFragment();
        }
        return singletonAuthenticationFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_authentication, container, false);

        loginButton = (Button) rootView.findViewById(R.id.fragment_authentication_button_login);
        registerButton = (Button) rootView.findViewById(R.id.fragment_authentication_button_register);
        finishRegisterButton = (Button) rootView.findViewById(R.id.fragment_authentication_button_finish_register);
        userName = (EditText) rootView.findViewById(R.id.fragment_authentication_edit_text_name);
        userPassword = (EditText) rootView.findViewById(R.id.button_stopwatch_pause);
        securityQuestion = (EditText) rootView.findViewById(R.id.fragment_authentication_edit_text_security_question);
        securityAnswer = (EditText) rootView.findViewById(R.id.fragment_authentication_edit_text_security_answer);

        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        finishRegisterButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case  R.id.fragment_authentication_button_login:
                handleLoginButton();
                break;
            case  R.id.fragment_authentication_button_register:
                handleRegisterButton();
                break;
            case  R.id.fragment_authentication_button_finish_register:
                handleFinishRegisterButton();
                break;
        }
    }

    private void handleLoginButton()
    {
        // dummy function: move to next fragment
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.login_activity_container, BabyInputFragment.getInstance());
        fragmentTransaction.commit();
    }

    private void handleRegisterButton()
    {
        // show remaining object
        finishRegisterButton.setVisibility(View.VISIBLE);
        securityQuestion.setVisibility(View.VISIBLE);
        securityAnswer.setVisibility(View.VISIBLE);

        // hide unnecessary object
        loginButton.setVisibility(View.GONE);
        registerButton.setVisibility(View.GONE);

    }

    private void handleFinishRegisterButton()
    {
        // dummy function: move to next fragment
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.login_activity_container, BabyInputFragment.getInstance());
        fragmentTransaction.commit();
    }

}
