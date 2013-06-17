package com.cruzroja.creuroja;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

public class MainActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<String>,
        LoginFragment.LoginCallbacks {

    private static final int LOADER_LOGIN = 1;

    private static final String mapFragmentTag = "mapFragment";
    private static final String loginFragmentTag = "loginFragment";

    private SharedPreferences prefs;
    private FragmentManager fm;

    private CRMapFragment mMapFragment;
    private LoginFragment mLoginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (savedInstanceState != null) {
            return;
        }

        fm = getSupportFragmentManager();

        if (prefs.getBoolean(LoginFragment.IS_VALID_USER, false)) {
            showMap();
        } else {
            showLogin();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onBackPressed() {
        if (mMapFragment != null && mMapFragment.isAdded()) {
            if (!mMapFragment.onBackPressed()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    private void showMap() {
        if (mMapFragment == null) {
            mMapFragment = (CRMapFragment) Fragment.instantiate(this, CRMapFragment.class.getName());

        }
        mMapFragment.setHasOptionsMenu(true);
        mMapFragment.setRetainInstance(true);
        Bundle args = new Bundle();
        args.putString(LoginFragment.USERNAME, prefs.getString(LoginFragment.USERNAME, ""));
        args.putString(LoginFragment.PASSWORD, prefs.getString(LoginFragment.PASSWORD, ""));
        getSupportLoaderManager().restartLoader(LOADER_LOGIN, args, this);
        fm.beginTransaction().replace(R.id.fragment_holder, mMapFragment, mapFragmentTag).commitAllowingStateLoss();
    }

    private void showLogin() {
        if (mLoginFragment == null) {
            mLoginFragment = (LoginFragment) Fragment.instantiate(this, LoginFragment.class.getName());
        }
        mLoginFragment.setHasOptionsMenu(true);
        mLoginFragment.setRetainInstance(true);
        mLoginFragment.setCallbacks(this);
        fm.beginTransaction().replace(R.id.fragment_holder, mLoginFragment, loginFragmentTag).commitAllowingStateLoss();
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new LoginLoader(this, args);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String s) {
        if (s.equals(LoginLoader.RESPONSE_WRONG_ID)) {
            showLogin();
            cleanData();
        }
    }

    private void cleanData() {
        prefs.edit().clear().commit();
        JSONParser.removeFromDisk();
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        //Nothing to do here.
    }

    @Override
    public void doLogin() {
        showMap();
    }
}