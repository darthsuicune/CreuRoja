package com.cruzroja.creuroja.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.cruzroja.creuroja.LoginLoader;
import com.cruzroja.creuroja.R;
import com.cruzroja.creuroja.User;
import com.cruzroja.creuroja.utils.ConnectionClient;
import com.cruzroja.creuroja.utils.Settings;

public class LoginActivity extends FragmentActivity implements LoaderCallbacks<User> {
    private static final int LOGIN_LOADER = 1;

    private EditText mUsernameView;
    private EditText mPasswordView;
    private View mLoginStatusView;
    private View mLoginFormView;
    private TextView mLoginStatusMessageView;
    private Button mLoginButtonView;

    private String mUsername;
    private String mPassword;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mLoginStatusView = findViewById(R.id.login_status);
        mLoginFormView = findViewById(R.id.login_form);
        mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
        mLoginButtonView = (Button) findViewById(R.id.login_button);

        mLoginButtonView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
        mLoginButtonView.setBackgroundColor(Color.parseColor("#EEEEEE"));
        setResult(RESULT_CANCELED);
    }

    /**
     * Attempts to sign in the account specified by the login form. If there are form errors
     * (invalid username, missing fields, etc.), the errors are presented and no actual login
     * attempt is made.
     */
    public void attemptLogin() {

        // Store values at the time of the login attempt.
        mUsername = mUsernameView.getText().toString();
        mPassword = mPasswordView.getText().toString();

        if (TextUtils.isEmpty(mPassword) || (TextUtils.isEmpty(mUsername))) {
            Toast.makeText(this, R.string.error_no_credentials, Toast.LENGTH_LONG).show();
        } else if (mPassword.length() < 4) {
            Toast.makeText(this, R.string.error_invalid_password, Toast.LENGTH_LONG).show();
        } else {
            if (ConnectionClient.isConnected(this)) {
                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.
                mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
                showProgress(true);
                Bundle args = new Bundle();
                args.putString(Settings.USERNAME, mUsername);
                args.putString(Settings.PASSWORD, mPassword);
                getSupportLoaderManager().restartLoader(LOGIN_LOADER, args, this);
            } else {
                Toast.makeText(this, R.string.error_login_no_connection, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showMap() {
        this.setResult(RESULT_OK);
        finish();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginStatusView.setVisibility(View.VISIBLE);
            mLoginStatusView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });

            mLoginFormView.setVisibility(View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<User> onCreateLoader(int id, Bundle args) {
        return new LoginLoader(LoginActivity.this, args);
    }

    @Override
    public void onLoadFinished(Loader<User> loader, User result) {
        if (result != null) {
            if (!result.mName.equals("")) { // Valid user
                result.save(PreferenceManager.getDefaultSharedPreferences(this), mPassword);
                showMap();
                return;
            } else { // An invalid user/password will send an empty username
                mPasswordView.setText("");
                Toast.makeText(this, R.string.error_invalid_password, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, R.string.error_connection, Toast.LENGTH_LONG).show();
        }
        showProgress(false);
    }

    @Override
    public void onLoaderReset(Loader<User> loader) {
    }
}
