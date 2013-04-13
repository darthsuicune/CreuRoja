package com.cruzroja.creuroja;

import java.util.ArrayList;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements
		LoaderCallbacks<ArrayList<Location>> {
	public static final int LOADER_CONNECTION = 1;

	GoogleMap mGoogleMap;
	ArrayList<Location> mLocationsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setMap();

		if (isConnected()) {
			// If the device is connected to the internet, start the download
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		// case R.id.menu_settings:
		// return true;
		case R.id.menu_refresh:
			getSupportLoaderManager().restartLoader(LOADER_CONNECTION, null,
					this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private boolean isConnected() {
		ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		return manager.getActiveNetworkInfo().isConnected();
	}

	private void setMap() {
		if (mGoogleMap == null) {
			mGoogleMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
		}
	}

	private void setExtraMapElements() {
		// TODO add markers to the map.
		if (mGoogleMap == null && mLocationsList == null) {
			return;
		}
		for (int i = 0; i < mLocationsList.size(); i++) {
			final Location location = mLocationsList.get(i);
			MarkerOptions marker = new MarkerOptions()
					.position(location.mPosition);
			if (location.mIcono != 0) {
				marker.icon(BitmapDescriptorFactory
						.fromResource(location.mIcono));
			}
			mGoogleMap.addMarker(marker);

			mGoogleMap.setInfoWindowAdapter(new InfoWindowAdapter() {

				@Override
				public View getInfoWindow(Marker arg0) {
					return null;
				}

				@Override
				public View getInfoContents(Marker marker) {
					View v = getLayoutInflater().inflate(R.layout.map_marker,
							null);
					if (location.mContenido != null) {
						if (location.mContenido.mNombre != null) {
							TextView title = (TextView) v
									.findViewById(R.id.location);
							title.setText(location.mContenido.mNombre);
							title.setTextAppearance(MainActivity.this,
									android.R.attr.textAppearanceLarge);
						}
						if (location.mContenido.mSnippet != null) {
							((TextView) v.findViewById(R.id.other_information))
									.setText(location.mContenido.mSnippet);
						}
					}
					return v;
				}
			});
		}
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
