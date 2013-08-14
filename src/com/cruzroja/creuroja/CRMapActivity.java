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

import com.cruzroja.creuroja.utils.ConnectionClient;
import com.cruzroja.creuroja.utils.Settings;

public class CRMapActivity extends ActionBarActivity implements
		LoaderCallbacks<Integer> {
	private static final int ACTIVITY_LOGIN = 1;
	private static final int LOGIN_LOADER = 1;

	private SharedPreferences prefs;

	private CRMapFragment mMapFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#CC0000")));

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
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
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

		mMapFragment = (CRMapFragment) Fragment.instantiate(this,
				CRMapFragment.class.getName());
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
			args.putString(Settings.USERNAME,
					prefs.getString(Settings.USERNAME, ""));
			args.putString(Settings.PASSWORD,
					prefs.getString(Settings.PASSWORD, ""));
			getSupportLoaderManager().restartLoader(LOGIN_LOADER, args, this);
		}
	}

	@Override
	public Loader<Integer> onCreateLoader(int id, Bundle args) {
		return new LoginLoader(this, args);
	}

	@Override
	public void onLoadFinished(Loader<Integer> loader, Integer result) {
		switch (result) {
		case Settings.INVALID_CREDENTIALS:
			// The user has been invalidated by the system administrator.
			Settings.clean(this);
			finish();
			break;
		case Settings.LOGIN_UNKNOWN_ERROR:
			// An error of other type has happened. We leave a log in the logcat
			// but allow usage
			Log.e(Settings.LOG, getString(R.string.error_connection));
			break;
		default:
			// Any type of valid user will be redirected here. Usage is still
			// allowed. If the user has a new role, it will be changed.
			if(result != prefs.getInt(Settings.USER_ROLE, 0)){
				prefs.edit().putInt(Settings.USER_ROLE, result).commit();
			}
			return;
		}
	}

	@Override
	public void onLoaderReset(Loader<Integer> loader) {
	}
}
