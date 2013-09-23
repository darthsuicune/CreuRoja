package com.cruzroja.creuroja;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.*;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.*;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import com.cruzroja.creuroja.utils.ConnectionClient;
import com.cruzroja.creuroja.utils.Settings;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This methods should be modified when new kinds of locations are introduced. -Variable definitions
 * onCreateView onCheckedChanged shouldShowMarker
 * 
 */
public class CRMapFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener,
		CompoundButton.OnCheckedChangeListener, SearchView.OnQueryTextListener {

	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	private static final int LOCATIONS_FILE_LOADER = 1;
	private static final int LOCATIONS_DOWNLOAD_LOADER = 2;
	private static final int DIRECTIONS_LOADER = 3;

	private SharedPreferences prefs;

	private User mUser;

	private ArrayList<Location> mLocationList;

	private GoogleMap mGoogleMap;
	private LocationClient mLocationClient;
	private Polyline mPolyline;

	private CheckBox mAdaptadasCheckBox;
	private CheckBox mAsambleaCheckBox;
	private CheckBox mBravoCheckBox;
	private CheckBox mCuapCheckBox;
	private CheckBox mHospitalCheckBox;
	private CheckBox mMaritimoCheckBox;
	private CheckBox mTerrestreCheckBox;
	private CheckBox mNostrumCheckBox;

	private View mAdaptadasBox;
	private View mAsambleaBox;
	private View mBravoBox;
	private View mCuapBox;
	private View mHospitalBox;
	private View mMaritimoBox;
	private View mTerrestreBox;
	private View mNostrumBox;

	public String mFilter = "";

	private View mMarkerPanel;

	private boolean isMarkerPanelShowing;

	public CRMapFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_map, container, false);
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

		mMarkerPanel = v.findViewById(R.id.marker_panel);

		// Prepare all checkboxes for use
		if (mMarkerPanel != null) {
			mAdaptadasBox = v.findViewById(R.id.box_adaptadas);
			mAsambleaBox = v.findViewById(R.id.box_asamblea);
			mBravoBox = v.findViewById(R.id.box_bravo);
			mCuapBox = v.findViewById(R.id.box_cuap);
			mHospitalBox = v.findViewById(R.id.box_hospital);
			mMaritimoBox = v.findViewById(R.id.box_maritimo);
			mTerrestreBox = v.findViewById(R.id.box_terrestre);
			mNostrumBox = v.findViewById(R.id.box_nostrum);

			mAdaptadasCheckBox = (CheckBox) v.findViewById(R.id.checkbox_adaptadas);
			mAsambleaCheckBox = (CheckBox) v.findViewById(R.id.checkbox_asamblea);
			mBravoCheckBox = (CheckBox) v.findViewById(R.id.checkbox_bravo);
			mCuapCheckBox = (CheckBox) v.findViewById(R.id.checkbox_cuap);
			mHospitalCheckBox = (CheckBox) v.findViewById(R.id.checkbox_hospital);
			mMaritimoCheckBox = (CheckBox) v.findViewById(R.id.checkbox_maritimo);
			mTerrestreCheckBox = (CheckBox) v.findViewById(R.id.checkbox_terrestre);
			mNostrumCheckBox = (CheckBox) v.findViewById(R.id.checkbox_nostrum);

			mAdaptadasCheckBox.setChecked(prefs.getBoolean(Settings.SHOW_ADAPTADAS, true));
			mAsambleaCheckBox.setChecked(prefs.getBoolean(Settings.SHOW_ASAMBLEA, true));
			mBravoCheckBox.setChecked(prefs.getBoolean(Settings.SHOW_BRAVO, true));
			mCuapCheckBox.setChecked(prefs.getBoolean(Settings.SHOW_CUAP, true));
			mHospitalCheckBox.setChecked(prefs.getBoolean(Settings.SHOW_HOSPITAL, true));
			mMaritimoCheckBox.setChecked(prefs.getBoolean(Settings.SHOW_MARITIMO, true));
			mTerrestreCheckBox.setChecked(prefs.getBoolean(Settings.SHOW_TERRESTRE, true));
			mNostrumCheckBox.setChecked(prefs.getBoolean(Settings.SHOW_NOSTRUM, true));

			mAdaptadasCheckBox.setOnCheckedChangeListener(this);
			mAsambleaCheckBox.setOnCheckedChangeListener(this);
			mBravoCheckBox.setOnCheckedChangeListener(this);
			mCuapCheckBox.setOnCheckedChangeListener(this);
			mHospitalCheckBox.setOnCheckedChangeListener(this);
			mMaritimoCheckBox.setOnCheckedChangeListener(this);
			mTerrestreCheckBox.setOnCheckedChangeListener(this);
			mNostrumCheckBox.setOnCheckedChangeListener(this);

			setCheckboxesVisibility();
		}
		return v;
	}

	private void setCheckboxesVisibility() {
		if (mUser == null) {
			mUser = User.getSavedUser(prefs);
			if (mUser == null) {
				return;
			}
		}

		mAdaptadasBox.setVisibility(mUser.canSeeCheckBox(R.drawable.adaptadas));
		mAsambleaBox.setVisibility(mUser.canSeeCheckBox(R.drawable.asamblea));
		mBravoBox.setVisibility(mUser.canSeeCheckBox(R.drawable.bravo));
		mCuapBox.setVisibility(mUser.canSeeCheckBox(R.drawable.cuap));
		mHospitalBox.setVisibility(mUser.canSeeCheckBox(R.drawable.hospital));
		mMaritimoBox.setVisibility(mUser.canSeeCheckBox(R.drawable.maritimo));
		mTerrestreBox.setVisibility(mUser.canSeeCheckBox(R.drawable.terrestre));
		mNostrumBox.setVisibility(mUser.canSeeCheckBox(R.drawable.nostrum));
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		requestGooglePlayServicesAvailability();
		SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager()
				.findFragmentById(R.id.map);

		if (savedInstanceState == null) {
			mapFragment.setRetainInstance(true);
			downloadNewData();
		} else {
			mGoogleMap = mapFragment.getMap();
		}

		loadDataFromFile();
		setMap();

		mLocationClient = new LocationClient(getActivity(), this, this);

	}

	@Override
	public void onStart() {
		super.onStart();
		// Connect the client.
		mLocationClient.connect();
	}

	@Override
	public void onStop() {
		// Disconnecting the client invalidates it.
		mLocationClient.disconnect();
		super.onStop();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_map, menu);
		setSearchOptions(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_locate:
			moveToCurrentLocation();
			return true;
		case R.id.search:
			// Nothing needed here thanks to the support library
			return true;
		case android.R.id.home:
		case R.id.menu_show_panel:
			showMarkerPanel();
			return true;
		case R.id.menu_refresh:
			downloadNewData();
			return true;
		case R.id.menu_show_hybrid:
			return setMapStyle(Settings.MAP_STYLE_HYBRID);
		case R.id.menu_show_normal:
			return setMapStyle(Settings.MAP_STYLE_NORMAL);
		case R.id.menu_show_satellite:
			return setMapStyle(Settings.MAP_STYLE_SATELLITE);
		case R.id.menu_show_terrain:
			return setMapStyle(Settings.MAP_STYLE_TERRAIN);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public boolean onBackPressed() {
		if (isMarkerPanelShowing) {
			mMarkerPanel.setVisibility(View.GONE);
			isMarkerPanelShowing = false;
			return true;
		}
		return isMarkerPanelShowing;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
			switch (resultCode) {
			case Activity.RESULT_OK:
				requestGooglePlayServicesAvailability();
				break;
			}
		}
	}

	/**
	 * Check that Google Play services is available
	 */
	private boolean requestGooglePlayServicesAvailability() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			return true;
			// Google Play services was not available for some reason
		} else {
			// Get the error dialog from Google Play services
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
					CONNECTION_FAILURE_RESOLUTION_REQUEST);

			// If Google Play services can provide an error dialog
			if (errorDialog != null) {
				// Create a new DialogFragment for the error dialog
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				// Set the dialog in the DialogFragment
				errorFragment.setDialog(errorDialog);
				// Show the error dialog in the DialogFragment
				errorFragment.show(getFragmentManager(), "Location Updates");
			}
		}
		return false;
	}

	private void loadDataFromFile() {
		getLoaderManager().restartLoader(LOCATIONS_FILE_LOADER, null, new LocationsLoaderHelper());
	}

	private void downloadNewData() {
		if (ConnectionClient.isConnected(getActivity())) {
			getLoaderManager().restartLoader(LOCATIONS_DOWNLOAD_LOADER, null,
					new LocationsLoaderHelper());
		} else {
			Toast.makeText(getActivity(), R.string.error_no_connection, Toast.LENGTH_LONG).show();
		}
	}

	/***********************
	 * General map methods *
	 ***********************/

	private void setMap() {
		if (mGoogleMap == null) {
			mGoogleMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map))
					.getMap();
			if (mGoogleMap != null) {
				setMapStyle(prefs.getInt(Settings.MAP_STYLE, Settings.MAP_STYLE_NORMAL));
				mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.setIndoorEnabled(false);
				mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
				mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
				mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
				mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
						new LatLng(41.3958, 2.1739), 12));
			}
		}
		drawMarkers();
	}

	private boolean setMapStyle(int mapStyle) {
		if (mGoogleMap == null) {
			return false;
		}

		switch (mapStyle) {
		case Settings.MAP_STYLE_NORMAL:
			mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			break;
		case Settings.MAP_STYLE_HYBRID:
			mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			break;
		case Settings.MAP_STYLE_SATELLITE:
			mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			break;
		case Settings.MAP_STYLE_TERRAIN:
			mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			break;
		default:
			return false;
		}
		prefs.edit().putInt(Settings.MAP_STYLE, mapStyle).commit();
		return true;
	}

	private void moveToCurrentLocation() {
		if (mGoogleMap != null) {
			if (areLocationServicesEnabled()) {
				if (mLocationClient.getLastLocation() != null) {
					mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(
							mLocationClient.getLastLocation().getLatitude(), mLocationClient
									.getLastLocation().getLongitude())));
				} else {
					Toast.makeText(getActivity(), R.string.locating, Toast.LENGTH_SHORT).show();
				}
			} else {
				showLocationSettings();
			}
		}
	}

	private boolean areLocationServicesEnabled() {
		LocationManager lm = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);
		if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
				|| lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			return true;
		}
		return false;
	}

	/************************
	 * Item drawing methods *
	 ************************/

	private void drawMarkers() {
		if (mGoogleMap == null || mLocationList == null) {
			return;
		}

		mGoogleMap.clear();

		if (mPolyline != null) {
			drawLine(mPolyline.getPoints());
		}

		for (Location location : mLocationList) {
			if (!shouldShowMarker(location, mFilter)) {
				continue;
			}

			MarkerOptions marker = new MarkerOptions().position(new LatLng(location.mLat,
					location.mLong));
			if (location.mIcono != 0) {
				marker.icon(BitmapDescriptorFactory.fromResource(location.mIcono));
			}
			if (location.mContenido.mNombre != null) {
				marker.title(location.mContenido.mNombre);
			}
			if (location.mContenido.mSnippet != null) {
				marker.snippet(location.mContenido.mSnippet);
			}

			mGoogleMap.addMarker(marker);
		}
		mGoogleMap.setInfoWindowAdapter(new MarkerAdapter());
		mGoogleMap.setOnInfoWindowClickListener(this);
	}

	private void drawLine(Collection<LatLng> points) {
		if (mPolyline != null) {
			mPolyline.remove();
		}
		if (mGoogleMap == null || points == null) {
			return;
		}
		if (points.size() == 0) {
			Toast.makeText(getActivity(), R.string.error_limit_reached, Toast.LENGTH_LONG).show();
		}

		mPolyline = mGoogleMap.addPolyline(new PolylineOptions().addAll(points).color(
				Color.parseColor("#CC0000")));
	}

	private void showMarkerPanel() {
		if (isMarkerPanelShowing) {
			mMarkerPanel.setVisibility(View.GONE);
			isMarkerPanelShowing = false;
		} else {
			isMarkerPanelShowing = true;
			mMarkerPanel.setVisibility(View.VISIBLE);
		}
	}

	private boolean shouldShowMarker(Location location, String filter) {
		if ((mUser == null) || !matchFilter(location, filter)) {
			return false;
		}
		switch (location.mIcono) {
		case R.drawable.adaptadas:
			return prefs.getBoolean(Settings.SHOW_ADAPTADAS, true)
					&& mUser.canSeeMarker(R.drawable.adaptadas);
		case R.drawable.asamblea:
			return prefs.getBoolean(Settings.SHOW_ASAMBLEA, true)
					&& mUser.canSeeMarker(R.drawable.asamblea);
		case R.drawable.bravo:
			return prefs.getBoolean(Settings.SHOW_BRAVO, true)
					&& mUser.canSeeMarker(R.drawable.bravo);
		case R.drawable.cuap:
			return prefs.getBoolean(Settings.SHOW_CUAP, true)
					&& mUser.canSeeMarker(R.drawable.cuap);
		case R.drawable.hospital:
			return prefs.getBoolean(Settings.SHOW_HOSPITAL, true)
					&& mUser.canSeeMarker(R.drawable.hospital);
		case R.drawable.maritimo:
			return prefs.getBoolean(Settings.SHOW_MARITIMO, true)
					&& mUser.canSeeMarker(R.drawable.maritimo);
		case R.drawable.terrestre:
			return prefs.getBoolean(Settings.SHOW_TERRESTRE, true)
					&& mUser.canSeeMarker(R.drawable.terrestre);
		case R.drawable.nostrum:
			return prefs.getBoolean(Settings.SHOW_NOSTRUM, true)
					&& mUser.canSeeMarker(R.drawable.nostrum);
		default:
			return true;
		}
	}

	private boolean matchFilter(Location location, String filter) {
		if (filter != null) {
			filter = dehyphenize(filter);
			String nombre = dehyphenize(location.mContenido.mNombre);
			String lugar = null;
			if (location.mContenido.mLugar != null) {
				lugar = dehyphenize(location.mContenido.mLugar);
			}
			if (!nombre.contains(filter)) {
				if (lugar == null || !lugar.contains(filter)) {
					return false;
				}
			}
		}
		return true;
	}

	@SuppressLint("DefaultLocale")
	private String dehyphenize(String input) {
		input = input.toLowerCase();
		return input.replace("à", "a").replace("á", "a").replace("é", "e").replace("è", "e")
				.replace("í", "i").replace("ì", "i").replace("ó", "o").replace("ò", "o")
				.replace("ú", "u").replace("ù", "u");
	}

	/**************************
	 * Location related calls *
	 **************************/

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		SharedPreferences.Editor editor = prefs.edit();
		switch (buttonView.getId()) {
		case R.id.checkbox_adaptadas:
			editor.putBoolean(Settings.SHOW_ADAPTADAS, isChecked);
			break;
		case R.id.checkbox_asamblea:
			editor.putBoolean(Settings.SHOW_ASAMBLEA, isChecked);
			break;
		case R.id.checkbox_bravo:
			editor.putBoolean(Settings.SHOW_BRAVO, isChecked);
			break;
		case R.id.checkbox_cuap:
			editor.putBoolean(Settings.SHOW_CUAP, isChecked);
			break;
		case R.id.checkbox_hospital:
			editor.putBoolean(Settings.SHOW_HOSPITAL, isChecked);
			break;
		case R.id.checkbox_maritimo:
			editor.putBoolean(Settings.SHOW_MARITIMO, isChecked);
			break;
		case R.id.checkbox_terrestre:
			editor.putBoolean(Settings.SHOW_TERRESTRE, isChecked);
			break;
		case R.id.checkbox_nostrum:
			editor.putBoolean(Settings.SHOW_NOSTRUM, isChecked);
			break;
		}
		editor.commit();
		drawMarkers();
	}

	private class LocationsLoaderHelper implements LoaderCallbacks<ArrayList<Location>> {

		@Override
		public Loader<ArrayList<Location>> onCreateLoader(int id, Bundle args) {
			if (ConnectionClient.isConnected(getActivity())) {
				switch (id) {
				case LOCATIONS_DOWNLOAD_LOADER:
					return new LocationsLoader(getActivity(), false);
				case LOCATIONS_FILE_LOADER:
					return new LocationsLoader(getActivity(), true);
				default:
					return null;
				}
			} else {
				Toast.makeText(getActivity(), R.string.error_no_connection, Toast.LENGTH_LONG)
						.show();
				return null;
			}

		}

		@Override
		public void onLoadFinished(Loader<ArrayList<Location>> loader, ArrayList<Location> locations) {
			mLocationList = locations;
			drawMarkers();
		}

		@Override
		public void onLoaderReset(Loader<ArrayList<Location>> loader) {
		}

	}

	private class MarkerAdapter implements GoogleMap.InfoWindowAdapter {
		@Override
		public View getInfoWindow(Marker marker) {
			return null;
		}

		@Override
		public View getInfoContents(Marker marker) {
			View v = ((LayoutInflater) getActivity().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker, null);

			((TextView) v.findViewById(R.id.location)).setText(marker.getTitle());

			if (marker.getSnippet() != null) {
				String address = marker.getSnippet().substring(0,
						marker.getSnippet().indexOf(Location.MARKER_NEW_LINE));
				String other = marker.getSnippet().substring(
						marker.getSnippet().indexOf(Location.MARKER_NEW_LINE)
								+ Location.MARKER_NEW_LINE.length(), marker.getSnippet().length());
				((TextView) v.findViewById(R.id.address)).setText(address);
				((TextView) v.findViewById(R.id.other_information)).setText(other);
			}

			return v;
		}
	}

	/***************************
	 * Direction related calls *
	 ***************************/

	@Override
	public void onInfoWindowClick(Marker marker) {
		if (marker.getPosition() == null || !mGoogleMap.isMyLocationEnabled()) {
			return;
		}
		if (areLocationServicesEnabled()) {
			if (mLocationClient.getLastLocation() != null) {
				Bundle args = new Bundle();
				args.putDouble(DirectionsLoader.ARG_ORIG_LAT, mLocationClient.getLastLocation()
						.getLatitude());
				args.putDouble(DirectionsLoader.ARG_ORIG_LONG, mLocationClient.getLastLocation()
						.getLongitude());
				args.putDouble(DirectionsLoader.ARG_DEST_LAT, marker.getPosition().latitude);
				args.putDouble(DirectionsLoader.ARG_DEST_LONG, marker.getPosition().longitude);
				getLoaderManager().restartLoader(DIRECTIONS_LOADER, args,
						new DirectionsLoaderHelper());
			} else {
				Toast.makeText(getActivity(), R.string.locating, Toast.LENGTH_SHORT).show();
			}
		} else {
			showLocationSettings();
		}

	}

	private class DirectionsLoaderHelper implements LoaderCallbacks<ArrayList<LatLng>> {

		@Override
		public Loader<ArrayList<LatLng>> onCreateLoader(int id, Bundle args) {
			if (ConnectionClient.isConnected(getActivity())) {
				return new DirectionsLoader(getActivity(), args);
			} else {
				Toast.makeText(getActivity(), R.string.error_no_connection, Toast.LENGTH_LONG)
						.show();
				return null;
			}
		}

		@Override
		public void onLoadFinished(Loader<ArrayList<LatLng>> loader, ArrayList<LatLng> directions) {
			drawPolyline(directions);
		}

		@Override
		public void onLoaderReset(Loader<ArrayList<LatLng>> loader) {
		}

	}

	private void drawPolyline(ArrayList<LatLng> directions) {
		if (mPolyline != null) {
			mPolyline.remove();
		}
		if (mGoogleMap == null || directions == null) {
			return;
		}
		if (directions.size() == 0) {
			Toast.makeText(getActivity(), R.string.error_limit_reached, Toast.LENGTH_LONG).show();
		}

		mPolyline = mGoogleMap.addPolyline(new PolylineOptions().addAll(directions).color(
				Color.parseColor("#CC0000")));
	}

	/*****************************
	 * Calls for Google Services *
	 *****************************/
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

		/*
		 * Google Play services can resolve some errors it detects. If the error has a resolution,
		 * try sending an Intent to start a Google Play services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(getActivity(),
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			Toast.makeText(getActivity(), R.string.location_unavailable, Toast.LENGTH_LONG).show();
		}
	}

	private void showLocationSettings() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.location_disabled_title);
		builder.setMessage(R.string.location_disabled_message);
		builder.setPositiveButton(R.string.open_location_settings,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface, int i) {
						startActivity(new Intent(
								android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					}
				});
		builder.setNegativeButton(R.string.cancel, null);
		builder.create().show();
	}

	// Define a DialogFragment that displays the error dialog
	public static class ErrorDialogFragment extends DialogFragment {
		// Global field to contain the error dialog
		private Dialog mDialog;

		// Default constructor. Sets the dialog field to null
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		// Set the dialog to display
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		// Return a Dialog to the DialogFragment.
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}

	/************************
	 * Search related calls *
	 ************************/

	@Override
	public boolean onQueryTextChange(String newText) {
		mFilter = newText;
		drawMarkers();
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		mFilter = query;
		drawMarkers();
		return false;
	}

	private void setSearchOptions(Menu menu) {
		SearchManager searchManager = (SearchManager) getActivity().getSystemService(
				Context.SEARCH_SERVICE);
		MenuItem searchMenuItem = menu.findItem(R.id.search);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
		if (searchView != null) {
			searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity()
					.getComponentName()));

			searchView.setOnQueryTextListener(this);
		}
	}

	/****************
	 * Other calls *
	 ****************/

	public void setUser(User user) {
		mUser = user;
		setCheckboxesVisibility();
		drawMarkers();
	}

	@Override
	public void onConnected(Bundle args) {
	}

	@Override
	public void onDisconnected() {
	}
}
