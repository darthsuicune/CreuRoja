package com.cruzroja.creuroja;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
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

    private static final String TEST_USER = "darthsuicune";
    private static final String TEST_PASS = "tu puta madre";

    private static final int LOADER_LOGIN = 1;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

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
            case R.id.action_login: //TODO: add action and perform login
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
        setContentView(R.layout.login);
        usernameView = (EditText) findViewById(R.id.username);
//        usernameView.setText(TEST_USER);
        passwordView = (EditText) findViewById(R.id.password);
//        passwordView.setText(TEST_PASS);
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
        Bundle args = new Bundle();
        args.putString(LoginLoader.ARG_USERNAME, username);
        args.putString(LoginLoader.ARG_PASSWORD, password);
        getSupportLoaderManager().restartLoader(LOADER_LOGIN, args, this);

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
        if (s.equals(LoginLoader.RESPONSE_401)) {
            showErrorMessage(R.string.error_401);
        } else if (s.equals(LoginLoader.RESPONSE_WRONG_ID)) {
            showErrorMessage(R.string.error_wrong_id);
            cleanCredentials();
        } else if (s.equals(LoginLoader.RESPONSE_406)) {
            showErrorMessage(R.string.error_406);
        } else if (s.equals(LoginLoader.RESPONSE_IO_EXCEPTION)) {
            showErrorMessage(R.string.error_io_exc);
        } else if (s.equals(LoginLoader.RESPONSE_NO_ID)) {
            showErrorMessage(R.string.error_no_id);
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

    @Override
    public void onLoaderReset(Loader<String> stringLoader) {
    }
}