package com.progrema.superbaby.ui.fragment.login;

import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.progrema.superbaby.util.ActiveContext;
import com.progrema.superbaby.util.SecurityUtils;

/**
 * Created by iqbalpakeh on 5/2/14.
 */
public class LogInFragment extends Fragment implements View.OnClickListener
{
    /**
     * LogInFragment private data
     */
    private Button loginButton;
    private Button registerButton;
    private Button finishRegisterButton;
    private EditText userName;
    private EditText userPassword;
    private EditText userSecurityQuestion;
    private EditText userSecurityAnswer;

    /**
     * Get the instance of LogInFragment
     *
     * @return LogInFragment instance
     */
    public static LogInFragment getInstance()
    {
        return new LogInFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        loginButton =
                (Button) rootView.findViewById(R.id.fragment_authentication_button_login);
        registerButton =
                (Button) rootView.findViewById(R.id.fragment_authentication_button_register);
        finishRegisterButton =
                (Button) rootView.findViewById(R.id.fragment_authentication_button_finish_register);
        userName =
                (EditText) rootView.findViewById(R.id.fragment_authentication_edit_text_name);
        userPassword =
                (EditText) rootView.findViewById(R.id.fragment_authentication_edit_text_password);
        userSecurityQuestion =
                (EditText) rootView.findViewById(R.id.fragment_authentication_edit_text_security_question);
        userSecurityAnswer =
                (EditText) rootView.findViewById(R.id.fragment_authentication_edit_text_security_answer);

        // we use this because of the EditText property of password set the font to be different!!
        userPassword.setTypeface(Typeface.DEFAULT);
        userSecurityAnswer.setTypeface(Typeface.DEFAULT);

        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        finishRegisterButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.fragment_authentication_button_login:
                handleLoginButton();
                break;
            case R.id.fragment_authentication_button_register:
                handleRegisterButton();
                break;
            case R.id.fragment_authentication_button_finish_register:
                handleFinishRegisterButton();
                break;
        }
    }

    /**
     * Handle login button
     */
    private void handleLoginButton()
    {
        String userName, password, userMessage;
        userName = this.userName.getText().toString();
        password = userPassword.getText().toString();

        // user input checking
        userMessage = loginInputCheck(userName, password);
        if (userMessage.equals(getResources().getString(R.string.ok_message)))
        {
            // save active user in preference
            ActiveContext.setActiveUser(getActivity(), userName);

            // move to baby input fragment
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.login_activity_container, BabyInputFragment.getInstance());
            fragmentTransaction.commit();
            return;
        }

        // show warning message to user
        Toast toast = Toast.makeText(getActivity(), userMessage, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * Handle registration button
     */
    private void handleRegisterButton()
    {
        inflateRegisterPage();
    }

    /**
     * Inflate the login page interface
     */
    private void inflateLoginPage()
    {
        // clear input
        clearInput();

        // hide unnecessary object
        finishRegisterButton.setVisibility(View.GONE);
        userSecurityQuestion.setVisibility(View.GONE);
        userSecurityAnswer.setVisibility(View.GONE);

        // show remaining object
        loginButton.setVisibility(View.VISIBLE);
        registerButton.setVisibility(View.VISIBLE);
    }

    /**
     * Inflate the register page interface
     */
    private void inflateRegisterPage()
    {
        // clear input
        clearInput();

        // show remaining object
        finishRegisterButton.setVisibility(View.VISIBLE);
        userSecurityQuestion.setVisibility(View.VISIBLE);
        userSecurityAnswer.setVisibility(View.VISIBLE);

        // hide unnecessary object
        loginButton.setVisibility(View.GONE);
        registerButton.setVisibility(View.GONE);
    }

    /**
     * Clear user input
     */
    private void clearInput()
    {
        userName.setText("");
        userPassword.setText("");
        userSecurityQuestion.setText("");
        userSecurityAnswer.setText("");
    }

    /**
     * Handle finish registration button
     */
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

            inflateLoginPage();
            return;
        }
        else if (userMessage.equals(getResources().getString(R.string.username_already_exist_message)))
        {
            inflateLoginPage();
        }

        // show warning message to user
        Toast toast = Toast.makeText(getActivity(), userMessage, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * Check user's input during login
     *
     * @param userName user's input
     * @param password user's input
     * @return message describing condition of checking
     */
    private String loginInputCheck(String userName, String password)
    {
        Cursor cursor;

        // empty value checking
        if (userName.isEmpty() || password.isEmpty())
        {
            return getResources().getString(R.string.input_not_complete_message);
        }

        // user name is not registered
        cursor = usernameQuery(userName);
        if (cursor.getCount() == 0)
        {
            return getResources().getString(R.string.username_is_not_registered_message);
        }

        // username and password verification
        if (!passwordVerification(cursor, password))
        {
            return getResources().getString(R.string.wrong_username_and_password_message);
        }

        // everything is fine
        return getResources().getString(R.string.ok_message);
    }

    /**
     * User name and password verification
     *
     * @param cursor   username and password cursor
     * @param password user's input
     * @return TRUE if verification is okay, FALSE otherwise
     */
    private boolean passwordVerification(Cursor cursor, String password)
    {
        String passwordHash;

        // get username from user input and password's hash value from database
        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            passwordHash = cursor.getString(BabyLogContract.User.Query.OFFSET_PASSWORD);

            // compare the hash value of stored password and input password
            return (SecurityUtils.computeSHA1(password).compareTo(passwordHash) == 0);
        }
        else
        {
            return false;
        }

    }

    /**
     * Check user's input during registration
     *
     * @param userName    user's input
     * @param password    user's input
     * @param secQuestion user's input
     * @param secAnswer   user's input
     * @return message describing condition of checking
     */
    private String registerInputCheck(String userName, String password, String secQuestion, String secAnswer)
    {
        Cursor cursor;

        // empty value checking
        if (userName.isEmpty() || password.isEmpty() ||
                secQuestion.isEmpty() || secAnswer.isEmpty())
        {
            return getResources().getString(R.string.input_not_complete_message);
        }

        // user name already exist checking
        cursor = usernameQuery(userName);
        if (cursor.getCount() > 0)
        {
            return getResources().getString(R.string.username_already_exist_message);
        }

        // everything is fine
        return getResources().getString(R.string.ok_message);
    }

    /**
     * Query username from data base
     *
     * @param userName user input
     * @return cursor containing user query result
     */
    private Cursor usernameQuery(String userName)
    {
        //TODO: we should do query on another thread and show the waiting icon

        String[] selectionArgument = {userName};
        return getActivity().getContentResolver().query(BabyLogContract.User.CONTENT_URI,
                BabyLogContract.User.Query.PROJECTION,
                BabyLogContract.User.USER_NAME + "=?",
                selectionArgument,
                BabyLogContract.User.USER_NAME);
    }
}
