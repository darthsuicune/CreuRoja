package com.cruzroja.android.view.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.cruzroja.android.R;
import com.cruzroja.android.controller.GoogleMapController;
import com.cruzroja.android.controller.MapController;
import com.cruzroja.android.model.auth.AccountUtils;
import com.cruzroja.android.model.auth.AccountUtils.LoginManager;
import com.cruzroja.android.view.fragments.LocationListFragment;
import com.cruzroja.android.view.fragments.NavigationDrawerFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class LocationsIndexActivity extends Activity implements LoginManager,
		NavigationDrawerFragment.NavigationDrawerCallbacks,
		LocationListFragment.OnFragmentInteractionListener {

	// Keep the authToken for manual refresh
	private String mAuthToken;

	// Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	private NavigationDrawerFragment mNavigationDrawerFragment;

	// Used to store the last screen title. For use in {@link #restoreActionBar()}.
	private CharSequence mTitle;

	private SharedPreferences prefs;

	private MapController mapController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AccountUtils utils = new AccountUtils(this, this);
		utils.getAuth(this);
	}

	// Callbacks for when the auth token is returned
	@Override
	public void successfulLogin(String authToken){
		mAuthToken = authToken;
		startUi();
	}
	@Override
	public void failedLogin(){
		finish();
	}

	private void startUi() {
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		setContentView(R.layout.activity_locations);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment
				.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		FragmentManager fragmentManager = getFragmentManager();
		Fragment fragment = null;
		switch (position) {
			case NavigationDrawerFragment.SEE_MAP:
				MapFragment mapFragment;
				if (mapController == null) {
					mapController = new GoogleMapController(prefs);
					mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
					if (mapFragment == null) {
						mapFragment = MapFragment.newInstance(mapController.getMapOptions());
					}
					mapController.setMapFragment(mapFragment);
					fragment = mapFragment;
				}
				break;
			case NavigationDrawerFragment.SEE_LIST:
				fragment = LocationListFragment.newInstance("", "");
				break;
			case NavigationDrawerFragment.MAP_TYPE_HYBRID:
				mapController.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				break;
			case NavigationDrawerFragment.MAP_TYPE_NORMAL:
				mapController.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				break;
			case NavigationDrawerFragment.MAP_TYPE_SATELLITE:
				mapController.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				break;
			case NavigationDrawerFragment.MAP_TYPE_TERRAIN:
				mapController.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				break;
			default:
				//wait, wat?
				break;
		}
		// update the main content by replacing fragments
		if (fragment != null) {
			fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
		}
	}

	public void onSectionAttached(int number) {
		switch (number) {
			case 1:
				mTitle = getString(R.string.title_section1);
				break;
			case 2:
				mTitle = getString(R.string.title_section2);
				break;
			case 3:
				mTitle = getString(R.string.title_section3);
				break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (mNavigationDrawerFragment != null && !mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.locations, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
			case R.id.action_settings:
				openSettings();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void openSettings() {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	@Override public void onFragmentInteraction(String id) {
		//TODO
		Toast.makeText(this, "ASDF " + id, Toast.LENGTH_LONG).show();
	}
}
