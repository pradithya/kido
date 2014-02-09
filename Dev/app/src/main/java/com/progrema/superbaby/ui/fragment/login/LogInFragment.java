package com.progrema.superbaby.ui.fragment.login;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.progrema.superbaby.R;
import com.progrema.superbaby.models.User;
import com.progrema.superbaby.provider.BabyLogContract;
import com.progrema.superbaby.ui.activity.HomeActivity;
import com.progrema.superbaby.util.SecurityUtils;

/**
 * Created by iqbalpakeh on 5/2/14.
 */
public class LogInFragment extends Fragment implements View.OnClickListener
{
    private static LogInFragment singletonLogInFragment = null;
    private Button loginButton;
    private Button registerButton;
    private Button finishRegisterButton;
    private EditText userName;
    private EditText userPassword;
    private EditText userSecurityQuestion;
    private EditText userSecurityAnswer;

    private interface UserQuery
    {
        String[] PROJECTION  =
                {
                        BaseColumns._ID,
                        BabyLogContract.User.USER_NAME,
                        BabyLogContract.User.PASSWORD,
                        BabyLogContract.User.SEC_QUESTION,
                        BabyLogContract.User.SEC_ANSWER
                };
    }

    public static synchronized LogInFragment getInstance()
    {
        if (singletonLogInFragment == null)
        {
            singletonLogInFragment = new LogInFragment();
        }
        return singletonLogInFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_authentication, container, false);

        loginButton = (Button) rootView.findViewById(R.id.fragment_authentication_button_login);
        registerButton = (Button) rootView.findViewById(R.id.fragment_authentication_button_register);
        finishRegisterButton = (Button) rootView.findViewById(R.id.fragment_authentication_button_finish_register);
        userName = (EditText) rootView.findViewById(R.id.fragment_authentication_edit_text_name);
        userPassword = (EditText) rootView.findViewById(R.id.fragment_authentication_edit_text_password);
        userSecurityQuestion = (EditText) rootView.findViewById(R.id.fragment_authentication_edit_text_security_question);
        userSecurityAnswer = (EditText) rootView.findViewById(R.id.fragment_authentication_edit_text_security_answer);

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
        String name, password, secQuestion, secAnswer, userMessage;
        name = userName.getText().toString();
        password = userPassword.getText().toString();

        // user input checking
        userMessage = loginInputCheck(name, password);
        if (userMessage.equals(getResources().getString(R.string.ok_message)))
        {
            // Go to HomeActivity
            startActivity(new Intent(getActivity(), HomeActivity.class));
        }
        else
        {
            // show warning message to user
            Toast toast = Toast.makeText(getActivity(), userMessage, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void handleRegisterButton()
    {
        // show remaining object
        finishRegisterButton.setVisibility(View.VISIBLE);
        userSecurityQuestion.setVisibility(View.VISIBLE);
        userSecurityAnswer.setVisibility(View.VISIBLE);

        // hide unnecessary object
        loginButton.setVisibility(View.GONE);
        registerButton.setVisibility(View.GONE);
    }

    private void handleFinishRegisterButton()
    {
        String name, password, secQuestion, secAnswer, userMessage;
        name = userName.getText().toString();
        password = userPassword.getText().toString();
        secQuestion = userSecurityQuestion.getText().toString();
        secAnswer = userSecurityAnswer.getText().toString();

        // user input checking
        userMessage = registerInputCheck(name, password, secQuestion, secAnswer);
        if (userMessage.equals(getResources().getString(R.string.ok_message)))
        {
            // get input value and store to DB
            User user = new User();
            user.setName(name);
            user.setPassword(password);
            user.setSecurityQuestion(secQuestion);
            user.setSecurityAnswer(secAnswer);
            user.insert(getActivity());

            // move to next fragment
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.login_activity_container, BabyInputFragment.getInstance());
            fragmentTransaction.commit();
        }
        else
        {
            // show warning message to user
            Toast toast = Toast.makeText(getActivity(), userMessage, Toast.LENGTH_LONG);
            toast.show();
        }

    }

    private String loginInputCheck(String name, String password)
    {
        // empty value checking
        if (name.isEmpty() || password.isEmpty())
        {
            return getResources().getString(R.string.input_not_complete_message);
        }

        // wrong username and password checking
        if (!isUserNamePasswordOk(name, password))
        {
            return getResources().getString(R.string.wrong_username_and_password);
        }

        return getResources().getString(R.string.ok_message);
    }

    private boolean isUserNamePasswordOk(String name, String password)
    {
        Cursor cursor;
        String passwordHash;

        cursor = queryUserNameFromDB(name);
        cursor.moveToFirst();
        passwordHash = cursor.getString(2);

        return (SecurityUtils.computeSHA1(password).compareTo(passwordHash) == 0);
    }

    private String registerInputCheck(String name, String password, String secQuestion, String secAnswer)
    {
        // empty value checking
        if (name.isEmpty() || password.isEmpty() ||
                secQuestion.isEmpty() || secAnswer.isEmpty())
        {
            return getResources().getString(R.string.input_not_complete_message);
        }

        // user name already exist checking
        if(queryUserNameFromDB(name).getCount() > 0)
        {
            return getResources().getString(R.string.username_already_exist_message);
        }

        return getResources().getString(R.string.ok_message);
    }

    private Cursor queryUserNameFromDB(String name)
    {
        String [] selectionArgument = {name};
        return getActivity().getContentResolver().query(BabyLogContract.User.CONTENT_URI,
                UserQuery.PROJECTION,
                BabyLogContract.User.USER_NAME + "=?",
                selectionArgument,
                BabyLogContract.User.USER_NAME);
    }
}
