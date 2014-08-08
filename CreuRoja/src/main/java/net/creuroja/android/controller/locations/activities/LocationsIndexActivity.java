package net.creuroja.android.controller.locations.activities;

import android.accounts.Account;
import android.app.ActionBar;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncStatusObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.SupportMapFragment;

import net.creuroja.android.R;
import net.creuroja.android.controller.locations.LocationsListListener;
import net.creuroja.android.model.Settings;
import net.creuroja.android.model.db.CreuRojaContract;
import net.creuroja.android.model.db.CreuRojaProvider;
import net.creuroja.android.model.locations.Location;
import net.creuroja.android.model.locations.LocationList;
import net.creuroja.android.model.locations.LocationType;
import net.creuroja.android.model.locations.RailsLocationList;
import net.creuroja.android.model.webservice.auth.AccountUtils;
import net.creuroja.android.model.webservice.auth.AccountUtils.LoginManager;
import net.creuroja.android.view.fragments.locations.GoogleMapFragmentHandler;
import net.creuroja.android.view.fragments.locations.LocationCardFragment;
import net.creuroja.android.view.fragments.locations.LocationListFragment;
import net.creuroja.android.view.fragments.locations.NavigationDrawerFragment;

import static net.creuroja.android.view.fragments.locations.GoogleMapFragmentHandler.*;
import static net.creuroja.android.view.fragments.locations.LocationCardFragment.OnLocationCardInteractionListener;
import static net.creuroja.android.view.fragments.locations.NavigationDrawerFragment.MapNavigationDrawerCallbacks;

public class LocationsIndexActivity extends ActionBarActivity
		implements LoginManager, MapNavigationDrawerCallbacks, LocationsListListener,
		OnLocationCardInteractionListener, MapInteractionListener {
	private static final int LOADER_LOCATIONS = 1;
	private static final String TAG_MAP = "mapFragment";
	private static final String TAG_LIST = "listFragment";
	private static final String TAG_CARD = "cardFragment";

	// Keep the authToken for manual refresh
	private Account mAccount;

	// Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private SupportMapFragment mapFragment;
	private GoogleMapFragmentHandler mapFragmentHandler;
	private LocationListFragment listFragment;
	private LocationCardFragment cardFragment;

	// Used to store the last screen title. For use in {@link #restoreActionBar()}.
	private CharSequence mTitle;
	private SharedPreferences prefs;

	private ViewMode currentViewMode;

	private LocationList mLocationList;
	private SyncStatusObserver observer;

	// Callbacks for when the auth token is returned
	@Override
	public void successfulLogin(String authToken) {
		startUi();
		if (currentViewMode == null) {
			String preferredMode =
					prefs.getString(Settings.LOCATIONS_INDEX_TYPE, ViewMode.MAP.toString());
			currentViewMode = ViewMode.getViewMode(preferredMode);
		}
		setMainFragment();
		requestSync();
		observer = new SyncStatusObserver() {
			@Override public void onStatusChanged(int which) {
				getSupportLoaderManager()
						.restartLoader(LOADER_LOCATIONS, null, new LocationListCallbacks());
			}
		};
		ContentResolver
				.addStatusChangeListener(ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE, observer);
	}

	@Override
	public void failedLogin() {
	}

	@Override public void onViewModeChanged(ViewMode newViewMode) {
		switch (newViewMode) {
			case LIST:
				currentViewMode = ViewMode.LIST;
				break;
			case MAP:
				currentViewMode = ViewMode.MAP;
				break;
			default:
				assert false;
		}
		setMainFragment();
	}

	private void setMainFragment() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment fragment;
		switch (currentViewMode) {
			case LIST:
				listFragment = (LocationListFragment) fragmentManager.findFragmentByTag(TAG_LIST);
				if (listFragment == null) {
					listFragment = LocationListFragment.newInstance(mLocationList);
				}
				fragment = listFragment;
				break;
			case MAP:
			default:
				mapFragment = (SupportMapFragment) fragmentManager.findFragmentByTag(TAG_MAP);
				if (mapFragment == null) {
					mapFragment = SupportMapFragment.newInstance(getMapOptions());
					mapFragmentHandler = new GoogleMapFragmentHandler(mapFragment, this);
				}
				fragment = mapFragment;
				break;
		}
		fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
	}

	@Override public void onLocationListItemSelected(Location location) {
		openLocationDetails(location);
	}

	@Override public void onDirectionsRequested(Location location) {
		//TODO: Implement route calculation and map drawing
	}

	@Override public void onCardCloseRequested() {
		//TODO: Remove fragment from sight
	}

	@Override public void onCardDetailsRequested(Location location) {
		openLocationDetails(location);
	}

	@Override public void onNavigationLegendItemSelected(LocationType type, boolean active) {
		//TODO: implement, whatever i had in mind when I thought of this.
	}

	@Override public void onNavigationMapTypeSelected(int mapType) {
		if (LocationsIndexActivity.ViewMode.MAP == currentViewMode && mapFragment != null) {
			mapFragmentHandler.setMapType(mapType);
		}
	}

	@Override public void onLocationClicked(Location location) {
		FragmentManager manager = getSupportFragmentManager();
		cardFragment = (LocationCardFragment) manager.findFragmentByTag(TAG_CARD);
		if (cardFragment == null) {
			cardFragment = LocationCardFragment.newInstance(location);
		} else {
			cardFragment.setLocation(location);
		}
		manager.beginTransaction().add(R.id.location_card_container, cardFragment, TAG_CARD)
				.commit();
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

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AccountUtils utils = new AccountUtils(this, this);
		utils.getAuth(this);
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
			case R.id.action_refresh:
				performSync();
				return true;
			case R.id.action_search:
				//TODO: implement, change to true
				return false;
			case R.id.action_locate:
				//TODO: implement, change to true
				return false;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void requestSync() {
		//TODO: Check for sync preferences
		performSync();
	}

	private void performSync() {
		Bundle bundle = new Bundle();
		bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
		bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
		ContentResolver.requestSync(mAccount, CreuRojaProvider.CONTENT_NAME, bundle);
	}

	private void openSettings() {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	private void openLocationDetails(Location location) {
		//TODO: Attach fragment if landscape / enough width.
		Intent intent = new Intent(this, LocationDetailsActivity.class);
		intent.putExtra(LocationDetailsActivity.EXTRA_LOCATION_ID, location.mId);
		startActivity(intent);
	}

	public enum ViewMode {
		MAP, LIST;

		public static ViewMode getViewMode(String mode) {
			if (mode.equals(LIST.toString())) {
				return LIST;
			} else {
				return MAP;
			}
		}
	}

	private class LocationListCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {
		@Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			Uri uri = CreuRojaContract.Locations.CONTENT_LOCATIONS;
			return new CursorLoader(getApplicationContext(), uri, null, null, null, null);
		}

		@Override public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
			mLocationList = new RailsLocationList(data);
		}

		@Override public void onLoaderReset(Loader<Cursor> loader) {

		}
	}
}
