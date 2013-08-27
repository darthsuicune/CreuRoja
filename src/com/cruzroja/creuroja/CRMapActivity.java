package com.cruzroja.creuroja;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.cruzroja.creuroja.utils.ConnectionClient;
import com.cruzroja.creuroja.utils.Settings;

public class CRMapActivity extends ActionBarActivity implements LoaderCallbacks<User> {
    private static final int ACTIVITY_LOGIN = 1;
    private static final int LOGIN_LOADER = 1;

    private SharedPreferences prefs;

    private CRMapFragment mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#CC0000")));

        if (savedInstanceState != null) {
            return;
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (!prefs.getBoolean(Settings.IS_VALID_USER, false)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, ACTIVITY_LOGIN);
        } else {
            checkCredentials();
            showMap();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
        case ACTIVITY_LOGIN:
            switch (resultCode) {
            case RESULT_OK:
                showMap();
                break;
            default:
                finish();
                break;
            }
            break;
        default:
            break;
        }
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

        mMapFragment = (CRMapFragment) Fragment.instantiate(this, CRMapFragment.class.getName());
        mMapFragment.setHasOptionsMenu(true);
        mMapFragment.setRetainInstance(true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.map_fragment_container, mMapFragment).commit();
    }

    /*************************
     * Login related methods *
     *************************/

    private void checkCredentials() {
        if (ConnectionClient.isConnected(this)) {
            Bundle args = new Bundle();
            args.putString(Settings.USERNAME, prefs.getString(Settings.USERNAME, ""));
            args.putString(Settings.PASSWORD, prefs.getString(Settings.PASSWORD, ""));
            getSupportLoaderManager().restartLoader(LOGIN_LOADER, args, this);
        }
    }

    @Override
    public Loader<User> onCreateLoader(int id, Bundle args) {
        if (ConnectionClient.isConnected(this)) {
            return new LoginLoader(CRMapActivity.this, args);
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<User> loader, User result) {
        if (result != null) {
            if (!result.mName.equals("")) {
                // Valid user
                result.save(prefs);
                mMapFragment.setUser(result);
                return;
            } else {
                // The user has been invalidated by the system administrator.
                Settings.clean(this);
                Toast.makeText(this, R.string.error_user_invalidated, Toast.LENGTH_LONG).show();
                finish();
            }

        } else {
            // There was a problem with the connection or with the server. Usage is allowed.
            Log.e(Settings.LOG, getString(R.string.error_connection));
        }
    }

    @Override
    public void onLoaderReset(Loader<User> loader) {
    }
}
