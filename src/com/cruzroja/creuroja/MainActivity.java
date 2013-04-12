package com.cruzroja.creuroja;

import java.util.ArrayList;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements
		LoaderCallbacks<ArrayList<Location>> {
	public static final int LOADER_CONNECTION = 1;

	public static final double INITIAL_LAT = 41.3958;
	public static final double INITIAL_LNG = 2.1739;

	private static final String FRAGMENT_TAG = "mapFragment";

	SupportMapFragment mMapFragment;
	GoogleMap mGoogleMap;
	ArrayList<Location> mLocationsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState != null) {
			return;
		}

		setMap();

		if (isConnected()) {
			// If the device is connected to the internet, start the connection
			getSupportLoaderManager().restartLoader(LOADER_CONNECTION, null,
					this);

		} else {
			// TODO: Here comes what to do without a valid connection, such as
			// showing old markers or whatever
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private boolean isConnected() {
		ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		return manager.getActiveNetworkInfo().isConnected();
	}

	private void setMap() {
		// Create the fragment if it doesn't exist yet and attach it to the
		// screen
		mMapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentByTag(FRAGMENT_TAG);

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		if (mMapFragment == null) {
			mMapFragment = new SupportMapFragment();
			transaction.add(R.id.map_container, mMapFragment, FRAGMENT_TAG);
		} else {
			transaction.attach(mMapFragment);
		}
		transaction.commit();
	}

	private void configureMap() {
		// Load the map from the fragment.
		// If the map is still null we have a problem here
		if (mGoogleMap == null) {
			mGoogleMap = mMapFragment.getMap();

			if (mGoogleMap == null) {
				return;
			}
		}
		mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		mGoogleMap.moveCamera(CameraUpdateFactory.zoomTo(13));
	}

	private void setExtraMapElements() {
		configureMap();
		// TODO add markers to the map
		for (int i = 0; i < mLocationsList.size(); i++) {
			mGoogleMap.addMarker(new MarkerOptions().position(
					mLocationsList.get(i).mPosition).title(
					mLocationsList.get(i).mContenido));
		}

		mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(
				INITIAL_LAT, INITIAL_LNG)));
	}

	public void showProgress(boolean show) {
		// Display a progress screen that would be hidden normally but will be
		// displayed when loading elements
	}

	@Override
	public Loader<ArrayList<Location>> onCreateLoader(int id, Bundle args) {
		Loader<ArrayList<Location>> loader = null;
		switch (id) {
		case LOADER_CONNECTION:
			showProgress(true);
			loader = new ConnectionLoader(this, args);
			break;
		}
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<Location>> loader,
			ArrayList<Location> locations) {
		switch (loader.getId()) {
		case LOADER_CONNECTION:
			mLocationsList = locations;
			setExtraMapElements();
			showProgress(false);
			break;
		}
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<Location>> loader) {
		mLocationsList = null;
	}
}
