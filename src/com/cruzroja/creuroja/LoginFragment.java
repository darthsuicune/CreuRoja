package com.cruzroja.creuroja;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<String>, TextView.OnEditorActionListener {

    SharedPreferences prefs;

    private EditText usernameView;
    private EditText passwordView;
    private Button loginButtonView;

    private String username;
    private String password;

    public final static String IS_VALID_USER = "isValidUser";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    private static final int LOADER_LOGIN = 1;

    public LoginFragment() {
    }

    public interface LoginCallbacks {
        public void doLogin();
    }

    private LoginCallbacks mLoginCallbacks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        usernameView = (EditText) v.findViewById(R.id.username);
        usernameView.setOnEditorActionListener(this);
        passwordView = (EditText) v.findViewById(R.id.password);
        passwordView.setOnEditorActionListener(this);
        loginButtonView = (Button) v.findViewById(R.id.login_button);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
        }
        loginButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin();
            }
        });

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_login, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_login:
                doLogin();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        switch (textView.getId()) {
            case R.id.username:
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    passwordView.requestFocus();
                    return true;
                }
            case R.id.password:
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    doLogin();
                    return true;

                }
            default:
                return false;
        }
    }

    public void setCallbacks(LoginCallbacks callbacksActivity) {
        mLoginCallbacks = callbacksActivity;
    }

    private void doLogin() {
        if (usernameView != null) {
            username = usernameView.getText().toString();
            password = passwordView.getText().toString();
        }
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), R.string.no_credentials, Toast.LENGTH_LONG).show();
            return;
        }
        showProgress(true);
        Bundle args = new Bundle();
        args.putString(LoginLoader.ARG_USERNAME, username);
        args.putString(LoginLoader.ARG_PASSWORD, password);
        getLoaderManager().restartLoader(LOADER_LOGIN, args, this);

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        final View loginStatusView = getActivity().findViewById(R.id.login_status);
        final View loginFormView = getActivity().findViewById(R.id.login_form);
        if (loginStatusView == null || loginFormView == null) {
            return;
        }
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

            loginStatusView.setVisibility(View.VISIBLE);
            loginStatusView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            loginStatusView.setVisibility(show ? View.VISIBLE
                                    : View.GONE);
                        }
                    });

            loginFormView.setVisibility(View.VISIBLE);
            loginFormView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            loginFormView.setVisibility(show ? View.GONE
                                    : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            loginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new LoginLoader(getActivity(), args);
    }

    @Override
    public void onLoadFinished(Loader<String> stringLoader, String s) {
        showProgress(false);

        loginFinished(s);
    }

    private void loginFinished(String s) {
        if (s.equals(LoginLoader.RESPONSE_401)) {
            Toast.makeText(getActivity(), R.string.error_401, Toast.LENGTH_LONG).show();
        } else if (s.equals(LoginLoader.RESPONSE_WRONG_ID)) {
            Toast.makeText(getActivity(), R.string.error_wrong_id, Toast.LENGTH_LONG).show();
        } else if (s.equals(LoginLoader.RESPONSE_406)) {
            Toast.makeText(getActivity(), R.string.error_406, Toast.LENGTH_LONG).show();
        } else if (s.equals(LoginLoader.RESPONSE_IO_EXCEPTION)) {
            Toast.makeText(getActivity(), R.string.error_io_exc, Toast.LENGTH_LONG).show();
        } else if (s.equals(LoginLoader.RESPONSE_NO_ID)) {
            Toast.makeText(getActivity(), R.string.error_no_id, Toast.LENGTH_LONG).show();
        } else if (s.equals(LoginLoader.RESPONSE_PROTOCOL_EXCEPTION)) {
            Toast.makeText(getActivity(), R.string.error_prot_exc, Toast.LENGTH_LONG).show();
        } else {
            prefs.edit().putBoolean(IS_VALID_USER, true).putString(USERNAME, username)
                    .putString(PASSWORD, password).commit();
            mLoginCallbacks.doLogin();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> stringLoader) {
    }
}
