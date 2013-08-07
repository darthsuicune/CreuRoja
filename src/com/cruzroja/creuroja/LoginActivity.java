package com.cruzroja.creuroja;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends FragmentActivity implements
        LoaderManager.LoaderCallbacks<String> {

    SharedPreferences prefs;

    private EditText usernameView;
    private EditText passwordView;

    private String username;
    private String password;

    private static final String IS_FIRST_RUN = "isFirstRun";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private static final int LOADER_LOGIN = 1;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        setContentView(R.layout.activity_login);
        if (prefs.getBoolean(IS_FIRST_RUN, true)) {
            makeFirstRun();
        } else {
            doLogin();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return super.onCreateOptionsMenu(menu);
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

    private void makeFirstRun() {
        showLogin();

    }


    private void showLogin() {
        usernameView = (EditText) findViewById(R.id.username);
        passwordView = (EditText) findViewById(R.id.password);
    }

    private void doLogin() {
        if (usernameView != null) {
            username = usernameView.getText().toString();
            password = passwordView.getText().toString();
        } else {
            username = prefs.getString(LoginLoader.ARG_USERNAME, "");
            password = prefs.getString(LoginLoader.ARG_PASSWORD, "");
        }
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            showErrorMessage(R.string.no_credentials);
            return;
        }
        showProgress(true);
        Bundle args = new Bundle();
        args.putString(LoginLoader.ARG_USERNAME, username);
        args.putString(LoginLoader.ARG_PASSWORD, password);
        getSupportLoaderManager().restartLoader(LOADER_LOGIN, args, this);

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        final View loginStatusView = findViewById(R.id.login_status);
        final View loginFormView = findViewById(R.id.login_form);
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

    private void showMap(String s) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new LoginLoader(this, args);
    }

    @Override
    public void onLoadFinished(Loader<String> stringLoader, String s) {
        showProgress(false);
        if (s.equals(LoginLoader.RESPONSE_401)) {
            showErrorMessage(R.string.error_401);
        } else if (s.equals(LoginLoader.RESPONSE_WRONG_ID)) {
            showErrorMessage(R.string.error_wrong_id);
            cleanCredentials();
        } else if (s.equals(LoginLoader.RESPONSE_406)) {
            showErrorMessage(R.string.error_406);
        } else if (s.equals(LoginLoader.RESPONSE_IO_EXCEPTION)) {
<<<<<<< HEAD
        } else if (s.equals(LoginLoader.RESPONSE_NO_ID)) {
=======
            showErrorMessage(R.string.error_io_exc);
        } else if (s.equals(LoginLoader.RESPONSE_NO_ID)) {
            showErrorMessage(R.string.error_no_id);
>>>>>>> origin/Login
        } else if (s.equals(LoginLoader.RESPONSE_PROTOCOL_EXCEPTION)) {
            showErrorMessage(R.string.error_prot_exc);
        } else {
            prefs.edit().putBoolean(IS_FIRST_RUN, false).putString(USERNAME, username)
                    .putString(PASSWORD, password).commit();
            showMap(s);
            return;
        }
        showLogin();
    }

    private void showErrorMessage(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
    }

    private void cleanCredentials() {
        prefs.edit().clear().commit();
    }

    private void showErrorMessage(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoaderReset(Loader<String> stringLoader) {
    }
}