package net.creuroja.android.view.fragments.locations;


import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import net.creuroja.android.R;
import net.creuroja.android.controller.locations.activities.LocationsIndexActivity;
import net.creuroja.android.model.Location;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {
	// Remember the position of the selected item.
	private static final String STATE_SELECTED_MAP_TYPE = "selected_map_type";

	/**
	 * Per the design guidelines, you should show the drawer on launch until the user manually
	 * expands it. This shared preference tracks this.
	 */
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

	private MapNavigationDrawerCallbacks mapDrawerCallbacks;

	// Helper component that ties the action bar to the navigation drawer.
	private ActionBarDrawerToggle mDrawerToggle;

	private DrawerLayout mDrawerLayout;
	private View mFragmentContainerView;

	private int mCurrentSelectedMapType = GoogleMap.MAP_TYPE_NORMAL;
	private boolean mFromSavedInstanceState;
	private boolean mUserLearnedDrawer;

	private SharedPreferences prefs;

	public NavigationDrawerFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Read in the flag indicating whether or not the user has demonstrated awareness of the
		// drawer. See PREF_USER_LEARNED_DRAWER for details.
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		mUserLearnedDrawer = prefs.getBoolean(PREF_USER_LEARNED_DRAWER, false);

		if (savedInstanceState != null) {
			mCurrentSelectedMapType = savedInstanceState.getInt(STATE_SELECTED_MAP_TYPE);
			mFromSavedInstanceState = true;
		}

		// Select either the default item (0) or the last selected item.
		selectMapType(mCurrentSelectedMapType);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Indicate that this fragment would like to influence the set of actions in the action bar.
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
		prepareViewModes(v);
		prepareLegend(v);
		prepareMapTypes(v);
		return v;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mapDrawerCallbacks = (MapNavigationDrawerCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException("Activity must implement MapNavigationDrawerCallbacks.");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mapDrawerCallbacks = null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_MAP_TYPE, mCurrentSelectedMapType);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Forward the new configuration the drawer toggle component.
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// If the drawer is open, show the global app actions in the action bar. See also
		// showGlobalContextActionBar, which controls the top-left area of the action bar.
		if (mDrawerLayout != null && isDrawerOpen()) {
			inflater.inflate(R.menu.global, menu);
			showGlobalContextActionBar();
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
			case R.id.action_example:
				Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT).show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void prepareViewModes(View v) {
		//TODO: Implement callback for activity
	}

	private void prepareLegend(View v) {
		//TODO: Implement callback for activity
	}

	public void prepareLegendObject(final TextView v, final Location.Type type,
									final boolean active) {
		if (v != null) {
			v.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View view) {
					v.setBackgroundColor((active) ? Color.parseColor("") : Color.parseColor(""));
					mapDrawerCallbacks.onNavigationLegendItemSelected(type, active);
				}
			});
		}
	}

	private void prepareMapTypes(View v) {
		TextView normal = (TextView) v.findViewById(R.id.navigation_map_type_normal);
		TextView terrain = (TextView) v.findViewById(R.id.navigation_map_type_terrain);
		TextView satellite = (TextView) v.findViewById(R.id.navigation_map_type_satellite);
		TextView hybrid = (TextView) v.findViewById(R.id.navigation_map_type_hybrid);

		prepareMapType(normal, GoogleMap.MAP_TYPE_NORMAL);
		prepareMapType(terrain, GoogleMap.MAP_TYPE_TERRAIN);
		prepareMapType(satellite, GoogleMap.MAP_TYPE_SATELLITE);
		prepareMapType(hybrid, GoogleMap.MAP_TYPE_HYBRID);
	}

	public void prepareMapType(final TextView v, final int mapType) {
		if (v != null) {
			v.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View view) {
					mapDrawerCallbacks.onNavigationMapTypeSelected(mapType);
				}
			});
		}
	}

	public boolean isDrawerOpen() {
		return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}

	/**
	 * Users of this fragment must call this method to set up the navigation drawer interactions.
	 *
	 * @param fragmentId   The android:id of this fragment in its activity's layout.
	 * @param drawerLayout The DrawerLayout containing this fragment's UI.
	 */
	public void setUp(int fragmentId, DrawerLayout drawerLayout) {
		mFragmentContainerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;

		// set a custom shadow that overlays the main content when the drawer opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// set up the drawer's list view with items and click listener

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the navigation drawer and the action bar app icon.
		mDrawerToggle = new MyDrawerToggle(getActivity(), mDrawerLayout);

		// If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
		// per the navigation drawer design guidelines.
		if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
			mDrawerLayout.openDrawer(mFragmentContainerView);
		}

		// Defer code dependent on restoration of previous instance state.
		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	private void selectMapType(int position) {
		mCurrentSelectedMapType = position;
		if (mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		}
		if(mapDrawerCallbacks != null) {
			//TODO: Parse the map type from the selection in the list
			mapDrawerCallbacks.onNavigationMapTypeSelected(0);
		}
	}

	/**
	 * Per the navigation drawer design guidelines, updates the action bar to show the global app
	 * 'context', rather than just what's in the current screen.
	 */
	private void showGlobalContextActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setTitle(R.string.app_name);
	}

	private ActionBar getActionBar() {
		return getActivity().getActionBar();
	}

	/**
	 * Callbacks interface that all activities using this fragment must implement.
	 */
	public static interface MapNavigationDrawerCallbacks {
		/**
		 * Called when an item in the navigation drawer is selected.
		 */
		void onViewModeChanged(LocationsIndexActivity.ViewMode newMode);
		void onNavigationLegendItemSelected(final Location.Type type,
											final boolean active);
		void onNavigationMapTypeSelected(final int mapType);
	}

	private class MyDrawerToggle extends ActionBarDrawerToggle {
		Activity mActivity;

		public MyDrawerToggle(Activity activity, DrawerLayout layout) {
			super(activity,                    /* host Activity */
					layout,                    /* DrawerLayout object */
					R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
					R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
					R.string.navigation_drawer_close);  /* "close drawer" description for accessibility */
			mActivity = activity;
		}

		@Override public void onDrawerClosed(View drawerView) {
			super.onDrawerClosed(drawerView);
			if (!isAdded()) {
				return;
			}

			mActivity.invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
		}

		@Override public void onDrawerOpened(View drawerView) {
			super.onDrawerOpened(drawerView);
			if (!isAdded()) {
				return;
			}

			if (!mUserLearnedDrawer) {
				// The user manually opened the drawer; store this flag to prevent auto-showing
				// the navigation drawer automatically in the future.
				mUserLearnedDrawer = true;
				SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
				sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
			}

			getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
		}
	}
}

