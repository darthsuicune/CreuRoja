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

/**
 * Created by lapuente on 07.06.13.
 */
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
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.address: //TODO: add action and perform login
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
        //UNCOMMENT!!
//        setContentView(0); //TODO Add actual login layout
//        usernameView = (EditText) findViewById(R.id.address);
//        passwordView = (EditText) findViewById(R.id.address);
        doLogin();
    }

    private void doLogin() {
        //UNCOMMENT!!
//        username = usernameView.getText().toString();
//        password = passwordView.getText().toString();
        username = TEST_USER;
        password = TEST_PASS;
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
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
        } else if (s.equals(LoginLoader.RESPONSE_406)) {
        } else if (s.equals(LoginLoader.RESPONSE_IO_EXCEPTION)) {
            showMap(s);
        } else if (s.equals(LoginLoader.RESPONSE_NO_ID)) {
            showMap(s);
        } else if (s.equals(LoginLoader.RESPONSE_PROTOCOL_EXCEPTION)) {
        } else {
            prefs.edit().putBoolean(IS_FIRST_RUN, false).putString(USERNAME, username)
                    .putString(PASSWORD, password).commit();
            showMap(s);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> stringLoader) {
    }
}