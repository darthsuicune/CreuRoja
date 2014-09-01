package net.creuroja.android.view.fragments.locations;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.creuroja.android.R;
import net.creuroja.android.controller.locations.activities.LocationsIndexActivity;
import net.creuroja.android.model.Settings;
import net.creuroja.android.model.locations.LocationType;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {
	private static final int selected = Color.CYAN;
	private static final int unselected = Color.TRANSPARENT;

	private MapNavigationDrawerCallbacks mapDrawerCallbacks;

	// Helper component that ties the action bar to the navigation drawer.
	private ActionBarDrawerToggle mDrawerToggle;

	private DrawerLayout mDrawerLayout;
	private View mFragmentContainerView;

	private boolean mFromSavedInstanceState;

	private SharedPreferences prefs;

	public NavigationDrawerFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

		if (savedInstanceState != null) {
			mFromSavedInstanceState = true;
		}
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
		prepareLegendItems(v);
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
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void prepareViewModes(View v) {
		TextView viewMap = (TextView) v.findViewById(R.id.navigation_drawer_section_map);
		TextView viewList = (TextView) v.findViewById(R.id.navigation_drawer_section_list);

		prepareViewMode(viewMap, LocationsIndexActivity.ViewMode.MAP);
		prepareViewMode(viewList, LocationsIndexActivity.ViewMode.LIST);
	}

	private void prepareViewMode(final TextView v, final LocationsIndexActivity.ViewMode mode) {
		v.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				mapDrawerCallbacks.onViewModeChanged(mode);
				prefs.edit().putInt(Settings.VIEW_MODE, mode.getValue()).apply();
			}
		});
	}

	private void prepareLegendItems(View v) {
		for (LocationType type : LocationType.getCurrentItems(getActivity().getContentResolver())) {
			prepareLegendItem((TextView) v.findViewById(type.mLegendViewId), type);
		}
	}

	public void prepareLegendItem(final TextView v, final LocationType type) {
		if (v != null) {
			v.setVisibility(View.VISIBLE);
			v.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View view) {
					boolean newState = !type.getViewable(prefs);
					prefs.edit().putBoolean(type.mPrefs, newState).apply();
					mapDrawerCallbacks.onNavigationLegendItemSelected(type, newState);
					changeToggleBackground(view, newState);
				}
			});
			changeToggleBackground(v, type.getViewable(prefs));
		}
	}

	private void changeToggleBackground(final View v, boolean newState) {
		v.setBackgroundColor(newState ? selected : unselected);
	}

	private void prepareMapTypes(View v) {
		TextView normal = (TextView) v.findViewById(R.id.navigation_map_type_normal);
		TextView terrain = (TextView) v.findViewById(R.id.navigation_map_type_terrain);
		TextView satellite = (TextView) v.findViewById(R.id.navigation_map_type_satellite);
		TextView hybrid = (TextView) v.findViewById(R.id.navigation_map_type_hybrid);

		prepareMapType(normal, MapFragmentHandler.MapType.MAP_TYPE_NORMAL);
		prepareMapType(terrain, MapFragmentHandler.MapType.MAP_TYPE_TERRAIN);
		prepareMapType(satellite, MapFragmentHandler.MapType.MAP_TYPE_SATELLITE);
		prepareMapType(hybrid, MapFragmentHandler.MapType.MAP_TYPE_HYBRID);
	}

	public void prepareMapType(final TextView v, final MapFragmentHandler.MapType mapType) {
		if (v != null) {
			v.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View view) {
					mapDrawerCallbacks.onNavigationMapTypeSelected(mapType);
					prefs.edit().putInt(Settings.MAP_TYPE, mapType.getValue()).apply();
					toggleMapType();
				}
			});
		}
	}

	private void toggleMapType() {
		if (getActivity() != null) {
			TextView normal =
					(TextView) getActivity().findViewById(R.id.navigation_map_type_normal);
			TextView terrain =
					(TextView) getActivity().findViewById(R.id.navigation_map_type_terrain);
			TextView satellite =
					(TextView) getActivity().findViewById(R.id.navigation_map_type_satellite);
			TextView hybrid =
					(TextView) getActivity().findViewById(R.id.navigation_map_type_hybrid);

			MapFragmentHandler.MapType type = MapFragmentHandler.MapType.fromValue(
					prefs.getInt(Settings.MAP_TYPE,
							MapFragmentHandler.MapType.MAP_TYPE_NORMAL.getValue()));

			changeToggleBackground(normal, MapFragmentHandler.MapType.MAP_TYPE_NORMAL == type);
			changeToggleBackground(terrain, MapFragmentHandler.MapType.MAP_TYPE_TERRAIN == type);
			changeToggleBackground(satellite,
					MapFragmentHandler.MapType.MAP_TYPE_SATELLITE == type);
			changeToggleBackground(hybrid, MapFragmentHandler.MapType.MAP_TYPE_HYBRID == type);
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

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the navigation drawer and the action bar app icon.
		mDrawerToggle = new MyDrawerToggle(getActivity(), mDrawerLayout);

		// If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
		// per the navigation drawer design guidelines.

		boolean userLearnedDrawer = prefs.getBoolean(Settings.PREF_USER_LEARNED_DRAWER, false);
		if (!userLearnedDrawer && !mFromSavedInstanceState) {
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
		toggleMapType();
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
		return ((ActionBarActivity) getActivity()).getSupportActionBar();
	}

	/**
	 * Callbacks interface that all activities using this fragment must implement.
	 */
	public static interface MapNavigationDrawerCallbacks {
		/**
		 * Called when an item in the navigation drawer is selected.
		 */
		void onViewModeChanged(LocationsIndexActivity.ViewMode newMode);

		void onNavigationLegendItemSelected(final LocationType type, final boolean newState);

		void onNavigationMapTypeSelected(final MapFragmentHandler.MapType mapType);
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
			if (Build.VERSION.SDK_INT >= 11) {
				mActivity.invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
			}
		}

		@Override public void onDrawerOpened(View drawerView) {
			super.onDrawerOpened(drawerView);
			if (!isAdded()) {
				return;
			}
			// The user manually opened the drawer; store this flag to prevent auto-showing
			// the navigation drawer automatically in the future.
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
			if (!sp.getBoolean(Settings.PREF_USER_LEARNED_DRAWER, false)) {
				sp.edit().putBoolean(Settings.PREF_USER_LEARNED_DRAWER, true).apply();
			}

			if (Build.VERSION.SDK_INT >= 11) {
				getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
			}
		}
	}
}
